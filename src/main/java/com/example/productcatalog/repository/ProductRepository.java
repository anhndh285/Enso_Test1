package com.example.productcatalog.repository;

import com.example.productcatalog.entity.Product;
import com.example.productcatalog.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("""
                SELECT p
                FROM Product p
                WHERE (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
                  AND (:categoryId IS NULL OR p.category.id = :categoryId)
                  AND (:minPrice IS NULL OR p.price >= :minPrice)
                  AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                  AND (:status IS NULL OR p.status = :status)
                  AND (:includeDiscontinued = true OR p.status <> com.example.productcatalog.entity.ProductStatus.DISCONTINUED)
            """)
    Page<Product> search(
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("status") ProductStatus status,
            @Param("includeDiscontinued") boolean includeDiscontinued,
            Pageable pageable
    );

    @Query("""
        SELECT p
        FROM Product p
        JOIN FETCH p.category
        WHERE p.id = :id
    """)
    Optional<Product> findByIdWithCategory(@Param("id") Integer id);
}
