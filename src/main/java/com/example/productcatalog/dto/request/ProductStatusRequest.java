package com.example.productcatalog.dto.request;

import com.example.productcatalog.entity.ProductStatus;
import jakarta.validation.constraints.NotNull;

public class ProductStatusRequest {
    @NotNull
    private ProductStatus status;

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }
}
