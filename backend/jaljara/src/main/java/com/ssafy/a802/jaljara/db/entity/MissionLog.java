package com.ssafy.a802.jaljara.db.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class MissionLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_id")
	private Mission mission;

	private LocalDateTime successTimestamp;

	private boolean isSuccess;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_log_id")
	private MissionAttachment missionAttachment;

}
