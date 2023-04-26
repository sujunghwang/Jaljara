package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.ChildInformationRequestDto;
import com.ssafy.a802.jaljara.api.dto.request.SleepLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.ChildInformationResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.RewardResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.SleepLogResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto;
import com.ssafy.a802.jaljara.api.service.ChildInformationService;
import com.ssafy.a802.jaljara.api.service.RewardService;
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
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> addReward(@PathVariable long userId){
        rewardService.addReward(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUsableRewardList(@PathVariable long userId){
        List<RewardResponseDto.RewardUsable> rewards = rewardService.findUsableRewardListByUserId(userId);
        if(rewards.size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @GetMapping("/used/{userId}")
    public ResponseEntity<?> getUsedRewardList(@PathVariable long userId){
        List<RewardResponseDto.RewardUsed> rewards = rewardService.findUsedRewardListByUserId(userId);
        if(rewards.size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @PutMapping("/use/{rewardId}")
    public ResponseEntity<?> modifyRewardUse(@PathVariable long rewardId) {
        rewardService.modifyRewardUsed(rewardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
