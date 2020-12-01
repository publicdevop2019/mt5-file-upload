package com.hw.aggregate.file;

import com.hw.aggregate.file.model.UploadedFile;
import com.hw.shared.rest.RoleBasedRestfulService;
import com.hw.shared.rest.VoidTypedClass;
import com.hw.shared.sql.RestfulQueryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Slf4j
@Service
public class PublicFileUploadApplicationService extends RoleBasedRestfulService<UploadedFile, Void, ResponseEntity<byte[]>, VoidTypedClass> {
    {
        entityClass = UploadedFile.class;
        role = RestfulQueryRegistry.RoleEnum.PUBLIC;
    }

    @Override
    public ResponseEntity<byte[]> getEntityRepresentation(UploadedFile uploadedFile) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("content-type",
                uploadedFile.getContentType());
        responseHeaders.setContentDispositionFormData(uploadedFile.getOriginalName(), uploadedFile.getOriginalName());
        byte[] fileInBytes = null;
        File fi = new File(uploadedFile.getSystemPath());
        try {
            fileInBytes = Files.readAllBytes(fi.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().headers(responseHeaders).body(fileInBytes);
    }
}
