package com.beertap.beertap_api.repositories;

import com.beertap.beertap_api.entities.Dispenser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DispenserRepository extends JpaRepository<Dispenser, UUID> {
}