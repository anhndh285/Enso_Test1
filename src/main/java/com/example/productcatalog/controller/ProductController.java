package com.example.productcatalog.controller;

import com.example.productcatalog.dto.request.InventoryRequest;
import com.example.productcatalog.dto.request.ProductRequest;
import com.example.productcatalog.dto.request.ProductStatusRequest;
import com.example.productcatalog.dto.response.InventoryResponse;
import com.example.productcatalog.dto.response.ProductResponse;
import com.example.productcatalog.entity.ProductStatus;
import com.example.productcatalog.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // POST /api/products
    @PostMapping
    public ProductResponse create(@RequestBody @Valid ProductRequest req) {
        return productService.create(req);
    }

    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Integer id) {
        return productService.getById(id);
    }

    // PUT /api/products/{id}
    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Integer id, @RequestBody @Valid ProductRequest req) {
        return productService.update(id, req);
    }

    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productService.delete(id);
    }

    // ===== MODULE 3: SEARCH/FILTER + PAGINATION + SORT =====
    // GET /api/products?keyword=&categoryId=&minPrice=&maxPrice=&status=&includeDiscontinued=&page=&size=&sort=
    @GetMapping
    public Page<ProductResponse> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "false") boolean includeDiscontinued,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        Pageable pageable = buildPageable(page, size, sort);
        return productService.search(keyword, categoryId, minPrice, maxPrice, status, includeDiscontinued, pageable);
    }

    // ===== MODULE 1: INVENTORY =====
    // GET /api/products/{id}/inventory
    @GetMapping("/{id}/inventory")
    public InventoryResponse getInventory(@PathVariable Integer id) {
        return productService.getInventory(id);
    }

    // PUT /api/products/{id}/inventory
    @PutMapping("/{id}/inventory")
    public InventoryResponse updateInventory(@PathVariable Integer id, @RequestBody @Valid InventoryRequest req) {
        return productService.updateInventory(id, req);
    }

    // ===== MODULE 2: STATUS =====
    // PUT /api/products/{id}/status
    @PutMapping("/{id}/status")
    public ProductResponse updateStatus(@PathVariable Integer id, @RequestBody @Valid ProductStatusRequest req) {
        return productService.updateStatus(id, req);
    }

    private Pageable buildPageable(int page, int size, String sort) {
        String[] parts = sort.split(",");
        String field = parts[0];
        Sort.Direction dir = (parts.length > 1 && parts[1].equalsIgnoreCase("asc"))
                ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(dir, field));
    }
}



