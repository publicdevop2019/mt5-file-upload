package com.hw.aggregate.file;

import com.hw.aggregate.file.model.UploadedFile;
import com.hw.shared.rest.DefaultRoleBasedRestfulService;
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

@Service
@Slf4j
public class PublicFileUploadApplicationService extends DefaultRoleBasedRestfulService<UploadedFile, Void, ResponseEntity<byte[]>, VoidTypedClass> {
    @PostConstruct
    private void setUp() {
        entityClass = UploadedFile.class;
        role = RestfulQueryRegistry.RoleEnum.PUBLIC;
    }

    @Override
    public UploadedFile replaceEntity(UploadedFile uploadedFile, Object command) {
        return null;
    }

    @Override
    public Void getEntitySumRepresentation(UploadedFile uploadedFile) {
        return null;
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

    @Override
    protected UploadedFile createEntity(long id, Object command) {
        return null;
    }

    @Override
    public void preDelete(UploadedFile uploadedFile) {

    }

    @Override
    public void postDelete(UploadedFile uploadedFile) {

    }

    @Override
    protected void prePatch(UploadedFile uploadedFile, Map<String, Object> params, VoidTypedClass middleLayer) {

    }

    @Override
    protected void postPatch(UploadedFile uploadedFile, Map<String, Object> params, VoidTypedClass middleLayer) {

    }
}
