package com.example.shopping_mart.service.category;

import com.example.shopping_mart.model.Category;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    Category getCategoryById(long id);
    Optional<Category> getCategoryByName(String name);
    List<Category> getAllCategories();
    Category addCategory(Category category);
    Category updateCategory(Category category, long id);
    void deleteCategory(long id);
}
