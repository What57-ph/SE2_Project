package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.CategoryDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;


    @Bean
    public ApplicationRunner initializeCategories() {
        return args -> {
            List<CategoryEntity> defaultName = List.of(
                    new CategoryEntity("Food & Drinks", "EXPENSE", LocalDateTime.now()),
                    new CategoryEntity("Transportation", "EXPENSE", LocalDateTime.now()),
                    new CategoryEntity("Shopping", "EXPENSE", LocalDateTime.now()),
                    new CategoryEntity("Health & Medical", "EXPENSE", LocalDateTime.now()),
                    new CategoryEntity("Entertainment", "EXPENSE", LocalDateTime.now()),
                    new CategoryEntity("Housing & Utilities", "EXPENSE", LocalDateTime.now()),
                    new CategoryEntity("Education", "EXPENSE", LocalDateTime.now()),
                    new CategoryEntity("Travel", "EXPENSE", LocalDateTime.now()),

                    new CategoryEntity("Salary", "INCOME", LocalDateTime.now()),
                    new CategoryEntity("Freelance", "INCOME", LocalDateTime.now()),
                    new CategoryEntity("Investments", "INCOME", LocalDateTime.now()),
                    new CategoryEntity("Gifts & Bonuses", "INCOME", LocalDateTime.now())
            );
            if (categoryRepository.count() == 0) {
//            UserEntity admin = userRepository.findById(1L).orElseThrow();
//            Set<CategoryEntity> adminCat = admin.getCategories();
                for (CategoryEntity category : defaultName) {
//                adminCat.add(category);
                    categoryRepository.save(category);
                }
//            userRepository.save(admin);
                log.info("✅ Default categories initialized.");
            } else {
                log.info("✅ Default categories already exist, skip initialize.");
            }
        };
    }


    public CategoryEntity addCategory(CategoryDto category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Kiểm tra xem người dùng đã có danh mục này chưa
//        boolean categoryExists = categoryRepository.existsByNameAndUser(category.getName(), currentUser);
        CategoryEntity currentCat = categoryRepository.findByNameIgnoreCase(category.getName())
                .orElse(null);
        if (currentUser.getCategories().contains(currentCat) && currentCat != null) {
            throw new IllegalArgumentException("You already have this category");
        }
        if (currentCat == null) {
            // Tạo mới CategoryEntity nếu chưa có
            currentCat = new CategoryEntity();
            String name = category.getName();
            currentCat.setName(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
            currentCat.setCreatedDate(LocalDateTime.now());
            currentCat.setType(category.getType());
            currentCat.setUsers(new HashSet<>()); //
        }

        // Gán 2 chiều
        currentCat.getUsers().add(currentUser);
        currentUser.getCategories().add(currentCat);
        //lưu 2 chiều
        CategoryEntity cat = categoryRepository.save(currentCat);
        userRepository.save(currentUser);
        return cat;
    }

    public Set<CategoryEntity> getCategoriesForCurrentUser() {
        // Lấy tên người dùng hiện tại từ Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Tìm danh mục của người dùng theo username
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return currentUser.getCategories();
    }

    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public CategoryEntity updateCategory(Long id, CategoryDto categoryDto) {
//        CategoryEntity category = categoryRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        CategoryEntity newCat = categoryRepository.findByNameIgnoreCase(categoryDto.getName()).orElse(null);
        CategoryEntity category = new CategoryEntity();
        //neu category mới trùng với category đã có trong db, gán cho ng dùng, nếu chưa có, tạo mới. và xóa cate cũ đi
        if (newCat == null) {
            category = addCategory(categoryDto);
        } else {
            // Gán 2 chiều
            newCat.getUsers().add(currentUser);
            currentUser.getCategories().add(newCat);
            //lưu 2 chiều
            category = categoryRepository.save(newCat);
            userRepository.save(currentUser);
        }
        deleteCategory(id);
        return category;
    }

    public void deleteCategory(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        currentUser.getCategories().remove(category);
        userRepository.save(currentUser);
    }
}
