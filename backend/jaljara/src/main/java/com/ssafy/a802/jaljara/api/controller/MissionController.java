package com.ssafy.a802.jaljara.api.controller;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.ssafy.a802.jaljara.common.annotation.ValidChild;
import com.ssafy.a802.jaljara.api.service.MissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

	private final MissionService missionService;

	//오늘의 미션 조회
	@GetMapping("/{userId}")
	@ValidChild
	public ResponseEntity<?> getMissionToday(@PathVariable long userId) {
		MissionTodayResponseDto missionToday = missionService.findMissionToday(userId);
		return new ResponseEntity<>(missionToday, HttpStatus.OK);
	}

	//미션 리롤
	@GetMapping("/{userId}/reroll")
	@ValidChild
	public ResponseEntity<?> rerollMissionToday(@PathVariable long userId) {
		missionService.modifyMissionTodayReroll(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//미션 수행
	@PostMapping("/attachment/{userId}")
	@ValidChild
	public ResponseEntity<?> uploadMissionTodayAttachment(@PathVariable long userId,
		@RequestPart("file") MultipartFile file) throws
		IOException {
		missionService.addMissionTodayAttachment(userId, file);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//미션 승인
	@PutMapping("/{userId}/clear")
	@ValidChild
	public ResponseEntity<?> clearMissionToday(@PathVariable long userId) {
		missionService.modifyMissionTodayIsClear(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//해당 날짜 미션 기록 조회
	@GetMapping("/{userId}/{date}")
	@ValidChild
	public ResponseEntity<?> getMissionLogWithThatDay(@PathVariable long userId, @PathVariable String date) throws
		ParseException {
		MissionLogRequestDto missionLogWithDate =
			missionService.findMissionLogWithMissionAttachment(userId, date);
		return new ResponseEntity<>(missionLogWithDate, HttpStatus.OK);
	}

	//미션 생성 - 테스트용 실제로는 스케쥴러로 돌아감
	@PostMapping("/test/generateMissionToday/{userId}")
	@ValidChild
	public void generateMissionToday(@PathVariable long userId) {
		missionService.addMissionToday(userId);
	}
}
