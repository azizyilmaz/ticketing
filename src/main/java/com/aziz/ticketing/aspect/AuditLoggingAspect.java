package com.aziz.ticketing.aspect;

import com.aziz.ticketing.service.AuditLogService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Aspect
@Component
public class AuditLoggingAspect {

    private final AuditLogService auditLogService;

    public AuditLoggingAspect(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logRestCalls(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = Optional.ofNullable(attrs).map(ServletRequestAttributes::getRequest).orElse(null);
        String userId = req != null ? Optional.ofNullable(req.getHeader("X-User-Id")).orElse("anonymous") : "system";
        String corr = req != null ? Optional.ofNullable(req.getHeader("X Correlation-Id")).orElse("-") : "-";
        String method = req != null ? req.getMethod() : "-";
        String path = req != null ? req.getRequestURI() : pjp.getSignature().toShortString();

        String requestBody = ""; // for brevity; add body capture via ContentCachingRequestWrapper if needed

        Object result = null;
        int status = 200;
        try {
            result = pjp.proceed();
            if (result instanceof ResponseEntity<?> resp) {
                status = resp.getStatusCode().value();
            }
            return result;
        } catch (Throwable t) {
            status = 500;
            throw t;
        } finally {
            // Persist asynchronously
            auditLogService.save(userId, corr, method, path, requestBody, String.valueOf(result), status);
        }
    }
}