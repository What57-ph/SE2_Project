package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.AuthenticationRequest;
import com.example.SE2_Project.Dto.UserDto;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.UserRepository;
import com.example.SE2_Project.Security.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;

@Service
@Slf4j
public class LoginService {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private UserService userService;


     @Transactional
         public UserEntity registerUser(UserDto userDTO) {

            if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
                throw new RuntimeException("Username đã tồn tại!");
            }

            UserEntity user = new UserEntity();
            user.setUsername(userDTO.getUsername());
            user.setPassword(userDTO.getPassword());
            user.setName(userDTO.getName());

        UserEntity savedUser = userService.createUser(user);
        System.out.println("User saved: " + savedUser.getUsername());
         userRepository.flush();
         return savedUser;

        }

        public UserEntity findByUsername(String username) {
            return userRepository.findByUsername(username).orElseThrow(
                    () -> new RuntimeException("Không tìm thấy người dùng!")
            );
        }

    public String processAfterLogin() {
        Set<String> roleCode = SecurityUtils.getRolesCurrentUser();
        log.info("User roles: {}", roleCode);

        if (roleCode.contains("ROLE_ADMIN")) {
            log.info("redirecting to admin page");
            return "redirect:/admin/show";
        }
        return "redirect:/homepage/show";
    }
    public String loginAndRedirect(AuthenticationRequest request) {
        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Tên đăng nhập không tồn tại"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không chính xác");
        }

        return processAfterLogin();
    }
}
