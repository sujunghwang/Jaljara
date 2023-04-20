package com.ssafy.a802.jaljara.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Getter;

@Entity
@Getter
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;

	private String name;

	private String profileImageUrl;

	private String provider;

	private String sub;

	@Enumerated(EnumType.STRING)
	private UserType userType;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	// @JoinColumn(name = "sleep_data_id") 양방향 아니어도 될듯
	private List<SleepLog> sleepLongList = new ArrayList<>();

	@OneToMany(mappedBy = "user")
	private List<Reward> rewardList = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_today_id")
	private MissionToday missionToday;

	@OneToMany(mappedBy = "user")
	private List<MissionLog> missionLogList = new ArrayList<>();

}
