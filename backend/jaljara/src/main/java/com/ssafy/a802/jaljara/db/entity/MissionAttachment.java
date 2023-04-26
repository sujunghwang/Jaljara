package com.ssafy.a802.jaljara.db.entity;

import javax.persistence.*;

import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mission_attachment")
@NoArgsConstructor
public class MissionAttachment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, insertable = false)
	private Long id;

	@Column(nullable = false)
	private String url;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_log_id", insertable = false, nullable = false, updatable = false)
	private MissionLog missionLog;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MissionType missionType;

	@Builder
	public MissionAttachment(String url, MissionLog missionLog, MissionType missionType) {
		this.url = url;
		this.missionLog = missionLog;
		this.missionType = missionType;
	}
}
