package com.ssafy.a802.jaljara.db.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

import javax.persistence.*;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MissionLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@Column(nullable = false)
	private String content;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MissionType missionType;

	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date missionDate;

	@Column(nullable = false)
	private boolean isSuccess;

	@Builder
	public MissionLog(User user, String content, MissionType missionType, Date missionDate, boolean isSuccess) {
		this.user = user;
		this.content = content;
		this.missionType = missionType;
		this.missionDate = missionDate;
		this.isSuccess = isSuccess;
	}
}
