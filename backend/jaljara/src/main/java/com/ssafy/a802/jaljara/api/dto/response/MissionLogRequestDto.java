package com.ssafy.a802.jaljara.api.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.a802.jaljara.db.entity.MissionType;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MissionLogRequestDto {

	private Long missionLogId;
	private Long userId;
	@JsonProperty
	private boolean isSuccess;
	private Date missionDate;
	private MissionType missionType;
	private String content;
	private String url;

	public MissionLogRequestDto(Long missionLogId, Long userId, boolean isSuccess, Date missionDate,
		MissionType missionType, String content, String url) {
		this.missionLogId = missionLogId;
		this.userId = userId;
		this.isSuccess = isSuccess;
		this.missionDate = missionDate;
		this.missionType = missionType;
		this.content = content;
		this.url = url;
	}
}
