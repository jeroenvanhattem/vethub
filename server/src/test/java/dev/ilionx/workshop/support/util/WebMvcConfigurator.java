package dev.ilionx.workshop.support.util;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

public class WebMvcConfigurator extends TestContextInitializer {

    protected MockMvc mockMvc;

    @BeforeEach
    public void setUpMockMvc() {
        this.mockMvc = configureMockMvc();
    }

    private MockMvc configureMockMvc() {
        return MockMvcBuilders
            .webAppContextSetup(applicationContext)
            .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
            .build();
    }
}
