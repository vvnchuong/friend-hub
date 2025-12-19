package com.friendhub.controller;

import com.friendhub.dto.response.ApiResponse;
import com.friendhub.dto.response.FileResponse;
import com.friendhub.enums.ErrorCode;
import com.friendhub.exception.AppException;
import com.friendhub.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @Value("${upload-file.base-uri}")
    private String basePath;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ApiResponse<List<FileResponse>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("folder") String folder
    ) throws IOException {

        if (files == null || files.isEmpty()) {
            throw new AppException(ErrorCode.FILE_EMPTY);
        }

        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "mp4");

        List<FileResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {

            if (file.isEmpty())
                throw new AppException(ErrorCode.FILE_EMPTY);

            String fileName = file.getOriginalFilename().toLowerCase();

            boolean isValid = allowedExtensions.stream().anyMatch(fileName::endsWith);
            if (!isValid)
                throw new AppException(ErrorCode.INVALID_FILE);

            fileService.createUploadFolder(folder);

            String uploaded = fileService.store(file, folder);

            responses.add(FileResponse.builder()
                    .fileName(uploaded)
                    .uploadedAt(Instant.now())
                    .build());
        }

        return ApiResponse.<List<FileResponse>>builder()
                .result(responses)
                .build();
    }

}
