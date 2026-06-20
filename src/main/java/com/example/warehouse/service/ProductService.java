package com.example.warehouse.service;

import com.example.warehouse.model.Category;
import com.example.warehouse.model.Product;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final WarehouseStore store;
    private final CategoryService categoryService;
    public ProductService(WarehouseStore store, CategoryService categoryService) { this.store = store; this.categoryService = categoryService; }
    public List<Product> getAll() { return store.products(); }
    public Product getById(Long id) { Product product = store.products.get(id); if (product == null) throw new IllegalArgumentException("Product not found: " + id); return product; }
    public List<Product> searchByName(String name) { return store.products().stream().filter(product -> product.getName().toLowerCase().contains(name.toLowerCase())).toList(); }
    public List<Product> getByCategory(Long categoryId) { return store.products().stream().filter(product -> product.getCategory() != null && product.getCategory().getId().equals(categoryId)).toList(); }
    public synchronized Product create(Product product, Long categoryId) { product.setId(store.productIds.incrementAndGet()); product.setCategory(categoryId == null ? null : categoryService.getById(categoryId)); store.products.put(product.getId(), product); return product; }
    public synchronized Product update(Long id, Product updated, Long categoryId) { Product current = getById(id); current.setName(updated.getName()); current.setDescription(updated.getDescription()); current.setPrice(updated.getPrice()); current.setQuantity(updated.getQuantity()); current.setCategory(categoryId == null ? null : categoryService.getById(categoryId)); return current; }
    public synchronized void delete(Long id) { getById(id); if (store.movements().stream().anyMatch(movement -> movement.getProduct().getId().equals(id))) throw new IllegalStateException("A product with stock movements cannot be deleted"); store.products.remove(id); }
}
