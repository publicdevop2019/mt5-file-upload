package com.hw.aggregate.file.model;

import com.hw.aggregate.file.exception.FileSizeException;
import com.hw.aggregate.file.exception.FileTypeException;
import com.hw.aggregate.file.exception.FileUploadException;
import com.hw.shared.Auditable;
import com.hw.shared.rest.Aggregate;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Entity
@Table(name = "uploaded_file")
@Data
@NoArgsConstructor
@Slf4j
public class UploadedFile extends Auditable implements Aggregate {

    @Id
    private Long id;

    @Column
    private String systemPath;

    @Column
    private String originalName;

    @Column
    private String contentType;
    @Version
    @Setter(AccessLevel.NONE)
    private Integer version;

    public static UploadedFile create(long id, MultipartFile command, Integer allowedSize, List<String> allowedTypes) {
        return new UploadedFile(id, command, allowedSize, allowedTypes);
    }

    public UploadedFile(Long id, MultipartFile file, Integer allowedSize, List<String> allowedTypes) {
        validateUploadCriteria(file, allowedSize, allowedTypes);
        String path = "files/" + id + ".upload";
        this.id = id;
        this.contentType = file.getContentType();
        this.originalName = file.getOriginalFilename();
        this.systemPath = path;
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
    }

    /**
     * validate file type, file size
     */
    private void validateUploadCriteria(MultipartFile file, Integer allowedSize, List<String> allowedTypes) {
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
