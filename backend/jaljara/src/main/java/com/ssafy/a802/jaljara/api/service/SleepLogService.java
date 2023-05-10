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
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SleepLogService {
    private final SleepLogRepository sleepLogRepository;
    private final MissionLogRepository missionLogRepository;
    private final ChildInformationRepository childInformationRepository;

    public void addSleepLog(SleepLogRequestDto.SleepLogInput sleepLogInput){
        long childId = sleepLogInput.getUserId();
        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND,
                        "부모와 연결되지 않은 유저입니다. userId: " + childId));

        //해당 날짜에 이미 수면 기록이 존재하는 경우
        if(sleepLogRepository.existsByUserIdAndDate(childId, sleepLogInput.getDate()))
            throw new CustomException(HttpStatus.CONFLICT,
                    "해당 날짜에 수면기록이 이미 존재합니다. userId: " + childId + ", date: " + sleepLogInput.getDate());

        //수면달성도 계산
        //목표취침,기상시간과 실제취침,기상시간을 분단위로 치환
        int tBed = childInformation.getTargetBedTime().toLocalTime().getHour() * 60 + childInformation.getTargetBedTime().toLocalTime().getMinute();
        int tWake = childInformation.getTargetWakeupTime().toLocalTime().getHour() * 60 + childInformation.getTargetWakeupTime().toLocalTime().getMinute();
        int bed = sleepLogInput.getBedTime().toLocalTime().getHour() * 60 + sleepLogInput.getBedTime().toLocalTime().getMinute();
        int wake = sleepLogInput.getWakeupTime().toLocalTime().getHour() * 60 + sleepLogInput.getWakeupTime().toLocalTime().getMinute();

        //항상 취침 < 기상이 되도록 조정
        if(tBed >= tWake)
            tBed -= 60 * 24;
        if(bed >= wake)
            bed -= 60 * 24;

        //목표로 한 수면시간 중 실제로 수면을 취한 비율을 계산해 수면달성도로 저장
        double sleepTime = Math.min(wake, tWake) - Math.max(bed, tBed);
        if(sleepTime < 0)
            sleepTime = 0;
        double tSleepTime = tWake - tBed;
        double sleepRate = sleepTime / tSleepTime;

        //수면기록 저장
        sleepLogRepository.save(SleepLog.builder()
                .userId(sleepLogInput.getUserId())
                .bedTime(sleepLogInput.getBedTime())
                .wakeupTime(sleepLogInput.getWakeupTime())
                .date(sleepLogInput.getDate())
                .sleepRate(sleepRate).build());
    }

    public List<Integer> findSleepLogByMonth(long childId, String date) throws ParseException {
        //입력된 달의 1일부터 31일 중 저장된 모든 수면기록 조회
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        List<SleepLog> sleepLogs = sleepLogRepository.findAllByUserIdAndDateBetween(
                childId,
                formatter.parse(date.concat("01")),
                formatter.parse(date.concat("31")));

        //입력된 달의 1일부터 31일 중 저장된 모든 미션기록 조회
        List<MissionLog> missionLogs = missionLogRepository.findAllByUserIdAndMissionDateBetween(
                childId,
                formatter.parse(date.concat("01")),
                formatter.parse(date.concat("31")));

        //수면기록이 저장된 날의 일(day)만 List에 저장
        List<Integer> daysExistsLog = new ArrayList<>();
        for(SleepLog sleepLog : sleepLogs)
            daysExistsLog.add(sleepLog.getDate().getDate());

        //미션기록이 저장된 날의 일(day)만 List에 저장
        for(MissionLog missionLog : missionLogs)
            daysExistsLog.add(missionLog.getMissionDate().getDate());

        //중복을 제거하여 return
        return daysExistsLog.stream().distinct().collect(Collectors.toList());
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
}
