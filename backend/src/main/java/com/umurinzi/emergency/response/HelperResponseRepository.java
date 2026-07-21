package com.umurinzi.emergency.response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelperResponseRepository extends JpaRepository<HelperResponse, UUID> {

    List<HelperResponse> findByEmergencyEventId(UUID emergencyEventId);

    Optional<HelperResponse> findByEmergencyEventIdAndContactId(UUID emergencyEventId, UUID contactId);

    List<HelperResponse> findByResponderUserId(UUID responderUserId);
}
