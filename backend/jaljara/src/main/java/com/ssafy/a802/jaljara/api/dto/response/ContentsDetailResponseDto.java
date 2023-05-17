package com.ssafy.a802.jaljara.api.dto.response;

import com.ssafy.a802.jaljara.db.entity.ContentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ContentsDetailResponseDto {

	private Long contentsId;

	private ContentType contentType;

	private String title;

	private String description;

	private String thumbnailImageUrl;

	private String youtubeUrl;

	public ContentsDetailResponseDto(Long contentsId, ContentType contentType, String title, String description,
		String thumbnailImageUrl, String youtubeUrl) {
		this.contentsId = contentsId;
		this.contentType = contentType;
		this.title = title;
		this.description = description;
		this.thumbnailImageUrl = thumbnailImageUrl;
		this.youtubeUrl = youtubeUrl;
	}
}
