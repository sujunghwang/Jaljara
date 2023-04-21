package com.ssafy.a802.jaljara.db.entity;

import javax.persistence.*;

@Entity
public class Mission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Transient
	private MissionContents missionContents;

	private String type;

	private String content;


}
