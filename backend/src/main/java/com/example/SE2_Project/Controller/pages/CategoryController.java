package com.example.SE2_Project.Controller.pages;

import com.example.SE2_Project.Dto.CategoryDto;
import com.example.SE2_Project.Entity.CategoryEntity;
import com.example.SE2_Project.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/all")
    public String getAllCategories(Model model) {
        Set<CategoryEntity> categories = categoryService.getCategoriesForCurrentUser();
        model.addAttribute("categories", categories);
        return "userCategory/categoryPage";
    }
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CategoryEntity category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "userCategory/update";
    }
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Long id, @ModelAttribute CategoryDto categoryDto) {
        categoryService.updateCategory(id, categoryDto);
        return "redirect:/category/all";
    }
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/category/all";
    }
    @GetMapping("/add")
    public String showAddCategoryForm(Model model) {
        model.addAttribute("category", new CategoryDto());
        return "userCategory/addCategory";
    }

    @PostMapping("/add")
    public String addCategory(@ModelAttribute("category") CategoryDto categoryDto, RedirectAttributes redirectAttributes) {
        try {
            categoryService.addCategory(categoryDto);
            redirectAttributes.addFlashAttribute("successMessage", "Category added successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/transactions/addNew";
        }
        return "redirect:/transactions/addNew";
    }
}
