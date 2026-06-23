package com.example.warehouse.service;

import com.example.warehouse.model.Category;
import com.example.warehouse.repository.CategoryRepository;
import com.example.warehouse.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + id));
    }

    public Category create(Category category) {
        if (categoryRepository.existsByNameIgnoreCase(category.getName()))
            throw new IllegalArgumentException("A category with this name already exists");
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category updated) {
        Category current = getById(id);
        if (!current.getName().equalsIgnoreCase(updated.getName()) &&
                categoryRepository.existsByNameIgnoreCase(updated.getName()))
            throw new IllegalArgumentException("A category with this name already exists");

        current.setName(updated.getName()); current.setDescription(updated.getDescription());
        return categoryRepository.save(current);
    }

    public void delete(Long id) {
        getById(id);

        if (productRepository.existsByCategoryId(id))
            throw new IllegalStateException("A category with products cannot be deleted");

        categoryRepository.deleteById(id);
    }
}
