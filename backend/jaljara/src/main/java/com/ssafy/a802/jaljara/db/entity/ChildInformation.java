package com.ssafy.a802.jaljara.db.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ChildInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "parent_id")
	private Long parentsId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id", insertable = false, updatable = false)
	private User parents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "child_id", insertable = false, updatable = false)
	private User child;

	@Column(name = "follow_to_id")
	private Long childId;

	private String currentReward;

	private int stackCount;

	private LocalDateTime targetBedTime;

	private LocalDateTime targetWakeUpTime;

}
