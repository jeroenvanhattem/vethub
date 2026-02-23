package dev.ilionx.workshop.common.config.properties;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


/**
 * Security related properties.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.cors")
public class SecurityProperties {

    /**
     * Allowed origin patterns for CORS (e.g., "http://localhost:*").
     * Supports wildcards when used with allowCredentials=true.
     */
    private String[] allowedOriginPatterns = {
        "http://localhost:*",
    };

}
