package com.example.SE2_Project.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Vô hiệu hóa CSRF nếu không cần thiết
                .authorizeHttpRequests(auth -> auth
                        // Các URL cho phép truy cập mà không cần đăng nhập
                        .requestMatchers("/login/**", "/users/**", "/process-after-login","/report", "/homepage","/api/auth/register").permitAll()
                        .requestMatchers("/auth/**", "/calender/**", "/report/**", "/expenses/**", "/css/**", "/img/**", "/js/**").permitAll()
                        .requestMatchers("/categories/**").authenticated()
                        // Tất cả các request còn lại đều phải xác thực
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/process-after-login", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
