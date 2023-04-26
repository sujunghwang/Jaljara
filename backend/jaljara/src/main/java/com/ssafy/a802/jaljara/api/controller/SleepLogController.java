package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.SleepLogResponseDto;
import com.ssafy.a802.jaljara.api.service.SleepLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/sleeplogs")
@RequiredArgsConstructor
public class SleepLogController {
    private final SleepLogService sleepLogService;

    @PostMapping()
    public ResponseEntity<?> addSleepLog(@RequestBody SleepLogRequestDto.SleepLogInput sleepLogInput){
        sleepLogService.addSleepLog(sleepLogInput);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{childId}/{date}/simple")
    public ResponseEntity<?> getDayHasLogList(@PathVariable long childId, @PathVariable String date) throws ParseException {
        List<Integer> dayHasLogList = sleepLogService.findSleepLogByMonth(childId, date);
        if(dayHasLogList.size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(dayHasLogList, HttpStatus.OK);
    }

    @GetMapping("/{childId}/{date}")
    public ResponseEntity<?> getSleepLogDetail(@PathVariable long childId, @PathVariable String date) throws ParseException {
        SleepLogResponseDto.SleepLogDetail sleepLogDetail = sleepLogService.findSleepLogByDay(childId, date);
        return new ResponseEntity<>(sleepLogDetail, HttpStatus.OK);
    }

    @GetMapping("/{childId}")
    public ResponseEntity<?> getSleepLogDetailToday(@PathVariable long childId) throws ParseException {
        //현재 시간 기준 어제의 수면 기록 조회
        SleepLogResponseDto.SleepLogDetail sleepLogDetail =
                sleepLogService.findSleepLogByDay(childId,
                        LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return new ResponseEntity<>(sleepLogDetail, HttpStatus.OK);
    }
}
