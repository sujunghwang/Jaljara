package com.ssafy.a802.jaljara.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.a802.jaljara.db.entity.MissionToday;
import lombok.Builder;
import lombok.Data;

@Data
public class MissionTodayResponseDto {

	private Long missionTodayId;
	private Long userId;
	private Long missionId;
	@JsonProperty("isClear")
	private boolean isClear;
	private String content;
	private String missionType;
	private int remainRerollCount;
	private String url;

	@Builder
	public MissionTodayResponseDto(Long missionTodayId, Long userId, Long missionId, boolean isClear,
		String content, String missionType, int remainRerollCount,
		String url) {
		this.missionTodayId = missionTodayId;
		this.userId = userId;
		this.missionId = missionId;
		this.isClear = isClear;
		this.content = missionType;
		this.missionType = missionType;
		this.remainRerollCount = remainRerollCount;
		this.url = url;
	}

	public MissionTodayResponseDto(MissionToday missionToday) {
		this.missionTodayId = missionToday.getId();
		this.userId = missionToday.getUser().getId();
		this.missionId = missionToday.getMission().getId();
		this.isClear = missionToday.isClear();
		this.content = missionToday.getMission().getContent();
		this.missionType = missionToday.getMission().getMissionType().toString();
		this.remainRerollCount = missionToday.getRemainRerollCount();
		this.url = missionToday.getUrl();
	}
}
