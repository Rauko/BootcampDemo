package com.example.warehouse.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class StockMovement {
    private Long id;
    @NotNull(message = "Movement type is required") private MovementType type;
    @Positive(message = "Amount must be greater than zero") private int amount;
    private String reason;
    private LocalDateTime createdAt;
    private Product product;
    public enum MovementType { INCOME, OUTCOME }
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public MovementType getType() { return type; } public void setType(MovementType type) { this.type = type; }
    public int getAmount() { return amount; } public void setAmount(int amount) { this.amount = amount; }
    public String getReason() { return reason; } public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getCreatedAt() { return createdAt; } public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public Product getProduct() { return product; } public void setProduct(Product product) { this.product = product; }
}
