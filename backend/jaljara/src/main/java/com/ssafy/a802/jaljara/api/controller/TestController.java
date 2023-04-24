package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.api.service.SleepLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final SleepLogService sleepLogService;
    @PostMapping()
    public ResponseEntity<?> addSleepLog(@RequestBody SleepLogRequestDto.SleepLogInput sleepLogInput){
        Map<String, Object> resultMap = new HashMap<>();
        int res = sleepLogService.addSleepLog(sleepLogInput);
        resultMap.put("res", res);
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
    @PostMapping("/{userId}/{date}")
    public ResponseEntity<?> test(@PathVariable long userId, @PathVariable String date){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("res", sleepLogService.findSleepLogByMonth(userId, date));
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

//    @PostMapping("/{userId}/{date}")
//    public ResponseEntity<?> addSleepLog(@PathVariable long userId, @PathVariable String date){
//        Map<String, Object> resultMap = new HashMap<>();
//        resultMap.put("res", sleepLogService.findSleepLogByDay(userId, date));
//        return new ResponseEntity<>(resultMap, HttpStatus.OK);
//    }
}
