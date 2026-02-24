package dev.ilionx.workshop.common.config;

import dev.ilionx.workshop.common.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static dev.ilionx.workshop.api.Paths.*;

/**
 * Security configuration for the Pet Clinic application. Enables CORS and Basic Auth for the demo.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final SecurityProperties securityProperties;

    /**
     * Configures the security filter chain with CORS, CSRF disabled, and basic authentication.
     *
     * @param http the HttpSecurity to configure
     * @return the configured SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.httpBasic(Customizer.withDefaults());
        http.authorizeHttpRequests(accessManagement -> {
            accessManagement.requestMatchers(PUBLIC_ACTUATOR_PATH + WILDCARD_PART).permitAll();
            accessManagement.requestMatchers(PUBLIC_PATH + WILDCARD_PART).permitAll();
            accessManagement.requestMatchers(OPENAPI_PATH + WILDCARD_PART).permitAll();
            accessManagement.anyRequest().permitAll();
        });

        return http.build();
    }

    /**
     * Creates a CORS configuration source with allowed origins, methods, and headers.
     *
     * @return the configured CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        // Use allowedOriginPatterns to support wildcards (e.g., "http://localhost:*") with credentials
        configuration.setAllowedOriginPatterns(Arrays.asList(securityProperties.getAllowedOriginPatterns()));
        configuration.setAllowedMethods(List.of(WILDCARD));
        configuration.setAllowedHeaders(List.of(WILDCARD));
        configuration.setExposedHeaders(List.of(WILDCARD));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(WILDCARD_PART, configuration);
        return source;
    }
}
