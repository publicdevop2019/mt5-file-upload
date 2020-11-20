package com.hw.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class Auditable implements Serializable {

    public static final String ENTITY_MODIFIED_BY = "modifiedBy";
    public static final String ENTITY_MODIFIED_AT = "modifiedAt";
    public static final String ENTITY_DELETED = "deleted";
    public static final String ENTITY_DELETED_BY = "deletedBy";
    public static final String ENTITY_DELETED_AT = "deletedAt";
    public static final String ENTITY_RESTORED_BY = "restoredBy";
    public static final String ENTITY_RESTORED_AT = "restoredAt";
    public static final String ENTITY_CREATED_BY = "createdBy";
    public static final String ENTITY_CREATED_AT = "createdAt";
    @CreatedBy
    private String createdBy;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @LastModifiedBy
    private String modifiedBy;
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedAt;
    private Boolean deleted;
    private String deletedBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;
    private String restoredBy;
    @Temporal(TemporalType.TIMESTAMP)
    private Date restoredAt;

}
