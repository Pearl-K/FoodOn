package com.foodon.foodon.image.application;

import com.foodon.foodon.image.domain.ImageFormat;
import com.foodon.foodon.image.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class LocalImageService implements ImageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 사용자가 전송한 파일을 임시 저장
     * (S3 업로드 시 사용될 파일)
     */
    public String upload(MultipartFile multipartFile) {

        ImageFormat.validate(multipartFile);

        try {
            UploadFile uploadFile = UploadFile.from(multipartFile);
            Path filePath = Paths.get(uploadDir , uploadFile.getOriginalFilename());
            Files.createDirectories(filePath.getParent());
            Files.copy(multipartFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            return uploadFile.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException("파일을 디스크에 저장하는 데 실패했습니다.", e);
        }
    }

}
