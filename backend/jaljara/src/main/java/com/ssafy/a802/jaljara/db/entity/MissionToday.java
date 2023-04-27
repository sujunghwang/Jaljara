package com.ssafy.a802.jaljara.db.entity;

import java.util.Date;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MissionToday {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int remainRerollCount;

	private boolean isClear;

	private String url;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date missionDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_id")
	private Mission mission;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@Builder(toBuilder = true)
	public MissionToday(Long id, int remainRerollCount, boolean isClear, String url,
		Mission mission, User user, Date missionDate) {
		this.id = id;
		this.remainRerollCount = remainRerollCount;
		this.isClear = isClear;
		this.url = url;
		this.mission = mission;
		this.user = user;
		this.missionDate = missionDate;
	}

	//====비즈니스 로직=====//
	public void reroll() {
		this.remainRerollCount--;
	}
}
