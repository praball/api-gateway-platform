package com.apigatewayplatform.auditservice.repository;

import com.apigatewayplatform.auditservice.entity.FailedAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedAuditRepository extends JpaRepository<FailedAuditLog, Long> {
}
