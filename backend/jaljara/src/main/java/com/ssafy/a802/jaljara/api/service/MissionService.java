package com.ssafy.a802.jaljara.api.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ssafy.a802.jaljara.api.dto.response.MissionLogRequestDto;
import com.ssafy.a802.jaljara.api.dto.response.MissionTodayResponseDto;
import com.ssafy.a802.jaljara.config.S3Config;
import com.ssafy.a802.jaljara.db.entity.Mission;
import com.ssafy.a802.jaljara.db.entity.MissionAttachment;
import com.ssafy.a802.jaljara.db.entity.MissionLog;
import com.ssafy.a802.jaljara.db.entity.MissionToday;
import com.ssafy.a802.jaljara.db.entity.SleepLog;
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.repository.UserRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionAttachmentRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionLogRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionRepositoryImpl;
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
	private final S3Config s3Config;
	private final MissionRepositoryImpl missionRepositoryImpl;

	@Transactional
	public Mission getRandomMission() {
		return missionRepository.findRandomMission();
	}

	//create mission today
	// cron "초 분 시 일 월 년"
	@Scheduled(cron = "00 00 00 * * *", zone = "Asia/Seoul")
	@Transactional
	public void addMissionToday(Long userId) {

		User findUser = userRepository.findById(userId).orElseThrow(() ->
			new IllegalArgumentException("사용자의 아이디가 존재하지 않습니다.: " + userId));

		//get random mission
		Mission randomMission = getRandomMission();

		MissionToday missionToday = missionTodayRepository.findByUserId(userId).orElse(null);

		//today date init
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneOffset offset = ZoneOffset.of("+09:00"); // 예시로 한국 표준시(+9:00)를 사용합니다.
		OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, offset);
		Instant instant = offsetDateTime.toInstant();
		Date date = Date.from(instant);

		// if user mission today first
		if (Objects.isNull(missionToday)) {
			//just generate mission today
			missionTodayRepository.save(MissionToday.builder()
				.user(findUser)
				.mission(randomMission)
				.remainRerollCount(DEFAULT_REROLL_COUNT)
				.isClear(false)
				.missionDate(date)
				.build());

		} else {
			//move exist mission today to mission log
			MissionLog savedMissionLog = missionLogRepository.save(MissionLog.builder()
				.user(findUser)
				.content(missionToday.getMission().getContent())
				.isSuccess(missionToday.isClear())
				.missionDate(missionToday.getMissionDate())
				.build());

			//if missionToday isSuccessed true then save missionLog Attachement
			if (!Objects.isNull(missionToday.getUrl())) {
				missionAttachmentRepository.save(MissionAttachment.builder()
					.url(missionToday.getUrl())
					.missionLog(savedMissionLog)
					.missionType(missionToday.getMission().getMissionType())
					.build());
			}

			//remove exist mission today and generate mission today
			removeMissionToday(userId);

			missionTodayRepository.save(MissionToday.builder()
				.user(findUser)
				.mission(randomMission)
				.missionDate(date)
				.remainRerollCount(DEFAULT_REROLL_COUNT)
				.isClear(false)
				.build());
		}
	}

	//find mission today
	public MissionTodayResponseDto findMissionToday(Long userId) {

		userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId: " + userId));

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

	//today mission reroll
	@Transactional
	public void modifyMissionTodayReroll(Long userId) {

		userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId: " + userId));

		User findUser = userRepository.findById(userId).orElseThrow(() ->
			new IllegalArgumentException("사용자의 아이디가 존재하지 않습니다. userId: " + userId));

		//generate new random mission today
		Mission randomMission = getRandomMission();

		MissionToday findMissionToday = missionTodayRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 유저에게 오늘의 미션이 존재하지 않습니다. userId: " + userId));

		if (findMissionToday.getRemainRerollCount() == 0) {
			throw new IllegalArgumentException("더 이상 리롤을 진행할 수 없습니다. 남은 리롤 횟수: "
				+ findMissionToday.getRemainRerollCount());
		}

		if (findMissionToday.isClear()) {
			throw new IllegalArgumentException("이미 미션 수행을 완료하였습니다.");
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
	@Transactional
	public void modifyMissionTodayIsClear(Long userId) {

		userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId: " + userId));

		MissionToday missionToday = missionTodayRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저에게 오늘의 미션이 존재하지 않습니다. userId: " + userId));

		missionTodayRepository.save(missionToday.toBuilder()
			.id(missionToday.getId())
			.isClear(true)
			.build());
	}

	//delete mission today
	@Transactional
	public void removeMissionToday(Long userId) {
		userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId: " + userId));

		MissionToday missionToday = missionTodayRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저에게 오늘의 미션이 존재하지 않습니다. userId: " + userId));

		missionTodayRepository.delete(missionToday);
	}

	//perform a mission (S3 save logic)
	//s3 save -> db save
	@Transactional
	public void saveMissionTodayAttachment(Long userId, MultipartFile multipartFile) throws IOException {
		userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId: " + userId));

		MissionToday missionToday = missionTodayRepository.findByUserId(userId).orElseThrow(() ->
			new IllegalArgumentException("해당 유저에게 오늘의 미션이 존재하지 않습니다. userId: " + userId));

		//ex) https://jaljara.s3.ap-northeast-1.amazonaws.com/randomUUID

		String originalName = multipartFile.getOriginalFilename(); //filename
		long size = multipartFile.getSize(); // file size

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(multipartFile.getContentType());
		objectMetadata.setContentLength(size);

		AmazonS3Client amazonS3Client = s3Config.amazonS3Client();
		String bucketName = s3Config.getBucketName();

		String uploadPath = UUID.randomUUID().toString();

		amazonS3Client.putObject(
			new PutObjectRequest(bucketName, uploadPath, multipartFile.getInputStream(), objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);

		String s3Url = amazonS3Client.getUrl(bucketName, uploadPath).toString(); //available access url

		missionTodayRepository.save(missionToday.toBuilder()
			.id(missionToday.getId())
			.url(s3Url)
			.build());
	}

	//get user's mission log attachment that day
	public MissionLogRequestDto findMissionLogWithMissionAttachment(Long userId, String missionDate) throws
		ParseException {
		userRepository.findById(userId).orElseThrow(
			() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다. userId: " + userId));

		//String to Date
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return missionRepositoryImpl.findMissionLogWithMissionAttachment(userId, formatter.parse(missionDate));
	}
}