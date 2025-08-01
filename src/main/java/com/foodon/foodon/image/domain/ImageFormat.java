package com.foodon.foodon.image.domain;

import com.foodon.foodon.image.exception.ImageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static com.foodon.foodon.image.domain.ImageExtension.WEBP;
import static com.foodon.foodon.image.exception.ImageErrorCode.ILLEGAL_IMAGE_FORMAT;
import static com.foodon.foodon.image.exception.ImageErrorCode.IMAGE_IS_NULL;

public class ImageFormat {

    public static void validate(MultipartFile multipartFile) {
        if(multipartFile == null || multipartFile.isEmpty()) {
            throw new ImageException.ImageBadRequestException(IMAGE_IS_NULL);
        }

        if(!isValidImage(multipartFile)) {
            throw new ImageException.ImageBadRequestException(ILLEGAL_IMAGE_FORMAT);
        }
    }

    /*
        파일 확장자 변조 방지하기 위한 이미지 파일 유효성 체크
     */
    private static boolean isValidImage(MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            byte[] headerBytes = new byte[12];
            if (inputStream.read(headerBytes) == -1) return false;

            ImageExtension imageExtension = ImageExtension.from(multipartFile.getOriginalFilename());
            return imageExtension.matches(headerBytes);
        } catch (IOException e) {
            return false;
        }
    }

}
