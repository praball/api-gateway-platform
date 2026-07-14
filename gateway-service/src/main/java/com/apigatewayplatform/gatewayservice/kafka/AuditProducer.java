package com.apigatewayplatform.gatewayservice.kafka;

import com.apigatewayplatform.gatewayservice.dto.AuditEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditProducer {

    private final KafkaTemplate<String, AuditEvent> kafkaTemplate;

    public void publish(AuditEvent event) {
        kafkaTemplate.send("audit-events", event);
    }
}
