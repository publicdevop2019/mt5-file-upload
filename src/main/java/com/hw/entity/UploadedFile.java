package com.hw.entity;

import com.hw.shared.Auditable;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "uploaded_file")
@Data
public class UploadedFile extends Auditable {

    @Id
    private Long id;

    @Column
    private String systemPath;

    @Column
    private String originalName;

    @Column
    private String contentType;

}
