package com.hw.aggregate.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.hw.shared.AppConstant.HTTP_HEADER_CHANGE_ID;

@Slf4j
@RestController
@RequestMapping
public class FileUploadController {

    @Autowired
    PublicFileUploadApplicationService publicFileUploadApplicationService;
    @Autowired
    AppFileUploadApplicationService appFileUploadApplicationService;

    @GetMapping("files/public/{id}")
    public ResponseEntity<byte[]> getUploadedFileById(@PathVariable(name = "id") Long id) {
        return publicFileUploadApplicationService.readById(id);
    }

    @PostMapping("files/app")
    public ResponseEntity<?> uploadedFiles(@RequestParam("file") MultipartFile file, @RequestHeader(HTTP_HEADER_CHANGE_ID) String changeId) {
        return ResponseEntity.ok().header("Location", appFileUploadApplicationService.create(file, changeId).getId().toString()).build();
    }


}
