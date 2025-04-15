package com.example.SE2_Project.Controller;
import com.example.SE2_Project.Controller.pages.CategoryController;
import com.example.SE2_Project.Dto.CategoryDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    public void testGetAllCategories() throws Exception {
        Set<CategoryEntity> categories = new HashSet<>();
        categories.add(new CategoryEntity()); // Add a sample category

        when(categoryService.getCategoriesForCurrentUser()).thenReturn(categories);

        mockMvc.perform(get("/category/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("userCategory/categoryPage"))
                .andExpect(model().attribute("categories", categories));
    }

    @Test
    public void testShowEditForm() throws Exception {
        Long categoryId = 1L;
        CategoryEntity category = new CategoryEntity();
        category.setId(categoryId);

        when(categoryService.getCategoryById(categoryId)).thenReturn(category);

        mockMvc.perform(get("/category/edit/{id}", categoryId))
                .andExpect(status().isOk())
                .andExpect(view().name("userCategory/update"))
                .andExpect(model().attribute("category", category));
    }

    @Test
    public void testUpdateCategory() throws Exception {
        Long categoryId = 1L;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Updated Category");

        doNothing().when(categoryService).updateCategory(categoryId, categoryDto);

        mockMvc.perform(post("/category/update/{id}", categoryId)
                        .flashAttr("category", categoryDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/category/all"));
    }

    @Test
    public void testDeleteCategory() throws Exception {
        Long categoryId = 1L;
        doNothing().when(categoryService).deleteCategory(categoryId);

        mockMvc.perform(post("/category/delete/{id}", categoryId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/category/all"));
    }

    @Test
    public void testShowAddCategoryForm() throws Exception {
        mockMvc.perform(get("/category/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("userCategory/addCategory"))
                .andExpect(model().attributeExists("category"));
    }

    @Test
    public void testAddCategory() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("New Category");

        doNothing().when(categoryService).addCategory(any(CategoryDto.class));

        mockMvc.perform(post("/category/add")
                        .flashAttr("category", categoryDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/addNew"));
    }

    @Test
    public void testAddCategoryWithError() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Invalid Category");

        // Simulate a failure in the service
        doThrow(new IllegalArgumentException("Category name is invalid")).when(categoryService).addCategory(categoryDto);

        mockMvc.perform(post("/category/add")
                        .flashAttr("category", categoryDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transactions/addNew"))
                .andExpect(flash().attribute("errorMessage", "Category name is invalid"));
    }
}