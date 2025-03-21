package com.example.SE2_Project.Configuration;

import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationInitConfig {

    @Value("${admin.username:admin}")
    private String ADMIN_USERNAME;

    @Value("${admin.password:admin123}")
    private String ADMIN_PASSWORD;

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public ApplicationInitConfig(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            log.info("🚀 Initializing application...");

            // Kiểm tra xem admin đã tồn tại chưa
            if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()) {
                log.info("🛠️ Admin user not found. Creating default admin...");

                // Tạo admin user
                UserEntity admin = new UserEntity();
                admin.setUsername(ADMIN_USERNAME);
                admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
                admin.setUsername("admin"); // Thêm email mặc định
                admin.setRole("ADMIN"); // Gán role là ADMIN

                userRepository.save(admin);
                log.info("✅ Admin user '{}' created successfully.", ADMIN_USERNAME);
            } else {
                log.info("🔎 Admin user '{}' already exists. Skipping initialization.", ADMIN_USERNAME);
            }
        };
    }
}
