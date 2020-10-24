package com.hw.aggregate.file;

import com.hw.aggregate.file.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepo extends JpaRepository<UploadedFile, Long> {
}
