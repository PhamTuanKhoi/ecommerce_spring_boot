package com.v1.ecommerce.service;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Review;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.request.ReviewRequest;

import java.util.List;

public interface ReviewService {
    public Review create(ReviewRequest req, User user) throws ProductException;
    public List<Review> findReviewByProductId(Long productId);
}
