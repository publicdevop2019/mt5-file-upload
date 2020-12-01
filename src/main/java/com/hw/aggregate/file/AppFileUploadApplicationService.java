package com.hw.aggregate.file;

import com.hw.aggregate.file.model.UploadedFile;
import com.hw.shared.rest.RoleBasedRestfulService;
import com.hw.shared.rest.VoidTypedClass;
import com.hw.shared.sql.RestfulQueryRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Slf4j
public class AppFileUploadApplicationService extends RoleBasedRestfulService<UploadedFile, Void, Void, VoidTypedClass> {
    @Value("${allowed.types}")
    private List<String> allowedTypes;

    @Value("${allowed.size}")
    private Integer allowedSize;

    {
        entityClass = UploadedFile.class;
        role = RestfulQueryRegistry.RoleEnum.APP;
    }

    @Override
    protected UploadedFile createEntity(long id, Object command) {
        return UploadedFile.create(id, (MultipartFile) command, allowedSize, allowedTypes);
    }
}
