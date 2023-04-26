package com.ssafy.a802.jaljara.api.dto.response;

import java.util.List;

import com.ssafy.a802.jaljara.db.entity.ContentType;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ContentsResponseDto {

	private String contentsVideo = "VIDEO";
	private List<ContentsDetailResponseDto> contentsVideoList;
	private String contentsSOUND = "SOUND";
	private List<ContentsDetailResponseDto> contentsSoundList;

	public ContentsResponseDto(List<ContentsDetailResponseDto> contentsVideoList,
		List<ContentsDetailResponseDto> contentsSoundList) {
		this.contentsVideoList = contentsVideoList;
		this.contentsSoundList = contentsSoundList;
	}
}
