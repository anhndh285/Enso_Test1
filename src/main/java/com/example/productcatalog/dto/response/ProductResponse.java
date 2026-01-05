package com.example.productcatalog.dto.response;

import com.example.productcatalog.entity.Product;
import com.example.productcatalog.entity.ProductStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponse {
    private Integer id;
    private String name;
    private BigDecimal price;

    private Integer categoryId;
    private String categoryName;

    private Integer stockQuantity;
    private LocalDateTime lastStockUpdatedAt;

    private ProductStatus status;

    public ProductResponse(Product p) {
        this.id = p.getId();
        this.name = p.getName();
        this.price = p.getPrice();
        this.categoryId = p.getCategory().getId();
        this.categoryName = p.getCategory().getName();
        this.stockQuantity = p.getStockQuantity();
        this.lastStockUpdatedAt = p.getLastStockUpdatedAt();
        this.status = p.getStatus();
    }
}
