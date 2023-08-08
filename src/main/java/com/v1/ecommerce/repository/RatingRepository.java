package com.v1.ecommerce.repository;

import com.v1.ecommerce.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("SELECT r FROM #{#entityName} r WHERE r.product.id =:product")
    public List<Rating> findRatingByProductId(@Param("product") Long productId);
}
