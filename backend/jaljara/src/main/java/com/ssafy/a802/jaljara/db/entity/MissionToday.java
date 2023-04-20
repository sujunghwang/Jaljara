package com.ssafy.a802.jaljara.db.entity;

import javax.persistence.*;

@Entity
public class MissionToday {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int remainRerollCount;

	private boolean isClear;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_id")
	private Mission mission;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
}
