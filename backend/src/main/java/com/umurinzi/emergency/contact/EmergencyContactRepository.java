package com.umurinzi.emergency.contact;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmergencyContactRepository extends JpaRepository<EmergencyContact, UUID> {

    List<EmergencyContact> findByOwnerIdOrderByPriorityOrderAsc(UUID ownerId);

    List<EmergencyContact> findByLinkedUserId(UUID linkedUserId);
}
