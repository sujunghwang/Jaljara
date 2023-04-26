package com.ssafy.a802.jaljara.api.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.a802.jaljara.api.dto.response.ContentsDetailResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.ContentsResponseDto;
import com.ssafy.a802.jaljara.db.entity.ContentType;
import com.ssafy.a802.jaljara.db.repository.ContentsRepository;
import com.ssafy.a802.jaljara.db.repository.ContentsRepositoryImpl;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentsService {

	private final ContentsRepository contentsRepository;
	private final ContentsRepositoryImpl contentsRepositoryImpl;

	//get contents list
	public ContentsResponseDto findContentsList() {
		return contentsRepositoryImpl.findContentsList();
	}

	//get contents list with contentType
	public List<ContentsDetailResponseDto> findContentsListWithContentType(String contentType) {
		return contentsRepositoryImpl.findContentsDetailList(ContentType.valueOf(contentType));
	}

	//get contents
	public ContentsDetailResponseDto findContents(Long contentsId) {
		return contentsRepositoryImpl.findContentsDetail(contentsId);
	}
}
