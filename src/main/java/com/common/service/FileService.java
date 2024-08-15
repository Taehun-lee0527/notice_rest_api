package com.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileService {
    void uploadFile(MultipartFile multipartFile, Path uploadFilePath, Path destinationFile) throws Exception;

    void deleteFile(String filePath) throws Exception;
}
