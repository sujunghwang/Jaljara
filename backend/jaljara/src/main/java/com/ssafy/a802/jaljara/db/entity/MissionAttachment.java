package com.ssafy.a802.jaljara.db.entity;

import javax.persistence.*;

@Entity
public class MissionAttachment {

	@Id
	@GeneratedValue
	private Long id;

	private String url;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_log_id")
	private MissionLog missionLog;

	@Enumerated(EnumType.STRING)
	private MissionContents missionContents;

}
