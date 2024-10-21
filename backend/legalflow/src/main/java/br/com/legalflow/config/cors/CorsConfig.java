package br.com.legalflow.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:9000",
                                "http://legalflow-frontend-czaygkejaae5d9bc.brazilsouth-01.azurewebsites.net",
                                "https://legalflow-frontend-czaygkejaae5d9bc.brazilsouth-01.azurewebsites.net",
                                "https://legalflow.online",
                                "https://www.legalflow.online")
                        .allowedOrigins()
                        .allowedOrigins()
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
