package orlov.oleksandr.programming.userrest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyConfig {
    @Value("${minimal.age}")
    private int minimalAge;

    @Bean
    public int minimalAge() {
        return minimalAge;
    }
}
