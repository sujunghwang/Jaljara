package com.ssafy.a802.jaljara.api.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.a802.jaljara.api.dto.response.MissionLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.MissionTodayResponseDto;
import com.ssafy.a802.jaljara.api.service.MissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class MissionTestController {

	private final MissionService missionService;

	//미션 생성
	@PostMapping("/child/missions/today/{userId}")
	public void generateMissionToday(@PathVariable Long userId) {
		missionService.addMissionToday(userId);
	}

	//미션 리롤
	@PostMapping("/child/missions/{userId}/reroll")
	public void rerellMissionToday(@PathVariable Long userId) {
		missionService.modifyMissionTodayReroll(userId);
	}

	//오늘의 미션 조회
	@PostMapping("/child/missions/{userId}")
	public MissionTodayResponseDto getMissionToday(@PathVariable Long userId) {
		return missionService.findMissionToday(userId);
	}

	//미션 승인
	@PostMapping("/parent/missions/{userId}/clear")
	public void clearMissionToday(@PathVariable Long userId) {
		missionService.modifyMissionTodayIsClear(userId);
	}

	@PostMapping("/child/missions/attatchment/{userId}")
	public void uploadMissionTodayAttachment(@PathVariable Long userId, @RequestPart("file") MultipartFile file) throws
		IOException {
		missionService.saveMissionTodayAttachment(userId, file);
	}

	@PostMapping("/parent/missions/{userId}/{date}")
	public MissionLogRequestDto getMissionLogWithThatDay(@PathVariable Long userId, @PathVariable String date) throws
		ParseException {
		return missionService.findMissionLogWithMissionAttachment(userId, date);
	}
}
