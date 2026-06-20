package com.example.warehouse.service;

import com.example.warehouse.model.Category;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {
    private final WarehouseStore store;
    public CategoryService(WarehouseStore store) { this.store = store; }
    public List<Category> getAll() { return store.categories(); }
    public Category getById(Long id) { Category category = store.categories.get(id); if (category == null) throw new IllegalArgumentException("Category not found: " + id); return category; }
    public synchronized Category create(Category category) {
        if (store.categories().stream().anyMatch(item -> item.getName().equalsIgnoreCase(category.getName()))) throw new IllegalArgumentException("A category with this name already exists");
        category.setId(store.categoryIds.incrementAndGet()); store.categories.put(category.getId(), category); return category;
    }
    public synchronized Category update(Long id, Category updated) { Category existing = getById(id); existing.setName(updated.getName()); existing.setDescription(updated.getDescription()); return existing; }
    public synchronized void delete(Long id) { getById(id); if (store.products().stream().anyMatch(product -> product.getCategory() != null && product.getCategory().getId().equals(id))) throw new IllegalStateException("A category with products cannot be deleted"); store.categories.remove(id); }
}
