package com.hw.controller;

import com.hw.entity.UploadedFile;
import com.hw.repo.UploadedFileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(path = "v1/api", produces = "application/pdf")
public class FileUploadController {
    @Autowired
    UploadedFileRepo uploadedFileRepo;

    @GetMapping("files/{fileId}")
    public ResponseEntity<byte[]> getUploadedFileById(@PathVariable(name = "fileId") Long profileId) {
        Optional<UploadedFile> findById = uploadedFileRepo.findById(profileId);
        if (findById.isEmpty())
            return ResponseEntity.badRequest().build();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("content-type",
                findById.get().getContentType());
        responseHeaders.setContentDispositionFormData(findById.get().getOriginalName(), findById.get().getOriginalName());
        byte[] fileInBytes = null;
        File fi = new File(findById.get().getSystemPath());
        try {
            fileInBytes = Files.readAllBytes(fi.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().headers(responseHeaders).body(fileInBytes);
    }

    @PostMapping("files")
    public ResponseEntity<?> uploadedFiles(@RequestHeader("authorization") String authorization, @RequestParam("file") MultipartFile file) {
        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setContentType(file.getContentType());
        uploadedFile.setOriginalName(file.getOriginalFilename());
        String path = "files/" + UUID.randomUUID().toString() + ".upload";
        File targetFile = new File(path);
        File dir = new File("files");
        if (!dir.exists()) {
            dir.mkdir();
        }
        OutputStream outStream;
        try {
            targetFile.createNewFile();
            outStream = new FileOutputStream(targetFile, false);
            outStream.write((file.getBytes()));
            uploadedFile.setSystemPath(path);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("error during saving file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        UploadedFile save = uploadedFileRepo.save(uploadedFile);
        return ResponseEntity.ok().header("Location", save.getId().toString()).build();
    }


}
