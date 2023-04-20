package com.ssafy.a802.jaljara.db.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class MissionAttachment {

	@Id
	@GeneratedValue
	private Long id;

	private String url;

	@Enumerated(EnumType.STRING)
	private MissionContents missionContents;

}
