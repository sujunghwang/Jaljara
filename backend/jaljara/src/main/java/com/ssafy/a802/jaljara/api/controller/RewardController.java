package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.response.RewardResponseDto;
import com.ssafy.a802.jaljara.api.service.RewardService;
import com.ssafy.a802.jaljara.common.annotation.ValidChildIdParameter;
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
    @ValidChildIdParameter
    public ResponseEntity<?> addReward(@PathVariable long userId){
        rewardService.addReward(userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @ValidChildIdParameter
    public ResponseEntity<?> getUsableRewardList(@PathVariable long userId){
        List<RewardResponseDto.RewardUsable> rewards = rewardService.findUsableRewardListByUserId(userId);
        if(rewards.size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(rewards, HttpStatus.OK);
    }

    @GetMapping("/used/{userId}")
    @ValidChildIdParameter
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
