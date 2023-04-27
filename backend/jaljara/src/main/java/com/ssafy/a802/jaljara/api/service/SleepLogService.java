package com.ssafy.a802.jaljara.api.service;

import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.SleepLogResponseDto;
import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import com.ssafy.a802.jaljara.db.entity.SleepLog;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.entity.UserType;
import com.ssafy.a802.jaljara.db.repository.ChildInformationRepository;
import com.ssafy.a802.jaljara.db.repository.SleepLogRepository;
import com.ssafy.a802.jaljara.db.repository.UserRepository;
import com.ssafy.a802.jaljara.exception.CustomException;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SleepLogService {
    private final UserRepository userRepository;
    private final SleepLogRepository sleepLogRepository;
    private final ChildInformationRepository childInformationRepository;

    public void addSleepLog(SleepLogRequestDto.SleepLogInput sleepLogInput){
        long childId = sleepLogInput.getUserId();
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //해당 유저와 연결된 부모가 없는 경우
        ChildInformation childInformation = childInformationRepository.findByChildId(childId).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "부모와 연결되지 않은 유저입니다. userId: " + childId));

        //입력된 취침시간 ~ 기상시간 사이 구간 계산
        Duration duration = Duration.between(
                sleepLogInput.getBedTime().toLocalTime(),
                sleepLogInput.getWakeupTime().toLocalTime());

        //목표 취침시간 ~ 기상시간 사이 구간 계산
        Duration targetDuration = Duration.between(
                childInformation.getTargetBedTime().toLocalTime(),
                childInformation.getTargetWakeupTime().toLocalTime());

        //음수인 경우(기상시간 < 취침시간) 24시간을 더해 올바른 구간 구하기
        if(duration.isNegative())
            duration = duration.plusHours(24);
        if(targetDuration.isNegative())
            targetDuration = targetDuration.plusHours(24);

        //수면달성도 계산
        double sleepRate = (double)duration.getSeconds()/targetDuration.getSeconds();
        if(sleepRate > 1)
            sleepRate = 1.0;

        //수면기록 저장
        sleepLogRepository.save(SleepLog.builder()
                .userId(sleepLogInput.getUserId())
                .bedTime(sleepLogInput.getBedTime())
                .wakeupTime(sleepLogInput.getWakeupTime())
                .date(sleepLogInput.getDate())
                .sleepRate(sleepRate).build());
    }

    public List<Integer> findSleepLogByMonth(long childId, String date) throws ParseException {
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //입력된 달의 1일부터 31일 중 저장된 모든 수면기록 조회
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        List<SleepLog> sleepLogs =
                sleepLogRepository.findAllByUserIdAndDateBetweenOrderByDate(
                        childId,
                        formatter.parse(date.concat("01")),
                        formatter.parse(date.concat("31")));

        //수면기록이 저장된 날의 일(day)만 List에 담아 return
        List<Integer> daysExistsLog = new ArrayList<>();
        for(SleepLog sleepLog : sleepLogs)
            daysExistsLog.add(sleepLog.getDate().getDate());
        return daysExistsLog;
    }

    public SleepLogResponseDto.SleepLogDetail findSleepLogByDay(long childId, String date) throws ParseException {
        //childId에 해당하는 유저가 없는 경우
        User child = userRepository.findById(childId).orElseThrow(
                () -> ExceptionFactory.userNotFound(childId));

        //해당 유저가 자녀 유저가 아닌 경우
        if(child.getUserType() != UserType.CHILD)
            throw ExceptionFactory.userTypeMismatch(childId, UserType.CHILD);

        //해당 유저가 해당 날짜에 측정된 수면 기록이 없는 경우
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SleepLog sleepLog = sleepLogRepository.findByUserIdAndDate(childId, formatter.parse(date)).orElseThrow(
                () -> new CustomException(HttpStatus.NOT_FOUND, "해당 날짜에 측정된 수면 기록이 없습니다. userId: " + childId + ", date: " + date));

        return SleepLogResponseDto.SleepLogDetail.builder()
                .userId(childId)
                .bedTime(sleepLog.getBedTime())
                .wakeupTime(sleepLog.getWakeupTime())
                .date(sleepLog.getDate())
                .sleepRate(sleepLog.getSleepRate()).build();
    }
}
