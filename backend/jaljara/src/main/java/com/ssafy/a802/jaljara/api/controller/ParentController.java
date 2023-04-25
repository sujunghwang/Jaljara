package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.ChildInformationRequestDto;
import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.ChildInformationResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.SleepLogResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto;
import com.ssafy.a802.jaljara.api.service.ChildInformationService;
import com.ssafy.a802.jaljara.api.service.RewardService;
import com.ssafy.a802.jaljara.api.service.SleepLogService;
import com.ssafy.a802.jaljara.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
public class ParentController {
//    private final UserService userService;
    private final SleepLogService sleepLogService;
    private final ChildInformationService childInformationService;
    private final RewardService rewardService;

    @GetMapping("/childinfos/{parentId}")
    public ResponseEntity<?> getChildList(@PathVariable long parentId){
        List<UserResponseDto.SimpleUserInfo> childList = childInformationService.findChildListByParentId(parentId);
        return new ResponseEntity<>(new ApiResponse<>(childList), HttpStatus.OK);
    }

    @GetMapping("/childinfos/detail/{childId}")
    public ResponseEntity<?> getChildDetail(@PathVariable long childId){
        ChildInformationResponseDto.ChildInfoDetail childDetail = childInformationService.findChildInformationByChildId(childId);
        return new ResponseEntity<>(new ApiResponse<>(childDetail), HttpStatus.OK);
    }

    @PutMapping("/childinfos/reward")
    public ResponseEntity<?> modifyCurrentReward(@RequestBody ChildInformationRequestDto.CurrentRewardInput input){
        childInformationService.modifyCurrentReward(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/childinfos/sleep")
    public ResponseEntity<?> modifyTargetSleep(@RequestBody ChildInformationRequestDto.TargetSleepInput input){
        childInformationService.modifyTargetSleepTime(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/childinfos/{childId}")
    public ResponseEntity<?> removeChild(@PathVariable long childId){
        childInformationService.removeChildInformationByChildId(childId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/sleeplogs/{childId}/{date}/simple")
    public ResponseEntity<?> getDayHasLogList(@PathVariable long childId, @PathVariable String date) throws ParseException {
        List<Integer> dayHasLogList = sleepLogService.findSleepLogByMonth(childId, date);
        return new ResponseEntity<>(new ApiResponse<>(dayHasLogList), HttpStatus.OK);
    }

    @GetMapping("/sleeplogs/{childId}/{date}")
    public ResponseEntity<?> getSleepLogDetail(@PathVariable long childId, @PathVariable String date) throws ParseException {
        SleepLogResponseDto.SleepLogDetail sleepLogDetail = sleepLogService.findSleepLogByDay(childId, date);
        return new ResponseEntity<>(new ApiResponse<>(sleepLogDetail), HttpStatus.OK);
    }

    @GetMapping("/sleeplogs/{childId}")
    public ResponseEntity<?> getSleepLogDetailToday(@PathVariable long childId) throws ParseException {
        SleepLogResponseDto.SleepLogDetail sleepLogDetail = sleepLogService.findSleepLogByDay(childId, LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        return new ResponseEntity<>(new ApiResponse<>(sleepLogDetail), HttpStatus.OK);
    }

    @PostMapping("/sleeplogs")
    public ResponseEntity<?> addSleepLog(@RequestBody SleepLogRequestDto.SleepLogInput sleepLogInput){
        sleepLogService.addSleepLog(sleepLogInput);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
