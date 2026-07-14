package com.apigatewayplatform.auditservice.kafka;

import com.apigatewayplatform.auditservice.dto.AuditEvent;
import com.apigatewayplatform.auditservice.entity.AuditLog;
import com.apigatewayplatform.auditservice.repository.AuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditConsumer {

    private final AuditRepository auditRepository;

    private static final String TOPIC = "audit-events";

    @RetryableTopic(attempts = "4", backoff = @Backoff(delay = 2000))
    @KafkaListener(topics = TOPIC)
    public void consume(AuditEvent event) {
        log.info("Received audit event: {}", event);
        AuditLog auditLog = AuditLog.builder()
                .username(event.getUsername())
                .method(event.getMethod())
                .path(event.getPath())
                .status(event.getStatus())
                .timestamp(event.getTimestamp())
                .build();
        // If you want to simulate when DB is down & to check DLT implementation, uncomment:
//        throw new RuntimeException("DB down");
        auditRepository.save(auditLog);
        log.info("Audit event persisted.");
    }
}
