package com.example.warehouse.service;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.StockMovement;
import com.example.warehouse.repository.ProductRepository;
import com.example.warehouse.repository.StockMovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockMovementService {
    private final StockMovementRepository movementRepository;
    private final ProductRepository productRepository;
    public StockMovementService(StockMovementRepository movementRepository, ProductRepository productRepository) { this.movementRepository = movementRepository; this.productRepository = productRepository; }
    public List<StockMovement> getAll() { return movementRepository.findAll(); }
    public List<StockMovement> getByProduct(Long productId) { return movementRepository.findByProductIdOrderByCreatedAtDesc(productId); }
    @Transactional
    public StockMovement register(Long productId, StockMovement movement) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        int newQuantity = movement.getType() == StockMovement.MovementType.INCOME ? product.getQuantity() + movement.getAmount() : product.getQuantity() - movement.getAmount();
        if (newQuantity < 0) throw new IllegalStateException("Insufficient stock. Available: " + product.getQuantity());
        product.setQuantity(newQuantity);
        movement.setProduct(product);
        movement.setCreatedAt(LocalDateTime.now());
        productRepository.save(product);
        return movementRepository.save(movement);
    }
}
