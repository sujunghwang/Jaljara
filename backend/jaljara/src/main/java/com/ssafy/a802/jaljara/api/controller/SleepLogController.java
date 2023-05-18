package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.SleepLogResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto;
import com.ssafy.a802.jaljara.api.service.SleepLogService;
import com.ssafy.a802.jaljara.common.annotation.ValidChild;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/sleeplogs")
@RequiredArgsConstructor
@Slf4j
public class SleepLogController {
    private final SleepLogService sleepLogService;

    @PostMapping()
    @ValidChild
    public ResponseEntity<?> addSleepLogs(@RequestBody List<SleepLogRequestDto.SleepSegmentEvent> sleepSegmentEvents){
        UserResponseDto.SimpleUserInfo userInfo = (UserResponseDto.SimpleUserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("슬립로그 컨트롤러 : 유저 몇번 이니? : " +userInfo.getUserId());
        log.info("슬립로그 컨트롤러 : 슬립 로그 : " + sleepSegmentEvents.toString());

        sleepLogService.addSleepLogs(sleepSegmentEvents, userInfo.getUserId());
        return ResponseEntity.ok("Sleep log created");
    }

    @GetMapping("/{childId}/{date}/simple")
    @ValidChild
    public ResponseEntity<?> getMissionCompleteDayList(@PathVariable long childId, @PathVariable String date) throws ParseException {
        List<Integer> missionCompleteDayList = sleepLogService.findMissionCompleteDayListByMonth(childId, date);
        return new ResponseEntity<>(missionCompleteDayList, HttpStatus.OK);
    }

    @GetMapping("/{childId}/{date}")
    @ValidChild
    public ResponseEntity<?> getSleepLogDetail(@PathVariable long childId, @PathVariable String date) throws ParseException {
        SleepLogResponseDto.SleepLogDetail sleepLogDetail = sleepLogService.findSleepLogByDay(childId, date);
        return new ResponseEntity<>(sleepLogDetail, HttpStatus.OK);
    }

    @GetMapping("/{childId}")
    @ValidChild
    public ResponseEntity<?> getSleepLogDetailToday(@PathVariable long childId) throws ParseException {
        //현재 시간 기준 어제의 수면 기록 조회
        SleepLogResponseDto.SleepLogDetail sleepLogDetail =
                sleepLogService.findSleepLogByDay(childId,
                        LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return new ResponseEntity<>(sleepLogDetail, HttpStatus.OK);
    }
}
