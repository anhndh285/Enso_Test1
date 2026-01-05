package com.example.productcatalog.dto.response;

import com.example.productcatalog.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InventoryResponse {
    private Integer productId;
    private Integer stockQuantity;
    private LocalDateTime lastStockUpdatedAt;

    public InventoryResponse(Product p) {
        this.productId = p.getId();
        this.stockQuantity = p.getStockQuantity();
        this.lastStockUpdatedAt = p.getLastStockUpdatedAt();
    }
}
