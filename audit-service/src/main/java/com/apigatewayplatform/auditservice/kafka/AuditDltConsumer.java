package com.apigatewayplatform.auditservice.kafka;

import com.apigatewayplatform.auditservice.dto.AuditEvent;
import com.apigatewayplatform.auditservice.entity.FailedAuditLog;
import com.apigatewayplatform.auditservice.repository.FailedAuditRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditDltConsumer {

    private final FailedAuditRepository failedAuditRepository;

    @KafkaListener(topics = "audit-events-dlt", groupId = "audit-dlt-group", containerFactory = "kafkaListenerContainerFactory")
    public void consumeDlt(AuditEvent event) {
        log.error("DLT: Received event {}", event);

        FailedAuditLog failed = FailedAuditLog.builder()
                .username(event.getUsername())
                .method(event.getMethod())
                .path(event.getPath())
                .status(event.getStatus())
                .timestamp(event.getTimestamp())
                .build();

        failedAuditRepository.save(failed);

        log.info("DLT: Failed audit event persisted.");
    }

}
