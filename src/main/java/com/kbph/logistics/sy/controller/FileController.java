package com.kbph.logistics.sy.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.kbph.logistics.configuration.I18nConfig;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class FileController {

	private final ResourceLoader resourceLoader;
	private static final String MANUAL_SUFFIX = "경북포항_IM21_UserGuide(";
	private static final String MANUAL_PREFIX = ")_v1.0.pdf";
	private final I18nConfig i18nConfig;

	@GetMapping("/sy/file/{fileId}")
	public ResponseEntity<Resource> manualDownload(@PathVariable("fileId") String fileId) {
	    try {
	        MessageSourceAccessor messageSourceAccessor = i18nConfig.getMessageSourceAccessor();
	        String fileName = messageSourceAccessor.getMessage("header." + fileId, Locale.getDefault());
	        String downloadFileName = MANUAL_SUFFIX + fileName + MANUAL_PREFIX;
	        Resource resource = resourceLoader.getResource("classpath:static/manual/" + downloadFileName);

	        // Check if resource exists
	        if (!resource.exists()) {
	            return ResponseEntity.notFound().build();
	        }

	        // UTF-8로 파일 이름 인코딩
	        String encodedFileName = URLEncoder.encode(downloadFileName, StandardCharsets.UTF_8.toString());
	        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName;

	        return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
	                .body(new InputStreamResource(resource.getInputStream()));

	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
}
