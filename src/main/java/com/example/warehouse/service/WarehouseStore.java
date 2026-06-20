package com.example.warehouse.service;

import com.example.warehouse.model.Category;
import com.example.warehouse.model.Product;
import com.example.warehouse.model.StockMovement;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class WarehouseStore {
    final Map<Long, Category> categories = new LinkedHashMap<>();
    final Map<Long, Product> products = new LinkedHashMap<>();
    final Map<Long, StockMovement> movements = new LinkedHashMap<>();
    final AtomicLong categoryIds = new AtomicLong();
    final AtomicLong productIds = new AtomicLong();
    final AtomicLong movementIds = new AtomicLong();

    synchronized List<Category> categories() { return new ArrayList<>(categories.values()); }
    synchronized List<Product> products() { return new ArrayList<>(products.values()); }
    synchronized List<StockMovement> movements() { return new ArrayList<>(movements.values()); }
}
