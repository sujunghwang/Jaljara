package com.ssafy.a802.jaljara.api.dto.response;

import com.ssafy.a802.jaljara.db.entity.ContentType;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ContentsDetailResponseDto {

	private Long contentsId;

	private ContentType contentType;

	private String url;

	public ContentsDetailResponseDto(Long contentsId, ContentType contentType, String url) {
		this.contentsId = contentsId;
		this.contentType = contentType;
		this.url = url;
	}
}
