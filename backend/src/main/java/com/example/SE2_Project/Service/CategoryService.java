package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.CategoryDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
        // Kiểm tra xem tên danh mục đã tồn tại chưa
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category name already exists");
        }

        // Lấy người dùng hiện tại từ Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> currentUser = userRepository.findByUsername(username);  // Bạn cần tạo phương thức tìm user theo username trong UserRepository

        // Tạo mới CategoryEntity
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(category.getName());
        categoryEntity.setCreatedDate(LocalDateTime.now());

        // Gán người dùng vào CategoryEntity
        categoryEntity.setUser(currentUser.orElse(null));  // Gán người dùng hiện tại vào

        // Lưu CategoryEntity vào cơ sở dữ liệu
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
