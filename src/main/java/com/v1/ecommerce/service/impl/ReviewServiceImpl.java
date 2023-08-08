package com.v1.ecommerce.service.impl;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Product;
import com.v1.ecommerce.model.Review;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.repository.ReviewRepository;
import com.v1.ecommerce.request.ReviewRequest;
import com.v1.ecommerce.service.ProductService;
import com.v1.ecommerce.service.ReviewService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    private ProductService productService;
    @Override
    public Review create(ReviewRequest req, User user) throws ProductException {
        Product product = this.productService.findById(req.getProductId());

        Review review = new Review();
        review.setProduct(product);
        review.setUser(user);
        review.setReview(req.getReview());
        review.setCreatedAt(LocalDateTime.now());

        return this.reviewRepository.save(review);
    }

    @Override
    public List<Review> findReviewByProductId(Long productId) {
        return this.reviewRepository.findReviewByProductId(productId);
    }
}
