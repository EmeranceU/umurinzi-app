package com.umurinzi.emergency.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Serves whatever {@code ProfilePhotoStorageService} writes to disk back out over
 * HTTP at {@code /uploads/**} — the other half of SDD §5.2 {@code POST
 * /users/me/photo}. Public/unauthenticated on purpose (see the matching entry in
 * {@code SecurityConfig.PUBLIC_PATHS}): React Native's {@code <Image>} doesn't attach
 * an Authorization header, and a profile photo URL is meant to be shareable the same
 * way any other avatar URL is.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + uploadDir + "/");
    }
}
