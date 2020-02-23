package com.hw.service;

import com.hw.entity.UploadedFile;
import com.hw.repo.UploadedFileRepo;
import com.hw.shared.BadRequestException;
import com.hw.shared.InternalServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FileUploadServiceImpl {

    @Autowired
    UploadedFileRepo uploadedFileRepo;

    @Value("${allowed.types}")
    List<String> allowedTypes;

    @Value("${allowed.size}")
    Integer allowedSize;

    public ResponseEntity<byte[]> getUploadedFileById(Long profileId) {
        Optional<UploadedFile> findById = uploadedFileRepo.findById(profileId);
        if (findById.isEmpty())
            throw new BadRequestException("file not found");
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

    public String uploadedFiles(MultipartFile file) {
        validateUploadCriteria(file);
        UploadedFile uploadedFile = new UploadedFile();
        UploadedFile draft = uploadedFileRepo.save(uploadedFile);
        draft.setContentType(file.getContentType());
        draft.setOriginalName(file.getOriginalFilename());
        String path = "files/" + draft.getId().toString() + ".upload";
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
        } catch (IOException e) {
            e.printStackTrace();
            log.error("error during saving file", e);
            throw new InternalServerException("error during saving file");
        }
        draft.setSystemPath(path);
        uploadedFileRepo.save(draft);
        return draft.getId().toString();
    }

    /**
     * validate file type, file size
     */
    private void validateUploadCriteria(MultipartFile file) {
        if (allowedTypes.stream().noneMatch(e -> e.equals(file.getContentType())))
            throw new BadRequestException("file type not allowed");
        try {
            if (file.getBytes().length > allowedSize)
                throw new BadRequestException("file size not allowed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
