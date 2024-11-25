package org.koreait.global.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfig {

    @Lazy
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
