package com.example.warehouse.service;

import com.example.warehouse.model.Category;
import com.example.warehouse.model.Product;
import com.example.warehouse.repository.ProductRepository;
import com.example.warehouse.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final StockMovementRepository movementRepository;
    private final CategoryService categoryService;
    public ProductService(ProductRepository productRepository, StockMovementRepository movementRepository, CategoryService categoryService) { this.productRepository = productRepository; this.movementRepository = movementRepository; this.categoryService = categoryService; }
    public List<Product> getAll() { return productRepository.findAll(); }
    public Product getById(Long id) { return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: " + id)); }
    public List<Product> searchByName(String name) { return productRepository.findByNameContainingIgnoreCase(name); }
    public List<Product> getByCategory(Long categoryId) { return productRepository.findByCategoryId(categoryId); }
    public Product create(Product product, Long categoryId) { product.setCategory(findCategory(categoryId)); return productRepository.save(product); }
    public Product update(Long id, Product updated, Long categoryId) { Product current = getById(id); current.setName(updated.getName()); current.setDescription(updated.getDescription()); current.setPrice(updated.getPrice()); current.setQuantity(updated.getQuantity()); current.setCategory(findCategory(categoryId)); return productRepository.save(current); }
    public void delete(Long id) { getById(id); if (movementRepository.existsByProductId(id)) throw new IllegalStateException("A product with stock movements cannot be deleted"); productRepository.deleteById(id); }
    private Category findCategory(Long categoryId) { return categoryId == null ? null : categoryService.getById(categoryId); }
}
