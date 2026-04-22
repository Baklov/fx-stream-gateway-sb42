package com.currencyflow.fxgateway.repository;

import com.currencyflow.fxgateway.entity.InboundRequestAuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InboundRequestAuditRepository extends JpaRepository<InboundRequestAuditEntity, Long> {
    Optional<InboundRequestAuditEntity> findByRequestId(String requestId);
}
