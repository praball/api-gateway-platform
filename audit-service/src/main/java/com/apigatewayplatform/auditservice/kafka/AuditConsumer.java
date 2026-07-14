package com.apigatewayplatform.auditservice.kafka;

import com.apigatewayplatform.auditservice.dto.AuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditConsumer {

    @KafkaListener(
            topics = "audit-events")
    public void consume(AuditEvent event) {
        System.out.println("Received audit event: " + event);
    }
}
