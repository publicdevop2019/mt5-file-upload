package com.hw.aggregate.file.model;

import com.hw.shared.sql.RestfulQueryRegistry;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UploadedFileQueryRegistry extends RestfulQueryRegistry<UploadedFile> {
    @Override
    public Class<UploadedFile> getEntityClass() {
        return UploadedFile.class;
    }
    @PostConstruct
    private void setUp() {
        cacheable.put(RoleEnum.USER, true);
        cacheable.put(RoleEnum.ADMIN, true);
        cacheable.put(RoleEnum.APP, true);
        cacheable.put(RoleEnum.PUBLIC, true);
        cacheable.put(RoleEnum.ROOT, true);
    }
}
