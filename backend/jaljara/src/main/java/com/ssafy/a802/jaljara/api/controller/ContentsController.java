package com.ssafy.a802.jaljara.api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.a802.jaljara.api.dto.response.ContentsDetailResponseDto;
import com.ssafy.a802.jaljara.api.dto.response.ContentsResponseDto;
import com.ssafy.a802.jaljara.api.service.ContentsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contents")
@RequiredArgsConstructor
public class ContentsController {

	private final ContentsService contentsService;

	@GetMapping
	public ContentsResponseDto getContentsList() {
		return contentsService.findContentsList();
	}

	@GetMapping("/type")
	public List<ContentsDetailResponseDto> getContentsListWithContentType(@RequestParam("contentType") String contentType) {
		return contentsService.findContentsListWithContentType(contentType);
	}

	@GetMapping("/{contentsId}")
	public ContentsDetailResponseDto getContents(@PathVariable Long contentsId) {
		return contentsService.findContents(contentsId);
	}
}
