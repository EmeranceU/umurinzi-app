package com.umurinzi.emergency.emergency;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyEventRepository extends JpaRepository<EmergencyEvent, UUID> {

    Page<EmergencyEvent> findByUserId(UUID userId, Pageable pageable);

    Optional<EmergencyEvent> findFirstByUserIdAndStatus(UUID userId, EmergencyStatus status);

    List<EmergencyEvent> findByStatus(EmergencyStatus status);
}
