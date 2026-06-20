package com.example.warehouse.service;

import com.example.warehouse.model.Product;
import com.example.warehouse.model.StockMovement;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class StockMovementService {
    private final WarehouseStore store;
    private final ProductService productService;
    public StockMovementService(WarehouseStore store, ProductService productService) { this.store = store; this.productService = productService; }
    public List<StockMovement> getAll() { return store.movements(); }
    public List<StockMovement> getByProduct(Long productId) { return store.movements().stream().filter(movement -> movement.getProduct().getId().equals(productId)).sorted(Comparator.comparing(StockMovement::getCreatedAt).reversed()).toList(); }
    public synchronized StockMovement register(Long productId, StockMovement movement) {
        Product product = productService.getById(productId);
        int newQuantity = movement.getType() == StockMovement.MovementType.INCOME ? product.getQuantity() + movement.getAmount() : product.getQuantity() - movement.getAmount();
        if (newQuantity < 0) throw new IllegalStateException("Insufficient stock. Available: " + product.getQuantity());
        product.setQuantity(newQuantity); movement.setId(store.movementIds.incrementAndGet()); movement.setProduct(product); movement.setCreatedAt(LocalDateTime.now()); store.movements.put(movement.getId(), movement); return movement;
    }
}
