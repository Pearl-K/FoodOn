package com.foodon.foodon.image.domain;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class S3Client {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.upload-path}")
    private String uploadPath;

    private final AmazonS3 amazonS3;

    public void upload(MultipartFile multipartFile) throws IOException {
        File tempFile = null;

        try {
            tempFile = File.createTempFile("upload_", ".tmp");
            multipartFile.transferTo(tempFile);
            amazonS3.putObject(new PutObjectRequest(
                    bucket,
                    getKey(multipartFile.getOriginalFilename()),
                    tempFile
            ));
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            removeTempFile(tempFile);
        }
    }

    public void upload(File file) throws IOException {
        amazonS3.putObject(new PutObjectRequest(
                bucket,
                getKey(file.getName()),
                file
        ));
    }

    private String getKey(String originalFilename) {
        return String.join(
                "/",
                uploadPath,
                originalFilename
        );
    }

    private void removeTempFile(File tempFile) {
        if(!Objects.isNull(tempFile) && tempFile.exists()) {
            tempFile.delete();
        }
    }
}
