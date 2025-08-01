package com.foodon.foodon.image.application;

import com.foodon.foodon.image.domain.ImageFormat;
import com.foodon.foodon.image.domain.S3Client;
import com.foodon.foodon.image.domain.UploadFile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class S3ImageService implements ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${cloud.aws.s3.upload-path}")
    private String s3UploadPath;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    private final S3Client s3Client;

    @Override
    public String upload(MultipartFile multipartFile) {

        ImageFormat.validate(multipartFile);

        try {
            UploadFile uploadFile = UploadFile.from(multipartFile);
            s3Client.upload(uploadFile);
            return getImageUrl(uploadFile);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드에 실패했습니다.", e);
        }
    }

    public String upload(String imageFileName) {
        try {
            Path filePath = Paths.get(uploadDir, imageFileName);
            File file = filePath.toFile();
            if (!file.exists()) {
                throw new FileNotFoundException("해당 파일명의 파일이 존재하지 않습니다: " + imageFileName);
            }
            s3Client.upload(file);
            Files.delete(file.toPath());
            return getImageUrl(file);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드에 실패했습니다.", e);
        }
    }

    private String getImageUrl(File file) {
        return getImageUrl(file.getName());
    }

    private String getImageUrl(UploadFile uploadFile){
        return getImageUrl(uploadFile);
    }

    private String getImageUrl(String fileName) {
        return baseUrl + String.join(
                "/",
                s3UploadPath,
                fileName
        );
    }

}
