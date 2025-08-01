package com.foodon.foodon.image.domain;

import com.foodon.foodon.image.exception.ImageErrorCode;
import com.foodon.foodon.image.exception.ImageException;
import com.foodon.foodon.image.exception.ImageException.ImageBadRequestException;
import lombok.Getter;

import java.util.Arrays;

import static com.foodon.foodon.image.exception.ImageErrorCode.ILLEGAL_IMAGE_EXTENSION;

@Getter
public enum ImageExtension {

    JPEG(".jpeg", "ffd8ffe"),
    JPG(".jpg", "ffd8ffe"),
    JFIF(".jfif", "ffd8ffe"),
    PNG(".png", "89504e47"),
    WEBP(".webp", "57454250")
    ;

    private static final String RIFF_SIGNATURE = "52494646";
    private final String extension;
    private final String signature;

    ImageExtension(String extension, String signature) {
        this.extension = extension;
        this.signature = signature;
    }

    public static ImageExtension from(String imageFileName) {
        return Arrays.stream(values())
                .filter(imageExtension -> imageFileName.toLowerCase().endsWith(imageExtension.getExtension()))
                .findFirst()
                .orElseThrow(() -> new ImageBadRequestException(ILLEGAL_IMAGE_EXTENSION));
    }

    public boolean matches(byte[] headerBytes) {
        String hex = bytesToHex(headerBytes);

        if(this == WEBP){
            return hex.length() >= 24
                    && hex.startsWith(RIFF_SIGNATURE)
                    && hex.substring(16, 24).equals(this.signature);
        }

        return hex.startsWith(this.signature);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString().toLowerCase();
    }

}
