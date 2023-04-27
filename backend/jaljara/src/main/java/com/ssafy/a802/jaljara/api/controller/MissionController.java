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
import com.ssafy.a802.jaljara.api.service.MissionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/missions")
@RequiredArgsConstructor
public class MissionController {

	private final MissionService missionService;

	//오늘의 미션 조회
	@GetMapping("/{userId}")
	public ResponseEntity<?> getMissionToday(@PathVariable Long userId) {
		MissionTodayResponseDto missionToday = missionService.findMissionToday(userId);
		return new ResponseEntity<>(missionToday, HttpStatus.OK);
	}

	//미션 리롤
	@GetMapping("/{userId}/reroll")
	public ResponseEntity<?> rerollMissionToday(@PathVariable Long userId) {
		missionService.modifyMissionTodayReroll(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//미션 수행
	@PostMapping("/attatchment/{userId}")
	public ResponseEntity<?> uploadMissionTodayAttachment(@PathVariable Long userId,
		@RequestPart("file") MultipartFile file) throws
		IOException {
		missionService.saveMissionTodayAttachment(userId, file);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//미션 승인
	@PutMapping("/{userId}/clear")
	public ResponseEntity<?> clearMissionToday(@PathVariable Long userId) {
		missionService.modifyMissionTodayIsClear(userId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	//해당 날짜 미션 기록 조회
	@GetMapping("/parent/missions/{userId}/{date}")
	public ResponseEntity<?> getMissionLogWithThatDay(@PathVariable Long userId, @PathVariable String date) throws
		ParseException {
		MissionLogRequestDto missionLogWithDate =
			missionService.findMissionLogWithMissionAttachment(userId, date);
		return new ResponseEntity<>(missionLogWithDate, HttpStatus.OK);
	}

	//미션 생성 - 테스트용 실제로는 스케쥴러로 돌아감
	@PostMapping("/test/generateMissionToday/{userId}")
	public void generateMissionToday(@PathVariable Long userId) {
		missionService.addMissionToday(userId);
	}
}
