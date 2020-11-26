package com.hw.shared.idempotent.model;

import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hw.shared.Auditable;
import com.hw.shared.idempotent.OperationType;
import com.hw.shared.idempotent.command.AppCreateChangeRecordCommand;
import com.hw.shared.rest.Aggregate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"changeId", "entityType"}))
@Data
@NoArgsConstructor
public class ChangeRecord extends Auditable implements Aggregate {
    public static final String CHANGE_ID = "changeId";
    public static final String ENTITY_TYPE = "entityType";
    @Id
    private Long id;
    @Column(nullable = false)
    private String changeId;
    @Column(nullable = false)
    private String entityType;
    @Column(nullable = false)
    private String serviceBeanName;

    @Lob
    @Column(columnDefinition = "BLOB")
    //@Convert(converter = CustomByteArraySerializer.class)
    // not using converter due to lazy load , no session error
    private byte[] replacedVersion;
    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] requestBody;
    @Version
    private Integer version;
    private HashSet<Long> deletedIds;
    private OperationType operationType;
    private String query;

    private ChangeRecord(Long id, AppCreateChangeRecordCommand command, ObjectMapper om) {
        this.id = id;
        this.changeId = command.getChangeId();
        this.entityType = command.getEntityType();
        this.serviceBeanName = command.getServiceBeanName();
        if (command.getRequestBody() instanceof JsonSerializable) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                om.writeValue(baos, command.getRequestBody());
                this.requestBody = baos.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (command.getRequestBody() instanceof MultipartFile) {
                this.requestBody = "MultipartFile skipped".getBytes();
            } else {
                this.requestBody = CustomByteArraySerializer.convertToDatabaseColumn(command.getRequestBody());
            }
        }
        this.operationType = command.getOperationType();
        this.query = command.getQuery();
        this.replacedVersion = CustomByteArraySerializer.convertToDatabaseColumn(command.getReplacedVersion());
        if (command.getDeletedIds() != null)
            this.deletedIds = new HashSet<>(command.getDeletedIds());
    }

    public static ChangeRecord create(Long id, AppCreateChangeRecordCommand command, ObjectMapper om) {
        return new ChangeRecord(id, command, om);
    }
}
