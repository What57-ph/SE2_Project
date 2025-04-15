package com.example.SE2_Project.Service;

import com.example.SE2_Project.Dto.CategoryDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Entity.UserEntity;
import com.example.SE2_Project.Repository.CategoryRepository;
import com.example.SE2_Project.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryService categoryService;

    private UserEntity user;
    private CategoryEntity category;

    @BeforeEach
    public void setUp() {
        // Set up mock user and category
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("testUser");
        user.setCategories(new HashSet<>());

        category = new CategoryEntity("Food & Drinks", "EXPENSE", LocalDateTime.now());
        category.setId(1L);
    }

    @Test
    public void testAddCategory() {
        // Mock the Authentication object
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        // Mock the SecurityContext and set the Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);  // Set mocked context to SecurityContextHolder

        // Given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Food & Drinks");
        categoryDto.setType("EXPENSE");

        // Mock behavior for user and category repository
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(categoryRepository.findByNameIgnoreCase("Food & Drinks")).thenReturn(Optional.empty());

        // When
        CategoryEntity savedCategory = categoryService.addCategory(categoryDto);

        // Then
        assertNotNull(savedCategory);
        assertEquals("Food & Drinks", savedCategory.getName());
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
        verify(userRepository, times(1)).save(user);
    }


    @Test
    public void testAddCategoryWithEmptyName() {
        // Given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("");  // Empty name
        categoryDto.setType("EXPENSE");

        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.addCategory(categoryDto);
        });

        assertEquals("Category name cannot be empty", thrown.getMessage());
    }

    @Test
    public void testGetCategoriesForCurrentUser() {
        // Given
        Set<CategoryEntity> categories = new HashSet<>();
        categories.add(category);
        user.setCategories(categories);

        // Mock the user retrieval
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // When
        Set<CategoryEntity> userCategories = categoryService.getCategoriesForCurrentUser();

        // Then
        assertNotNull(userCategories);
        assertEquals(1, userCategories.size());
        assertTrue(userCategories.contains(category));
    }

    @Test
    public void testGetCategoryById() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // When
        CategoryEntity foundCategory = categoryService.getCategoryById(categoryId);

        // Then
        assertNotNull(foundCategory);
        assertEquals(categoryId, foundCategory.getId());
    }

    @Test
    public void testGetCategoryByIdNotFound() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.getCategoryById(categoryId);
        });

        assertEquals("Category not found", thrown.getMessage());
    }

    @Test
    public void testUpdateCategory() {
        // Given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Updated Category");
        categoryDto.setType("EXPENSE");

        // Mock the category repository to return existing category
        when(categoryRepository.findByNameIgnoreCase("Updated Category")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // When
        CategoryEntity updatedCategory = categoryService.updateCategory(category.getId(), categoryDto);

        // Then
        assertNotNull(updatedCategory);
        assertEquals("Updated Category", updatedCategory.getName());
        verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteCategory() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // When
        categoryService.deleteCategory(categoryId);

        // Then
        assertTrue(user.getCategories().isEmpty());
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    public void testDeleteCategoryNotFound() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.deleteCategory(categoryId);
        });

        assertEquals("Category not found", thrown.getMessage());
    }

    @Test
    public void testAddCategoryAlreadyExists() {
        // Given
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Food & Drinks");
        categoryDto.setType("EXPENSE");

        // Mock behavior for user and category repository
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(categoryRepository.findByNameIgnoreCase("Food & Drinks")).thenReturn(Optional.of(category));
        user.setCategories(Set.of(category));

        // When & Then
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.addCategory(categoryDto);
        });

        assertEquals("You already have this category", thrown.getMessage());
    }
}
