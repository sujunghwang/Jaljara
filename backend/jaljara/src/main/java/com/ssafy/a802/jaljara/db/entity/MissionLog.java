package com.ssafy.a802.jaljara.db.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

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
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	private String content;

	private LocalDateTime date;

	private boolean isSuccess;

	@Builder
	public MissionLog(User user, String content, LocalDateTime date, boolean isSuccess) {
		this.user = user;
		this.content = content;
		this.date = date;
		this.isSuccess = isSuccess;
	}
}
