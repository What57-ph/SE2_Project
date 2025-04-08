package com.example.SE2_Project.Service;

import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Optional<UserEntity> user = userRepository.findByUsername(userDetails.getUsername());
            return user.isPresent() ? user.get().getId() : null;
        }
        return null;
    }

    public UserEntity getUserById(Long userId) {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        return userOptional.orElseThrow(() -> new NoSuchElementException("Người dùng không tồn tại"));
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity createUser(UserEntity user) {
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(false);
        user.setTransactions(Collections.emptyList());
        user.setCreatedDate(LocalDate.now());
        List<CategoryEntity> defaultCategories = categoryRepository.findAllByIdBetween(1L, 12L);
        // Chuyển defaultCategory từ List thành Set
        Set<CategoryEntity> defaultCategorySet = new HashSet<>(defaultCategories);
        user.setCategories(defaultCategorySet);
        // Gán người dùng vào mỗi CategoryEntity (gán ngược lại để đảm bảo quan hệ 2 chiều)
        for (CategoryEntity category : defaultCategorySet) {
            category.getUsers().add(user);  // Gán người dùng vào danh sách users của category
        }
        categoryRepository.saveAll(defaultCategorySet);  // Lưu tất cả các category đã thay đổi
        return userRepository.save(user);
    }

    public UserEntity updateUser(Long id, UserEntity userDetails) {
        UserEntity user = getUserById(id);
        user.setName(userDetails.getName());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);
        user.setStatus(true);
        userRepository.save(user);
    }

    public void activateUser(Long id) {
        UserEntity user = getUserById(id);
        user.setStatus(false);
        userRepository.save(user);
    }
}
