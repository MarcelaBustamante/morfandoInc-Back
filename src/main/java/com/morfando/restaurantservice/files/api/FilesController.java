package com.morfando.restaurantservice.files.api;

import com.morfando.restaurantservice.files.api.dto.UploadURL;
import com.morfando.restaurantservice.users.api.application.S3Integration;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/files")
public class FilesController {

	private final S3Integration s3;

	@GetMapping("/upload/link")
	public UploadURL generateUploadLink(
			@RequestParam String fileName,
			@Parameter(description = "File size in bytes") @RequestParam long fileSize) {
		return s3.createUploadURL(fileName, fileSize);
	}
}
