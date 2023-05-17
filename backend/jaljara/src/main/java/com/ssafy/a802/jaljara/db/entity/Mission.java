package com.ssafy.a802.jaljara.db.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "mission")
public class Mission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, insertable = false)
	private long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MissionType missionType;

	@Column(nullable = false)
	private String content;


}
