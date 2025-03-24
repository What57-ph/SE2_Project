package com.example.SE2_Project.Configuration;

import com.example.SE2_Project.Security.CustomeUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(auth -> auth
                        // Các URL cho phép truy cập mà không cần đăng nhập
                        .requestMatchers("/process-login","/categories/**", "/users/**", "/process-after-login", "/report", "/homepage", "/api/auth/register","/guest/register").permitAll()
                        .requestMatchers("/auth/**", "/calender/**", "/report/**", "/expenses/**", "/css/**", "/img/**", "/js/**","/img/**").permitAll()
                        .requestMatchers("/login", "/guest/login").permitAll()
                        // Tất cả các request còn lại đều phải xác thực
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/guest/login")
                        .loginProcessingUrl("/process-login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/process-after-login", true)  // Chuyển hướng đến trang homepage sau khi đăng nhập thành công
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .invalidSessionUrl("/login?invalid-session=true")  // Chuyển hướng nếu session không hợp lệ
                        .maximumSessions(1)  // Giới hạn số lượng session
                        .expiredUrl("/login?expired=true")  // Nếu session hết hạn, chuyển hướng đến trang login
                );
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }
    @Autowired
    private CustomeUserDetail customeUserDetail;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customeUserDetail);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
