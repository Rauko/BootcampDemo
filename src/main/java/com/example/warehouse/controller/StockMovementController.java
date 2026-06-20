package com.example.warehouse.controller;

import com.example.warehouse.model.StockMovement;
import com.example.warehouse.service.StockMovementService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movements")
public class StockMovementController {

    private final StockMovementService movementService;

    public StockMovementController(StockMovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping
    public List<StockMovement> getAll() {
        return movementService.getAll();
    }

    @GetMapping("/product/{productId}")
    public List<StockMovement> getByProduct(@PathVariable Long productId) {
        return movementService.getByProduct(productId);
    }

    @PostMapping("/product/{productId}")
    public StockMovement register(@PathVariable Long productId,
                                  @Valid @RequestBody StockMovement movement) {
        return movementService.register(productId, movement);
    }
}
