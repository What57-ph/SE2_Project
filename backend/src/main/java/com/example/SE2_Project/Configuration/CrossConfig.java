package com.example.SE2_Project.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;  // Đảm bảo dùng đúng lớp này từ Spring Web
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class CrossConfig {

    @Bean
    public CorsFilter corsFilter() {
        // Sử dụng đúng UrlBasedCorsConfigurationSource của Spring Web
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Cho phép gửi cookie
        config.addAllowedOrigin("http://localhost:3000"); // Frontend của bạn
        config.setAllowedHeaders(List.of("*")); // Cho phép tất cả các headers
        config.setAllowedMethods(List.of("*")); // Cho phép tất cả các phương thức (GET, POST, PUT, DELETE,...)

        source.registerCorsConfiguration("/**", config); // Đảm bảo CORS cho tất cả URL

        return new CorsFilter(source); // Trả về đối tượng CorsFilter của Spring
    }
}
