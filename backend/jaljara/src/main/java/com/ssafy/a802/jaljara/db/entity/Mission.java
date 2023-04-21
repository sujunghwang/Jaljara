package com.ssafy.a802.jaljara.db.entity;

import javax.persistence.*;

@Entity
public class Mission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private MissionType missionType;

	private String content;


}
