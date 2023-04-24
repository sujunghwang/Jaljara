package com.ssafy.a802.jaljara.api.dto.response;

import com.ssafy.a802.jaljara.db.entity.MissionToday;

import lombok.Builder;
import lombok.Data;

@Data
public class MissionTodayResponseDto {

	private Long missionTodayId;
	private Long userId;
	private Long missionId;

	private boolean isClear;
	private int remainRerollCount;
	private String url;

	@Builder
	public MissionTodayResponseDto(Long missionTodayId, Long userId, Long missionId, boolean isClear,
		int remainRerollCount,
		String url) {
		this.missionTodayId = missionTodayId;
		this.userId = userId;
		this.missionId = missionId;
		this.isClear = isClear;
		this.remainRerollCount = remainRerollCount;
		this.url = url;
	}

	public MissionTodayResponseDto(MissionToday missionToday) {
		this.missionTodayId = missionToday.getId();
		this.userId = missionToday.getUser().getId();
		this.missionId = missionToday.getMission().getId();
		this.isClear = missionToday.isClear();
		this.remainRerollCount = missionToday.getRemainRerollCount();
		this.url = missionToday.getUrl();
	}
}
