package com.v1.ecommerce.repository;

import com.v1.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    //    TODO: space p + " " because query is string
    @Query("SELECT p FROM #{#entityName} p " +
            "WHERE " +
            "(COALESCE(:category, '', NULL) = '' OR p.category.name =:category ) " +
            "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice))" +
            "AND (:minDiscount IS NULL OR p.discountPercent >= :minDiscount)" +
            "ORDER BY " +
            "CASE WHEN :sort='price_low' THEN p.discountedPrice END ASC, " +
            "CASE WHEN :sort='price_high' THEN p.discountedPrice END DESC"
    )
    List<Product> filterProducts(
            @Param("category") String category,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("minDiscount") Integer minDiscount,
            @Param("sort") String sort
//            ,
//            @Param("stock") String stock,
//            @Param("colors") List<String> colors, @Param("sizes") List<String> sizes,
//            @Param("pageNumber") Integer pageNumber, @Param("pageSize") Integer pageSize
    );

    @Query("SELECT p FROM #{#entityName} p WHERE " +
            "(TIMESTAMPDIFF(second, p.createdAt, :currentTime) * 1000) < :aboutNew")
    List<Product> findNewArrivals(
            @Param("currentTime") LocalDateTime currentTime,
            @Param("aboutNew") int aboutNew);

    @Query("SELECT p FROM #{#entityName} p WHERE p.category.id =:categoryId")
    Product findByCategoryId(Long categoryId);
}
