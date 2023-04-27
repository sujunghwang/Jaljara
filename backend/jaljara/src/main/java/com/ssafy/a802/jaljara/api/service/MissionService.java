package com.ssafy.a802.jaljara.api.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.http.HttpStatus;
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
import com.ssafy.a802.jaljara.db.entity.User;
import com.ssafy.a802.jaljara.db.entity.UserType;
import com.ssafy.a802.jaljara.db.repository.UserRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionAttachmentRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionLogRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionRepository;
import com.ssafy.a802.jaljara.db.repository.mission.MissionRepositoryImpl;
import com.ssafy.a802.jaljara.db.repository.mission.MissionTodayRepository;
import com.ssafy.a802.jaljara.exception.CustomException;
import com.ssafy.a802.jaljara.exception.ExceptionFactory;

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

	// cron "초 분 시 일 월 년"
	@Transactional
	@Scheduled(cron = "00 00 00 * * *", zone = "Asia/Seoul")
	public void addMissionTodayChildren() {
		List<User> allByUserType = userRepository.findAllByUserType(UserType.CHILD);
		for (User user : allByUserType) {
			addMissionToday(user.getId());
		}
	}

	//create mission today
	@Transactional
	public void addMissionToday(long userId) {
		User findUser = userRepository.findById(userId).orElseThrow(() ->
			ExceptionFactory.userNotFound(userId));

		//get random mission
		Mission randomMission = getRandomMission();

		MissionToday findMissionToday = missionTodayRepository.findByUserId(userId).orElse(null);

		//today date init
		LocalDateTime localDateTime = LocalDateTime.now();
		ZoneOffset offset = ZoneOffset.of("+09:00"); // 예시로 한국 표준시(+9:00)를 사용합니다.
		OffsetDateTime offsetDateTime = OffsetDateTime.of(localDateTime, offset);
		Instant instant = offsetDateTime.toInstant();
		Date date = Date.from(instant);

		// if user mission today first
		if (Objects.isNull(findMissionToday)) {
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
				.content(findMissionToday.getMission().getContent())
				.isSuccess(findMissionToday.isClear())
				.missionDate(findMissionToday.getMissionDate())
				.build());

			//if missionToday isSuccessed true then save missionLog Attachement
			if (!Objects.isNull(findMissionToday.getUrl())) {
				missionAttachmentRepository.save(MissionAttachment.builder()
					.url(findMissionToday.getUrl())
					.missionLog(savedMissionLog)
					.missionType(findMissionToday.getMission().getMissionType())
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
	public MissionTodayResponseDto findMissionToday(long userId) {

		MissionToday findMissionToday = missionTodayRepository.findByUserId(userId).orElseThrow(() ->
			ExceptionFactory.userMissionTodayNotFound(userId));

		return MissionTodayResponseDto.builder()
			.missionTodayId(findMissionToday.getId())
			.missionId(findMissionToday.getMission().getId())
			.userId(findMissionToday.getUser().getId())
			.remainRerollCount(findMissionToday.getRemainRerollCount())
			.isClear(findMissionToday.isClear())
			.url(findMissionToday.getUrl())
			.build();
	}

	//today mission reroll
	@Transactional
	public void modifyMissionTodayReroll(long userId) {

		//generate new random mission today
		Mission randomMission = getRandomMission();

		MissionToday findMissionToday = missionTodayRepository.findByUserId(userId).orElseThrow(() ->
			ExceptionFactory.userMissionTodayNotFound(userId));

		if (findMissionToday.getRemainRerollCount() == 0) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "더 이상 리롤을 진행할 수 없습니다. 남은 리롤 횟수: "
				+ findMissionToday.getRemainRerollCount());
		}

		if (findMissionToday.isClear()) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "이미 미션 수행을 완료하였습니다.");
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
	public void modifyMissionTodayIsClear(long userId) {

		MissionToday findMissionToday = missionTodayRepository.findByUserId(userId).orElseThrow(() ->
			ExceptionFactory.userMissionTodayNotFound(userId));

		missionTodayRepository.save(findMissionToday.toBuilder()
			.id(findMissionToday.getId())
			.isClear(true)
			.build());
	}

	//delete mission today
	@Transactional
	public void removeMissionToday(long userId) {

		MissionToday findMissionToday = missionTodayRepository.findByUserId(userId).orElseThrow(() ->
			ExceptionFactory.userMissionTodayNotFound(userId));

		missionTodayRepository.delete(findMissionToday);
	}

	//perform a mission (S3 save logic)
	//s3 save -> db save
	@Transactional
	public void addMissionTodayAttachment(long userId, MultipartFile multipartFile) throws IOException {

		MissionToday findMissionToday = missionTodayRepository.findByUserId(userId).orElseThrow(() ->
			ExceptionFactory.userMissionTodayNotFound(userId));

		//ex) https://jaljara.s3.ap-northeast-1.amazonaws.com/randomUUID

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

		missionTodayRepository.save(findMissionToday.toBuilder()
			.id(findMissionToday.getId())
			.url(s3Url)
			.build());
	}

	//get user's mission log attachment that day
	public MissionLogRequestDto findMissionLogWithMissionAttachment(long userId, String missionDate) throws
		ParseException {

		//String to Date
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		return missionRepositoryImpl.findMissionLogWithMissionAttachment(userId, formatter.parse(missionDate));
	}
}