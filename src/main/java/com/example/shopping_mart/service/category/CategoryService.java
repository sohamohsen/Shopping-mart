package com.example.shopping_mart.service.category;

import com.example.shopping_mart.exceptions.AlreadyExistsException;
import com.example.shopping_mart.exceptions.CategoryNotFoundException;
import com.example.shopping_mart.model.Category;
import com.example.shopping_mart.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new AlreadyExistsException(String.format("Category '%s' already exists", category.getName()));
        }
        return categoryRepository.save(category);

    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(Category category, long id) {
        Category existingCategory = getCategoryById(id); // Throws if not found

        existingCategory.setName(category.getName()); // Modify the entity

        return existingCategory; // No need to explicitly save in a @Transactional method
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public void deleteCategory(long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with ID " + id + " not found"));
        categoryRepository.delete(category); // Deletes the category
    }

}