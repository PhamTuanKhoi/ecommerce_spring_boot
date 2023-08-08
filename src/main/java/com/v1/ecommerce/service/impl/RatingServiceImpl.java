package com.v1.ecommerce.service.impl;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Product;
import com.v1.ecommerce.model.Rating;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.repository.RatingRepository;
import com.v1.ecommerce.request.RatingRequest;
import com.v1.ecommerce.service.ProductService;
import com.v1.ecommerce.service.RatingService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {
    private RatingRepository ratingRepository;
    private ProductService productService;

    public RatingServiceImpl(RatingRepository ratingRepository, ProductService productService) {
        this.ratingRepository = ratingRepository;
        this.productService = productService;
    }

    @Override
    public Rating create(RatingRequest req, User user) throws ProductException {
        Product product = this.productService.findById(req.getProductId());

        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(req.getRating());
        rating.setCreatedAt(LocalDateTime.now());

        return this.ratingRepository.save(rating);
    }

    @Override
    public List<Rating> findRatingByProductId(Long productId) {
        return this.ratingRepository.findRatingByProductId(productId);
    }
}
