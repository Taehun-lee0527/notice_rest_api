package com.common.service.impl;

import com.common.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public void uploadFile(MultipartFile multipartFile, Path uploadFilePath, Path destinationFile) throws Exception {
        try{
            if (!Files.exists(uploadFilePath)) {
                try {
                    Files.createDirectories(uploadFilePath);
                } catch (IOException e) {
                    e.printStackTrace(); // 디렉토리 생성 실패 시 에러 처리
                }
            }

            multipartFile.transferTo(destinationFile.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file " + multipartFile.getOriginalFilename(), e);
        }
    }

    @Override
    public void deleteFile(String filePath) throws Exception {

    }
}
