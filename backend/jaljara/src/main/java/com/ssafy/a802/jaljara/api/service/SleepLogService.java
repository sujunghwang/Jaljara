package com.ssafy.a802.jaljara.api.service;

import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.SleepLogResponseDto;
import com.ssafy.a802.jaljara.db.entity.ChildInformation;
import com.ssafy.a802.jaljara.db.entity.SleepLog;
import com.ssafy.a802.jaljara.db.repository.ChildInformationRepository;
import com.ssafy.a802.jaljara.db.repository.SleepLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SleepLogService {
    private final SleepLogRepository sleepLogRepository;
    private final ChildInformationRepository childInformationRepository;

    public int addSleepLog(SleepLogRequestDto.SleepLogInput sleepLogInput){
        ChildInformation childInformation = childInformationRepository.findByChildId(sleepLogInput.getUserId());
        Duration duration = Duration.between(sleepLogInput.getBedTime().toLocalTime(), sleepLogInput.getWakeupTime().toLocalTime());
        Duration targetDuration = Duration.between(childInformation.getTargetBedTime().toLocalTime(), childInformation.getTargetWakeupTime().toLocalTime());
        if(duration.isNegative())
            duration = duration.plusHours(24);
        if(targetDuration.isNegative())
            targetDuration = targetDuration.plusHours(24);

        double sleepRate = (double)duration.getSeconds()/targetDuration.getSeconds();
        sleepLogRepository.save(SleepLog.builder()
                .userId(sleepLogInput.getUserId())
                .bedTime(sleepLogInput.getBedTime())
                .wakeupTime(sleepLogInput.getWakeupTime())
                .date(sleepLogInput.getDate())
                .sleepRate(sleepRate).build());
        return 1;
    }

    public SleepLogResponseDto.SleepLogDetail findSleepLogByDay(long userId, String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        SleepLog sleepLog = sleepLogRepository.findByUserIdAndDate(userId, formatter.parse(date));
        if(sleepLog == null)
            return null;

        return SleepLogResponseDto.SleepLogDetail.builder()
                .userId(userId)
                .bedTime(sleepLog.getBedTime())
                .wakeupTime(sleepLog.getWakeupTime())
                .date(sleepLog.getDate())
                .sleepRate(sleepLog.getSleepRate()).build();
    }

    public List<Integer> findSleepLogByMonth(long userId, String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        List<SleepLog> sleepLogs = sleepLogRepository.findAllByUserIdAndDateBetweenOrderByDate(userId, formatter.parse(date.concat("01")), formatter.parse(date.concat("31")));
        List<Integer> daysExistsLog = new ArrayList<>();
        for(SleepLog sleepLog : sleepLogs)
            daysExistsLog.add(sleepLog.getDate().getDate());
        return daysExistsLog;
    }
}
