package com.example.shopping_mart.service.category;

import com.example.shopping_mart.exceptions.AlreadyExistsException;
import com.example.shopping_mart.exceptions.CategoryNotFoundException;
import com.example.shopping_mart.model.Category;
import com.example.shopping_mart.repository.CategoryRepository;
import com.example.shopping_mart.request.AddCategoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findByName(name));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(category1 -> !categoryRepository.existsByname(category1.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(() -> new AlreadyExistsException(category.getName()+"already exists"));
    }

    public Category createCategory(AddCategoryRequest request) {
        return new Category(
                request.getName()
        );
    }

    @Override
    public Category updateCategory(Category category, long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return categoryRepository.save(oldCategory);
                }).orElseThrow(() -> new CategoryNotFoundException("Category not Found!"));
    }

    private Category updateExistingCategory(Category existingCategory, Category updatedCategory) {
        existingCategory.setName(updatedCategory.getName());
        return existingCategory;
    }


    @Override
    public void deleteCategory(long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new CategoryNotFoundException("Category not found");
                });
    }
}
