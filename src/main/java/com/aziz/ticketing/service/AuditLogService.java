package com.aziz.ticketing.service;

import com.aziz.ticketing.entity.AuditLogEntity;
import com.aziz.ticketing.repository.AuditLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void save(String userId, String correlationId, String method, String path, String requestBody, String responseBody, int status) {
        AuditLogEntity log = new AuditLogEntity();
        log.setUserId(userId);
        log.setCorrelationId(correlationId);
        log.setMethod(method);
        log.setPath(path);
        log.setRequestBody(requestBody);
        log.setResponseBody(responseBody);
        log.setHttpStatus(status);
        log.setTs(OffsetDateTime.now());
        auditLogRepository.save(log);
    }
}