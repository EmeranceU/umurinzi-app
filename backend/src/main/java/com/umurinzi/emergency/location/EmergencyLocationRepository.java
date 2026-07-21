package com.umurinzi.emergency.location;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyLocationRepository extends JpaRepository<EmergencyLocation, UUID> {

    List<EmergencyLocation> findByEmergencyEventIdOrderByRecordedAtAsc(UUID emergencyEventId);
}
