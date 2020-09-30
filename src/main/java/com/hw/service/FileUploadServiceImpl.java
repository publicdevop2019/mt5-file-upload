package com.hw.service;

import com.hw.entity.UploadedFile;
import com.hw.exception.FileSizeException;
import com.hw.exception.FileTypeException;
import com.hw.exception.FileUploadException;
import com.hw.repo.UploadedFileRepo;
import com.hw.shared.IdGenerator;
import com.hw.shared.rest.exception.EntityNotExistException;
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

@Service
@Slf4j
public class FileUploadServiceImpl {

    @Autowired
    UploadedFileRepo uploadedFileRepo;

    @Value("${allowed.types}")
    List<String> allowedTypes;

    @Value("${allowed.size}")
    Integer allowedSize;

    @Autowired
    private IdGenerator idGenerator;

    public ResponseEntity<byte[]> getUploadedFileById(Long profileId) {
        Optional<UploadedFile> findById = uploadedFileRepo.findById(profileId);
        if (findById.isEmpty())
            throw new EntityNotExistException();
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
        uploadedFile.setId(idGenerator.getId());
        uploadedFile.setContentType(file.getContentType());
        uploadedFile.setOriginalName(file.getOriginalFilename());
        String path = "files/" + uploadedFile.getId().toString() + ".upload";
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
            log.error("error during saving file", e);
            throw new FileUploadException();
        }
        uploadedFile.setSystemPath(path);
        uploadedFileRepo.save(uploadedFile);
        return uploadedFile.getId().toString();
    }

    /**
     * validate file type, file size
     */
    private void validateUploadCriteria(MultipartFile file) {
        if (allowedTypes.stream().noneMatch(e -> e.equals(file.getContentType())))
            throw new FileTypeException();
        try {
            if (file.getBytes().length > allowedSize)
                throw new FileSizeException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
