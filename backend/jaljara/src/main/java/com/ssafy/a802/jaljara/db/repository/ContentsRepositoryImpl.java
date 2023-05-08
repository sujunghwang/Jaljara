package com.ssafy.a802.jaljara.db.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.ssafy.a802.jaljara.api.dto.response.ContentsDetailResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.ContentsResponseDto;
import com.ssafy.a802.jaljara.db.entity.ContentType;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ContentsRepositoryImpl {

	private final EntityManager em;

	public ContentsResponseDto findContentsList () {
		ContentsResponseDto result = new ContentsResponseDto();
		result.setContentsVideoList(findContentsDetailList(ContentType.VIDEO));
		result.setContentsSoundList(findContentsDetailList(ContentType.SOUND));
		return result;
	}

	//컨텐츠 상세 개별 조회
	public ContentsDetailResponseDto findContentsDetail (Long contentsId) {
		return em.createQuery(
			"select new com.ssafy.a802.jaljara.api.dto.response.ContentsDetailResponseDto"
				+ "(c.id, c.contentType, c.title, c.description, c.youtubeUrl, c.thumbnailImageUrl)"
				+ " from Contents c"
				+ " where c.id = :contentsId", ContentsDetailResponseDto.class)
			.setParameter("contentsId", contentsId)
			.getSingleResult();
	}

	//컨텐츠 상세 리스트 조회
	public List<ContentsDetailResponseDto> findContentsDetailList (ContentType contentType) {
		return em.createQuery(
			"select new com.ssafy.a802.jaljara.api.dto.response.ContentsDetailResponseDto"
				+ "(c.id, c.contentType, c.title, c.description, c.youtubeUrl, c.thumbnailImageUrl)"
				+ " from Contents c"
				+ " where c.contentType =:contentType", ContentsDetailResponseDto.class)
			.setParameter("contentType", contentType)
			.getResultList();
	}
}
