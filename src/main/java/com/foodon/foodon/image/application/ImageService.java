package com.foodon.foodon.image.application;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String upload(MultipartFile multipartFile);
}
