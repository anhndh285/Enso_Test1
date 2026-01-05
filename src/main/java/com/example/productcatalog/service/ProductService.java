package com.example.productcatalog.service;

import com.example.productcatalog.dto.request.InventoryRequest;
import com.example.productcatalog.dto.request.ProductRequest;
import com.example.productcatalog.dto.request.ProductStatusRequest;
import com.example.productcatalog.dto.response.InventoryResponse;
import com.example.productcatalog.dto.response.ProductResponse;
import com.example.productcatalog.entity.Category;
import com.example.productcatalog.entity.Product;
import com.example.productcatalog.entity.ProductStatus;
import com.example.productcatalog.exception.NotFoundException;
import com.example.productcatalog.repository.CategoryRepository;
import com.example.productcatalog.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ===== CRUD PRODUCT =====

    @Transactional
    public ProductResponse create(ProductRequest req) {
        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found: " + req.getCategoryId()));

        Product p = new Product();
        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setCategory(category);

        if (req.getStockQuantity() != null) {
            if (req.getStockQuantity() < 0) throw new IllegalArgumentException("Stock quantity cannot be negative");
            p.setStockQuantity(req.getStockQuantity());
            p.setLastStockUpdatedAt(LocalDateTime.now());
        }

        return toResponse(productRepository.save(p));
    }

    public ProductResponse getById(Integer id) {
        Product p = productRepository.findByIdWithCategory(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));
        return toResponse(p);
    }

    @Transactional
    public ProductResponse update(Integer id, ProductRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));

        ensureProductIsUsableForNewOperations(p);

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found: " + req.getCategoryId()));

        p.setName(req.getName());
        p.setPrice(req.getPrice());
        p.setCategory(category);

        if (req.getStockQuantity() != null) {
            if (req.getStockQuantity() < 0) throw new IllegalArgumentException("Stock quantity cannot be negative");
            p.setStockQuantity(req.getStockQuantity());
            p.setLastStockUpdatedAt(LocalDateTime.now());
        }

        return toResponse(productRepository.save(p));
    }


    @Transactional
    public void delete(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }

    // ===== MODULE 1: INVENTORY =====
    public InventoryResponse getInventory(Integer productId) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));
        return new InventoryResponse(p);
    }

    @Transactional
    public InventoryResponse updateInventory(Integer productId, InventoryRequest req) {
        Product p = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found: " + productId));

        ensureProductIsUsableForNewOperations(p);

        int newQty = req.getNewQuantity();
        if (newQty < 0) throw new IllegalArgumentException("Stock quantity cannot be negative");

        p.setStockQuantity(newQty);
        p.setLastStockUpdatedAt(LocalDateTime.now());
        productRepository.save(p);

        return new InventoryResponse(p);
    }


    // ===== MODULE 2: STATUS =====
    @Transactional
    public ProductResponse updateStatus(Integer id, ProductStatusRequest req) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));

        p.setStatus(req.getStatus());
        return toResponse(productRepository.save(p));
    }

    private void ensureProductIsUsableForNewOperations(Product p) {
        if (p.getStatus() == ProductStatus.PAUSED) {
            throw new IllegalArgumentException("Product is PAUSED. New operations are not allowed.");
        }
        if (p.getStatus() == ProductStatus.DISCONTINUED) {
            throw new IllegalArgumentException("Product is DISCONTINUED. New operations are not allowed.");
        }
    }


    // ===== MODULE 3: SEARCH + FILTER (keyword, category, price, status) =====
    public Page<ProductResponse> search(
            String keyword,
            Integer categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            ProductStatus status,
            boolean includeDiscontinued,
            Pageable pageable
    ) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("minPrice must be <= maxPrice");
        }
        return productRepository.search(keyword, categoryId, minPrice, maxPrice, status, includeDiscontinued, pageable)
                .map(this::toResponse);
    }

    // ===== Mapper =====
    private ProductResponse toResponse(Product p) {
        ProductResponse r = new ProductResponse(p);
        r.setId(p.getId());
        r.setName(p.getName());
        r.setPrice(p.getPrice());

        r.setStockQuantity(p.getStockQuantity());
        r.setLastStockUpdatedAt(p.getLastStockUpdatedAt());
        r.setStatus(p.getStatus());

        r.setCategoryId(p.getCategory().getId());
        r.setCategoryName(p.getCategory().getName());
        return r;
    }
}


