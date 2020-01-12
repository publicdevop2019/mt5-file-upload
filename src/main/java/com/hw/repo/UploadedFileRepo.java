package com.hw.repo;

import com.hw.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepo extends JpaRepository<UploadedFile, Long> {
}
