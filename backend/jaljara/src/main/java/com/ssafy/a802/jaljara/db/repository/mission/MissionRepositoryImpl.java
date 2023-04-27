package com.ssafy.a802.jaljara.db.repository.mission;

import java.util.Date;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.ssafy.a802.jaljara.api.dto.response.MissionLogRequestDto;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MissionRepositoryImpl {

	private final EntityManager em;

	public MissionLogRequestDto findMissionLogWithMissionAttachment(Long userId, Date missionDate) {
		return em.createQuery(
			"select new com.ssafy.a802.jaljara.api.dto.response.MissionLogRequestDto"
				+ "(ml.id, ml.user.id, ml.isSuccess, ml.missionDate, ma.missionType, ml.content, ma.url)"
				+ " from MissionLog ml"
				+ " join MissionAttachment ma"
				+ " on ml.id = ma.missionLog.id"
				+ " where ml.user.id =:userId"
				+ " and ml.missionDate =:missionDate", MissionLogRequestDto.class)
			.setParameter("userId", userId)
			.setParameter("missionDate", missionDate)
			.getSingleResult();
	}
}
