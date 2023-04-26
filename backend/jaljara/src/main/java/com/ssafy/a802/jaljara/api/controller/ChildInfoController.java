package com.ssafy.a802.jaljara.api.controller;

import com.ssafy.a802.jaljara.api.dto.request.ChildInformationRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.ChildInformationResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.UserResponseDto;
import com.ssafy.a802.jaljara.api.service.ChildInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/childinfos")
@RequiredArgsConstructor
public class ChildInfoController {
    private final ChildInformationService childInformationService;

    @PostMapping("/connect/{parentId}/{childId}")
    public ResponseEntity<?> addChildInformation(@PathVariable long parentId, @PathVariable long childId){
        childInformationService.addChildInformation(parentId, childId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{parentId}")
    public ResponseEntity<?> getChildList(@PathVariable long parentId){
        List<UserResponseDto.SimpleUserInfo> childList = childInformationService.findChildListByParentId(parentId);
        if(childList.size() == 0)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(childList, HttpStatus.OK);
    }

    @GetMapping("/detail/{childId}")
    public ResponseEntity<?> getChildDetail(@PathVariable long childId){
        ChildInformationResponseDto.ChildInfoDetail childDetail = childInformationService.findChildInformationByChildId(childId);
        return new ResponseEntity<>(childDetail, HttpStatus.OK);
    }

    @PutMapping("/reward")
    public ResponseEntity<?> modifyCurrentReward(@RequestBody ChildInformationRequestDto.CurrentRewardInput input){
        childInformationService.modifyCurrentReward(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/sleep")
    public ResponseEntity<?> modifyTargetSleep(@RequestBody ChildInformationRequestDto.TargetSleepInput input){
        childInformationService.modifyTargetSleepTime(input);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/streakCnt/{childId}")
    public ResponseEntity<?> modifyStreakCnt(@PathVariable long childId){
        childInformationService.modifyStreakCntPlus(childId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<?> removeChild(@PathVariable long childId){
        childInformationService.removeChildInformationByChildId(childId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
