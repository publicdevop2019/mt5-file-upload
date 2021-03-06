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
}
