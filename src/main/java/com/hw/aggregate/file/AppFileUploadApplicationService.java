package com.hw.aggregate.file;

import com.hw.aggregate.file.model.UploadedFile;
import com.hw.shared.rest.DefaultRoleBasedRestfulService;
import com.hw.shared.rest.VoidTypedClass;
import com.hw.shared.sql.RestfulQueryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AppFileUploadApplicationService extends DefaultRoleBasedRestfulService<UploadedFile, Void, Void, VoidTypedClass> {
    @Value("${allowed.types}")
    private List<String> allowedTypes;

    @Value("${allowed.size}")
    private Integer allowedSize;

    @PostConstruct
    private void setUp() {
        entityClass = UploadedFile.class;
        role = RestfulQueryRegistry.RoleEnum.APP;
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
    public Void getEntityRepresentation(UploadedFile uploadedFile) {
        return null;
    }

    @Override
    protected UploadedFile createEntity(long id, Object command) {
        return UploadedFile.create(id, (MultipartFile) command,allowedSize,allowedTypes);
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
