package dev.ilionx.workshop.common.config;

import io.github.jframe.autoconfigure.properties.ApplicationProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The configuration for the OpenAPI documentation.
 */
@Configuration
public class OpenAPIConfig {

    /**
     * Configures the OpenAPI group for the API documentation.
     *
     * @param properties the application properties
     * @return the grouped OpenAPI configuration
     */
    @Bean
    public OpenAPI myOpenAPI(final ApplicationProperties properties) {
        final Contact contact = new Contact();
        contact.setName("Ilionx Workshop Team");
        contact.setUrl("https://ilionx.com");

        final Info info = new Info()
            .title("Ilionx Pet Store API")
            .version(properties.getVersion())
            .contact(contact)
            .description("API documentation for the Ilionx Pet Store application.");

        return new OpenAPI().info(info).servers(List.of(aServer(properties.getUrl())));
    }

    private Server aServer(final String url) {
        final Server server = new Server();
        server.setUrl(url);
        return server;
    }
}
