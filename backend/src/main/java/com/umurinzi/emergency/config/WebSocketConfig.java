package com.umurinzi.emergency.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * STOMP-over-WebSocket endpoint for live emergency/Helper-response updates
 * (SDD §1.4d, §5.8a). Broker topic shape: {@code /topic/emergencies/{id}}.
 *
 * <p><b>Phase 0 scope only:</b> registers the endpoint and broker prefixes so the
 * shape described in the SDD exists and other modules can depend on it. JWT
 * validation on {@code CONNECT} and the per-topic {@code HelperAccessGuard} recheck on
 * {@code SUBSCRIBE} (the {@code realtime.StompChannelInterceptor} from SDD §3) are
 * Phase 4b work, once there's an authenticated principal and an ownership check to
 * enforce — wiring an interceptor that can't yet do either would be a false sense of
 * security, not scaffolding.
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // TODO (Phase 4b): restrict to known client origins
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    // TODO (Phase 4b): register a ChannelInterceptor on the inbound client channel that
    // validates the JWT on CONNECT and re-runs HelperAccessGuard on every SUBSCRIBE to
    // /topic/emergencies/{id} (SDD §3, §6).
}
