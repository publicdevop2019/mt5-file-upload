package com.hw.controller;

import com.hw.service.FileUploadServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(path = "v1/api", produces = "application/pdf")
public class FileUploadController {

    @Autowired
    FileUploadServiceImpl fileStorageService;

    @GetMapping("files/{fileId}")
    public ResponseEntity<byte[]> getUploadedFileById(@PathVariable(name = "fileId") Long profileId) {
        return fileStorageService.getUploadedFileById(profileId);
    }

    @PostMapping("files")
    public ResponseEntity<?> uploadedFiles(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok().header("Location", fileStorageService.uploadedFiles(file)).build();
    }


}
