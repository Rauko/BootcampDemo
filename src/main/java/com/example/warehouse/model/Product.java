package com.example.warehouse.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Product name is required") @Column(nullable = false) private String name;
    private String description;
    @NotNull(message = "Price is required") @DecimalMin(value = "0.0", message = "Price cannot be negative") @Column(nullable = false, precision = 19, scale = 2) private BigDecimal price;
    @PositiveOrZero(message = "Quantity cannot be negative") @Column(nullable = false) private int quantity;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal price) { this.price = price; }
    public int getQuantity() { return quantity; } public void setQuantity(int quantity) { this.quantity = quantity; }
    public Category getCategory() { return category; } public void setCategory(Category category) { this.category = category; }
}
