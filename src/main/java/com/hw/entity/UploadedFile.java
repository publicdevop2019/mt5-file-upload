package com.hw.entity;

import com.hw.shared.Auditable;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "UploadedFile")
@SequenceGenerator(name = "uploadedFileId_gen", sequenceName = "uploadedFileId_gen", initialValue = 100)
@Data
public class UploadedFile extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uploadedFileId_gen")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column
    private String systemPath;

    @Column
    private String originalName;

    @Column
    private String contentType;

}
