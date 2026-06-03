package com.beertap.beertap_api.repositories;

import com.beertap.beertap_api.entities.DispenserSpendingLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DispenserSpendingLineRepository extends JpaRepository<DispenserSpendingLine, UUID> {
    List<DispenserSpendingLine> findByDispenserIdOrderByOpenedAtAsc(UUID dispenserId);
}