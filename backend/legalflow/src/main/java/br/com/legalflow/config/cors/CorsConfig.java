package br.com.legalflow.config.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${spring.profiles.active}")
    private String profile;
    private static final String LOCAL_ORIGIN = "http://localhost:9000";
    private static final String[] PRODUCTION_ORIGINS = {
            "https://legalflow.online",
            "https://www.legalflow.online"
    };

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                if (profile.equals("prod")) {
                    registry.addMapping("/**")
                            .allowedOrigins(PRODUCTION_ORIGINS)
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                } else {
                    registry.addMapping("/**")
                            .allowedOrigins(LOCAL_ORIGIN)
                            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                            .allowedHeaders("*")
                            .allowCredentials(true);
                }
            }
        };
    }
}
