package com.ssafy.a802.jaljara.api.dto.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import com.ssafy.a802.jaljara.db.entity.MissionLog;
import com.ssafy.a802.jaljara.db.entity.MissionToday;
import com.ssafy.a802.jaljara.db.entity.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class MissionLogSaveRequestDto {
	@NotEmpty
	private Long userId;
	private User user;
	@NotEmpty
	private String content;
	@NotEmpty
	private boolean isSuccess;
	@NotEmpty
	private LocalDateTime date;

	public MissionLog toEntity(MissionToday missionToday) {
		return MissionLog.builder()
			.user(missionToday.getUser())
			.content(missionToday.getMission().getContent())
			.date(missionToday.getCreatedAt())
			.isSuccess(missionToday.isClear())
			.build();
	}
}