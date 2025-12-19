package com.friendhub.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${upload-file.base-uri}")
    private String basePath;

    public void createUploadFolder(String folder) throws IOException {
        Path folderPath = Paths.get(basePath, folder);
        System.out.println("Base Path: " + basePath);

        if (!Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
            System.out.println("CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + folderPath);
        } else {
            System.out.println("SKIP MAKING DIRECTORY, ALREADY EXISTS");
        }
    }

    public String store(MultipartFile file, String folder) throws IOException {
        System.out.println("Base Path: " + basePath);

        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();

        Path folderPath = Paths.get(basePath, folder);
        Files.createDirectories(folderPath);

        Path filePath = folderPath.resolve(finalName);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return finalName;
    }
}

