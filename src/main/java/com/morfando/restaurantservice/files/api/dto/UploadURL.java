package com.morfando.restaurantservice.files.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UploadURL {
	@Schema(description = "URL where the data is to be uploaded")
	private String uploadURL;
	@Schema(description = "File key for downloading the uploaded file")
	private String fileKey;
}
