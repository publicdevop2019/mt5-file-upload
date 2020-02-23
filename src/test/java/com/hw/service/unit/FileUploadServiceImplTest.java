package com.hw.service.unit;

import com.hw.repo.UploadedFileRepo;
import com.hw.service.FileUploadServiceImpl;
import com.hw.shared.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class FileUploadServiceImplTest {
    @InjectMocks
    FileUploadServiceImpl fileUploadService = new FileUploadServiceImpl();

    @Mock
    UploadedFileRepo uploadedFileRepo;

    @Before
    public void init() {
        ReflectionTestUtils.setField(fileUploadService, "allowedTypes", Arrays.asList("image/jpeg"));
        ReflectionTestUtils.setField(fileUploadService, "allowedSize", 1);
    }

    @Test(expected = BadRequestException.class)
    public void get_file_with_not_exist_id() {
        Mockito.doReturn(Optional.empty()).when(uploadedFileRepo).findById(any(Long.class));
        fileUploadService.getUploadedFileById(new Random().nextLong());
    }

    @Test(expected = BadRequestException.class)
    public void uploaded_not_allowed_file_type() {
        MockMultipartFile file = new MockMultipartFile("file", "orig", "application/pdf", "bar".getBytes());
        fileUploadService.uploadedFiles(file);
    }

    @Test(expected = BadRequestException.class)
    public void uploaded_not_allowed_file_size() {
        MockMultipartFile file = new MockMultipartFile("file", "orig", "image/jpeg", "bar".getBytes());
        fileUploadService.uploadedFiles(file);
    }
}