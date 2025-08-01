package com.foodon.foodon.image.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ImageName {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS");

    public static String from(String origFileName) {
        String fileName = UUID.randomUUID() + "_" + FORMATTER.format(LocalDateTime.now());
        String extension = ImageExtension.from(origFileName).getExtension();
        return fileName + extension;
    }
}
