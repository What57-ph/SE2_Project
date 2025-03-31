package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.CategoryDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    public CategoryEntity addCategory(CategoryDto category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Kiểm tra xem người dùng đã có danh mục này chưa
        boolean categoryExists = categoryRepository.existsByNameAndUser(category.getName(), currentUser);
        if (categoryExists) {
            throw new IllegalArgumentException("You already have this category");
        }

        // Tạo mới CategoryEntity
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());
        categoryEntity.setCreatedDate(LocalDateTime.now());
        categoryEntity.setUser(currentUser);
        categoryEntity.setType(category.getType());
        // Gán người dùng hiện tại

        // Lưu CategoryEntity vào database
        return categoryRepository.save(categoryEntity);
    }

    public List<CategoryEntity> getCategoriesForCurrentUser() {
        // Lấy tên người dùng hiện tại từ Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // Tìm danh mục của người dùng theo username
        return categoryRepository.findByUserUsername(username);
    }
    public CategoryEntity getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public CategoryEntity updateCategory(Long id, CategoryDto categoryDto) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(categoryDto.getName());
        category.setCreatedDate(LocalDateTime.now());

        return categoryRepository.save(category);
    }
    public void deleteCategory(Long id) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        categoryRepository.delete(category);
    }
}
