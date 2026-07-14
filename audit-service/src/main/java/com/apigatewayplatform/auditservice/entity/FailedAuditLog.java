package com.apigatewayplatform.auditservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "failed_audit_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FailedAuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String method;

    private String path;

    private Integer status;

    private Instant timestamp;
}
