package com.ssafy.a802.jaljara.api.service;

import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.SleepLogResponseDto;
import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import com.ssafy.a802.jaljara.db.entity.MissionLog;
import com.ssafy.a802.jaljara.db.entity.SleepLog;
import com.ssafy.a802.jaljara.db.repository.ChildInformationRepository;
import com.ssafy.a802.jaljara.db.repository.SleepLogRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionLogRepository;
import com.ssafy.a802.jaljara.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class SleepLogService {
    private final SleepLogRepository sleepLogRepository;
    private final MissionLogRepository missionLogRepository;
    private final ChildInformationRepository childInformationRepository;

    public void addSleepLogs(List<SleepLogRequestDto.SleepSegmentEvent> sleepSegmentEvents, long childId){

        if(sleepSegmentEvents == null) throw new CustomException(HttpStatus.BAD_REQUEST, "저장할 수면 정보가 없습니다.");

        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND,
                        "부모와 연결되지 않은 유저입니다. userId: " + childId));

        LocalDateTime _now = LocalDateTime.now();

        log.info("오늘 날짜 : " + ZonedDateTime.of(_now, ZoneId.systemDefault()).toInstant().toEpochMilli());

        //목표취침,기상시간을 분단위로 치환
        int tBed = childInformation.getTargetBedTime().toLocalTime().getHour() * 60 + childInformation.getTargetBedTime().toLocalTime().getMinute();
        int tWake = childInformation.getTargetWakeupTime().toLocalTime().getHour() * 60 + childInformation.getTargetWakeupTime().toLocalTime().getMinute();


        // 이후의 모든 시간은 24시를 0으로 취급

        // 오후 12시 이후 이면
        // 12:00 ~ 23:59
        // -12:00 ~ -00:01 음수로 convert
        if(tBed >= 12*60) tBed -= 24 * 60;

        int sleepLen = tWake - tBed;

        for(SleepLogRequestDto.SleepSegmentEvent event: sleepSegmentEvents){
            // 수면 status가 성공
            if(event.getStatus() == SleepLogRequestDto.SleepSegmentEvent.STATUS_SUCCESSFUL
            ){
                // 수면 세그먼트 감지 시작 시간
                LocalDateTime start2LDT = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getStartTimeMillis()), ZoneId.systemDefault());

                // 수면 세그먼트 감지 끝난 시간
                LocalDateTime end2LDT = LocalDateTime.ofInstant(Instant.ofEpochMilli(event.getEndTimeMills()), ZoneId.systemDefault());

                // 시작 시간 -> 분으로
                int start2min = start2LDT.getHour() * 60 + start2LDT.getMinute();
                if(start2min >= 12 * 60) start2min -= 24 * 60;

                // 끝 시간 -> 분으로
                int end2min = end2LDT.getHour() * 60 + end2LDT.getMinute();
                if(end2min >= 12 * 60) end2min -= 24 * 60;

                log.info("start2min : " + start2min + "end2min : " + end2min);

                if(end2min >= tBed){
                    int endMin = Math.min(end2min, tWake);
                    int startMin = Math.max(start2min, tBed);
                    double tRate = (1D * endMin - startMin)/sleepLen;

                    // ~ 23:59 -> 오늘에 저장
                    if(endMin < 0){
                        Date dateToday = Timestamp.valueOf(end2LDT);

                        log.info("오늘에 저장 : " + dateToday);
                        Optional<SleepLog> optLog =  sleepLogRepository.findByUserIdAndDate(childId, dateToday);

                        if(optLog.isEmpty()){
                            sleepLogRepository.save(
                                    SleepLog.builder()
                                            .sleepRate(tRate)
                                            .userId(childId)
                                            .bedTime(min2Time(startMin))
                                            .wakeupTime(min2Time(endMin))
                                            .date(dateToday).build()
                            );
                        }
//                        else{
//                            SleepLog tlog = optLog.get();
//                            sleepLogRepository.save(
//                                    SleepLog.builder()
//                                            .id(tlog.getId())
//                                            .sleepRate(tlog.getSleepRate() + tRate)
//                                            .userId(tlog.getUserId())
//                                            .bedTime(tlog.getBedTime())
//                                            .wakeupTime(min2Time(endMin))
//                                            .date(dateToday).build()
//                            );
//                        }
                    }else{ // 00:00 ~ 11:59 -> 어제 저장
                        Date dateYesterDay = Timestamp.valueOf(end2LDT.minusDays(1));
                        log.info("어제에 저장 : " + dateYesterDay);
                        Optional<SleepLog> optLog = sleepLogRepository.findByUserIdAndDate(childId, dateYesterDay);
                        if(optLog.isEmpty()){
                            sleepLogRepository.save(
                                    SleepLog.builder()
                                            .sleepRate(tRate)
                                            .userId(childId)
                                            .bedTime(min2Time(startMin))
                                            .wakeupTime(min2Time(endMin))
                                            .date(dateYesterDay).build()
                            );
                        }
//                        else{
//                            SleepLog tlog = optLog.get();
//                            sleepLogRepository.save(
//                                    SleepLog.builder()
//                                            .id(tlog.getId())
//                                            .sleepRate(tlog.getSleepRate() + tRate)
//                                            .userId(tlog.getUserId())
//                                            .bedTime(tlog.getBedTime())
//                                            .wakeupTime(min2Time(endMin))
//                                            .date(dateYesterDay).build()
//                            );
//                        }
                    }
                }
            }
        }
    }

    private Time min2Time(int min){
        StringBuilder sb = new StringBuilder();

        if(min <0 ) min += 60 * 24;

        int hour = min/60;
        if(hour < 10) sb.append(0);
        sb.append(hour).append(':');

        int minute = min%60;
        if(minute < 10) sb.append(0);
        sb.append(minute).append(':');

        sb.append("00");

        // Time.valueOf("22:00:00");
        return Time.valueOf(sb.toString());
    }

    public List<Integer> findMissionCompleteDayListByMonth(long childId, String date) throws ParseException {
        //그 달의 마지막 날 구하기
        String days = Integer.toString(LocalDate.parse(date.concat("01"), DateTimeFormatter.ofPattern("yyyyMMdd")).lengthOfMonth());

        //입력된 달의 1일부터 그 달의 마지막 일 중 미션 성공한 날 조회
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        List<MissionLog> missionLogs = missionLogRepository.findAllByUserIdAndMissionDateBetweenAndIsSuccess(
                childId,
                formatter.parse(date.concat("01")),
                formatter.parse(date.concat(days)),
                true);

        List<Integer> daysExistsLog = new ArrayList<>();
        //성공한 미션기록이 저장된 날의 일(day)만 List에 저장
        for(MissionLog missionLog : missionLogs)
            daysExistsLog.add(missionLog.getMissionDate().getDate());

        return daysExistsLog;
    }

    public SleepLogResponseDto.SleepLogDetail findSleepLogByDay(long childId, String date) throws ParseException {
        //해당 유저가 해당 날짜에 측정된 수면 기록이 없는 경우
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SleepLog sleepLog = sleepLogRepository.findByUserIdAndDate(childId, formatter.parse(date)).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "해당 날짜에 측정된 수면 기록이 없습니다. userId: " + childId + ", date: " + date));

        return SleepLogResponseDto.SleepLogDetail.builder()
                .userId(childId)
                .bedTime(sleepLog.getBedTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .wakeupTime(sleepLog.getWakeupTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .date(sleepLog.getDate())
                .sleepRate(sleepLog.getSleepRate()).build();
    }

    @Scheduled(cron = "0 10 12 * * *", zone = "Asia/Seoul")
    public void checkGoodSleep(){
        List<ChildInformation> childInformations = childInformationRepository.findAll();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = calendar.getTime();

        for(ChildInformation childInformation : childInformations){
            long childId = childInformation.getChildId();

            SleepLog sleepLog = sleepLogRepository.findByUserIdAndDate(childId, yesterday).orElse(null);
            MissionLog missionLog = missionLogRepository.findByUserIdAndMissionDate(childId, yesterday).orElse(null);

            if(sleepLog != null && missionLog != null && sleepLog.getSleepRate() >= 0.8 && missionLog.isSuccess()){
                childInformationRepository.save(
                        childInformation.toBuilder()
                                .streakCount(childInformation.getStreakCount() + 1)
                                .build());
            }

        }
    }

}
