package com.ssafy.a802.jaljara.api.service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.a802.jaljara.api.dto.request.MissionLogSaveRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.MissionTodayResponseDto;
import com.ssafy.a802.jaljara.db.entity.Mission;
import com.ssafy.a802.jaljara.db.entity.MissionLog;
import com.ssafy.a802.jaljara.db.entity.MissionToday;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.repository.UserRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionAttachmentRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionLogRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionTodayRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MissionService {
	private final static int DEFAULT_REROLL_COUNT = 2;

	private final MissionRepository missionRepository;
	private final MissionTodayRepository missionTodayRepository;
	private final MissionLogRepository missionLogRepository;
	private final MissionAttachmentRepository missionAttachmentRepository;
	private final UserRepository userRepository;

	public Mission getRandomMission() {
		return missionRepository.findRandomMission();
	}

	//create mission today
	public void generateMissionToday(Long userId) {
		User findUser = userRepository.findById(userId).orElseThrow(() ->
			new IllegalArgumentException("사용자의 아이디가 존재하지 않습니다.: " + userId));

		//get random mission
		Mission randomMission = getRandomMission();

		MissionToday missionToday = missionTodayRepository.findByUserId(userId).orElse(null);

		// if user mission today first
		if (Objects.isNull(missionToday)) {
			//just generate mission today
			saveMissionToday(findUser, randomMission);

		} else {
			//move exist mission today to mission log
			missionLogRepository.save(MissionLog.builder()
				.user(findUser)
				.content(missionToday.getMission().getContent())
				.isSuccess(missionToday.isClear())
				.date(missionToday.getCreatedAt())
				.build());

			//remove exist mission today and generate mission today
			missionTodayRepository.delete(missionToday);
			saveMissionToday(findUser, randomMission);
		}
	}

	//save mission today
	private void saveMissionToday(User findUser, Mission randomMission) {
		missionTodayRepository.save(MissionToday.builder()
			.user(findUser)
			.mission(randomMission)
			.remainRerollCount(DEFAULT_REROLL_COUNT)
			.isClear(false)
			.build());
	}

	//today mission reroll
	public void rerollMissionToday(Long userId) {

		User findUser = userRepository.findById(userId).orElseThrow(() ->
			new IllegalArgumentException("사용자의 아이디가 존재하지 않습니다. userId: " + userId));

		//generate new random mission today
		Mission randomMission = getRandomMission();

		MissionToday findMissionToday = missionTodayRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 유저에게 오늘의 미션이 존재하지 않습니다. userId: " + userId));

		if (findMissionToday.getRemainRerollCount() == 0) {
			new IllegalArgumentException("더 이상 리롤을 진행할 수 없습니다. 남은 리롤 횟수: "
				+ findMissionToday.getRemainRerollCount());
		}

		//reroll count --
		findMissionToday.reroll();

		missionTodayRepository.save(findMissionToday.toBuilder()
			.id(findMissionToday.getId())
			.mission(randomMission)
			.build()
		);
	}

	//complete mission today (parents okay sign)
	public void updateMissionTodayIsClear(Long userId) {
		MissionToday missionToday = missionTodayRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저에게 오늘의 미션이 존재하지 않습니다. userId: " + userId));

		missionTodayRepository.save(missionToday.toBuilder()
			.id(missionToday.getId())
			.isClear(true)
			.build());
	}

	//get mission today
	public MissionTodayResponseDto findMissionToday(Long userId) {
		MissionToday missionToday = missionTodayRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저에게 오늘의 미션이 존재하지 않습니다. userId: " + userId));

		return MissionTodayResponseDto.builder()
			.missionTodayId(missionToday.getId())
			.missionId(missionToday.getMission().getId())
			.userId(missionToday.getUser().getId())
			.remainRerollCount(missionToday.getRemainRerollCount())
			.isClear(missionToday.isClear())
			.url(missionToday.getUrl())
			.build();
	}

	//perform a mission (S3 save logic)

	//get user's mission log
}
