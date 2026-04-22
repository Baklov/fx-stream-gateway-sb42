package com.currencyflow.fxgateway.repository;

import com.currencyflow.fxgateway.entity.CurrencySnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface CurrencySnapshotRepository extends JpaRepository<CurrencySnapshotEntity, Long> {
    Optional<CurrencySnapshotEntity> findTopByCurrencyCodeOrderBySnapshotTimeDesc(String currencyCode);
    List<CurrencySnapshotEntity> findByCurrencyCodeAndSnapshotTimeGreaterThanEqualOrderBySnapshotTimeDesc(String currencyCode, Instant threshold);
}
