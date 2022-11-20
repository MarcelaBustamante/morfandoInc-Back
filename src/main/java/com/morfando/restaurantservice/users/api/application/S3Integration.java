package com.morfando.restaurantservice.users.api.application;

import com.morfando.restaurantservice.files.api.dto.UploadURL;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class S3Integration {
	private static final long MAX_SIZE_MB = 25;
	private static final long MAX_SIZE_BYTES = MAX_SIZE_MB * 1024 * 1024;
	private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
	private final Duration linkExpiration;
	private final S3Presigner presigner;
	private final String bucket;
	private final String bucketBasePath;

	public S3Integration(@Value("${s3.profile:}") String profile,
						 @Value("${s3.region}") String region,
						 @Value("${s3.link-expiration:5m}") Duration linkExpiration,
						 @Value("${s3.bucket.name}") String bucket,
						 @Value("${s3.bucket.base-path}") String bucketBasePath) {
		this.linkExpiration = linkExpiration;
		this.bucket = bucket;
		this.bucketBasePath = bucketBasePath;
		if (StringUtils.isNotBlank(profile)) {
			this.presigner = S3Presigner.builder()
					.region(Region.of(region))
					.credentialsProvider(ProfileCredentialsProvider.create(profile))
					.build();
		} else {
			this.presigner = S3Presigner.builder()
					.region(Region.of(region))
					.build();
		}
	}

	public UploadURL createUploadURL(String nombreArchivo, long bytes) {
		if(bytes > MAX_SIZE_BYTES) {
			throw new IllegalStateException("File bigger than max allowed: " + MAX_SIZE_MB + " MB");
		}
		String fileKey = createFileKey(nombreArchivo);
		PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(builder -> builder
				.signatureDuration(linkExpiration)
				.putObjectRequest(req -> req.
						bucket(bucket)
						.key(fileKey)
						.contentLength(bytes))
				.build());
		return new UploadURL(presignedRequest.url().toString(), fileKey);
	}

	private String createFileKey(String originalFileName) {
		return bucketBasePath + "/" + LocalDateTime.now().format(DTF) + "." + originalFileName;
	}
}
