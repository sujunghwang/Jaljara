package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.response.RewardResponseDto;
import com.ssafy.a802.jaljara.api.service.RewardService;
import com.ssafy.a802.jaljara.common.annotation.ValidChild;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {
    private final RewardService rewardService;

    @PostMapping("/{userId}")
    @ValidChild
    public ResponseEntity<?> addReward(@PathVariable long userId){
        rewardService.addReward(userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @ValidChild
    public ResponseEntity<?> getUsableRewardList(@PathVariable long userId){
        List<RewardResponseDto.RewardUsable> rewards = rewardService.findUsableRewardListByUserId(userId);
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @GetMapping("/used/{userId}")
    @ValidChild
    public ResponseEntity<?> getUsedRewardList(@PathVariable long userId){
        List<RewardResponseDto.RewardUsed> rewards = rewardService.findUsedRewardListByUserId(userId);
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @PutMapping("/use/{rewardId}")
    public ResponseEntity<?> modifyRewardUse(@PathVariable long rewardId) {
        rewardService.modifyRewardUsed(rewardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
