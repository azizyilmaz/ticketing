package com.aziz.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "ix_audit_user", columnList = "userId, ts"),
        @Index(name = "ix_audit_corr", columnList = "correlationId")})
@Getter
@Setter
public class AuditLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String correlationId;

    private String method;

    private String path;

    @Lob
    private String requestBody;

    @Lob
    private String responseBody;

    private int httpStatus;

    private OffsetDateTime ts;
}
