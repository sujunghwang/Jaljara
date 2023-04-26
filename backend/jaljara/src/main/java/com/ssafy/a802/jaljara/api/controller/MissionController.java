package com.ssafy.a802.jaljara.api.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.a802.jaljara.api.dto.response.MissionLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.MissionTodayResponseDto;
import com.ssafy.a802.jaljara.api.service.MissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

	private final MissionService missionService;

	//오늘의 미션 조회
	@GetMapping("/{userId}")
	public MissionTodayResponseDto getMissionToday(@PathVariable Long userId) {
		return missionService.findMissionToday(userId);
	}

	//미션 리롤
	@GetMapping("/{userId}/reroll")
	public void rerellMissionToday(@PathVariable Long userId) {
		missionService.modifyMissionTodayReroll(userId);
	}

	//미션 수행
	@PostMapping("/attatchment/{userId}")
	public void uploadMissionTodayAttachment(@PathVariable Long userId, @RequestPart("file") MultipartFile file) throws
		IOException {
		missionService.saveMissionTodayAttachment(userId, file);
	}

	//미션 승인
	@PutMapping("/{userId}/clear")
	public void clearMissionToday(@PathVariable Long userId) {
		missionService.modifyMissionTodayIsClear(userId);
	}

	//해당 날짜 미션 기록 조회
	@GetMapping("/parent/missions/{userId}/{date}")
	public MissionLogRequestDto getMissionLogWithThatDay(@PathVariable Long userId, @PathVariable String date) throws
		ParseException {
		return missionService.findMissionLogWithMissionAttachment(userId, date);
	}

	//미션 생성 - 테스트용 실제로는 스케쥴러로 돌아감
	@PostMapping("/test/generateMissionToday/{userId}")
	public void generateMissionToday(@PathVariable Long userId) {
		missionService.addMissionToday(userId);
	}
}
