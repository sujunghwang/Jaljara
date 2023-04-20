package com.ssafy.a802.jaljara.db.entity;

public enum MissionContents {
	MISSION1("IMAGE", "입술에 립밤 발라주기."),
	MISSION2("IMAGE", "뽀뽀 백번 해주기."),
	MISSION3("IMAGE","떡뽁기 먹여주기."),
	MISSION4("RECORD", "엉덩이."),
	MISSION5("RECORD", "발바닥.");

	private final String type;
	private final String content;

	MissionContents(String type, String content) {
		this.type = type;
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public String getContent() {
		return content;
	}
}
