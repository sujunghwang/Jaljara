package com.ssafy.a802.jaljara.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<?> getContentsList() {
		ContentsResponseDto contentsList = contentsService.findContentsList();
		return new ResponseEntity<>(contentsList, HttpStatus.OK);
	}

	@GetMapping("/type")
	public ResponseEntity<?> getContentsListWithContentType(
		@RequestParam("contentType") String contentType) {
		List<ContentsDetailResponseDto> contentList =
			contentsService.findContentsListWithContentType(contentType);
		return new ResponseEntity<>(contentList, HttpStatus.OK);
	}

	@GetMapping("/{contentsId}")
	public ResponseEntity<?> getContents(@PathVariable long contentsId) {
		ContentsDetailResponseDto contents = contentsService.findContents(contentsId);
		return new ResponseEntity<>(contents, HttpStatus.OK);
	}
}
