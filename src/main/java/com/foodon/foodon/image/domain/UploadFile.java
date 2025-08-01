package com.foodon.foodon.image.domain;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class UploadFile implements MultipartFile {

    private final String fileName;
    private final byte[] bytes;

    private UploadFile(String fileName, byte[] bytes) {
        this.fileName = fileName;
        this.bytes = bytes;
    }

    public static UploadFile from(
            MultipartFile multipartFile
    ) throws IOException {
        String fileName = ImageName.from(multipartFile.getOriginalFilename());
        byte[] bytes = multipartFile.getBytes();

        return new UploadFile(fileName, bytes);
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return 0;
    }

    @Override
    public byte[] getBytes()  {
        return bytes;
    }

    @Override
    public InputStream getInputStream()  {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(dest)) {
            fileOutputStream.write(bytes);
        }
    }
}
