package com.umurinzi.emergency.response;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelperLocationRepository extends JpaRepository<HelperLocation, UUID> {

    List<HelperLocation> findByHelperResponseIdOrderByRecordedAtAsc(UUID helperResponseId);
}
