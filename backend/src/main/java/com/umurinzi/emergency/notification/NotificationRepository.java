package com.umurinzi.emergency.notification;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByRecipientUserId(UUID recipientUserId, Pageable pageable);

    Optional<Notification> findByProviderMessageId(String providerMessageId);

    Optional<Notification> findByTrackingToken(String trackingToken);
}
