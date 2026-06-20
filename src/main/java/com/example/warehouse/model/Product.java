package com.example.warehouse.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public class Product {
    private Long id;
    @NotBlank(message = "Product name is required") private String name;
    private String description;
    @NotNull(message = "Price is required") @DecimalMin(value = "0.0", message = "Price cannot be negative") private BigDecimal price;
    @PositiveOrZero(message = "Quantity cannot be negative") private int quantity;
    private Category category;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal price) { this.price = price; }
    public int getQuantity() { return quantity; } public void setQuantity(int quantity) { this.quantity = quantity; }
    public Category getCategory() { return category; } public void setCategory(Category category) { this.category = category; }
}
