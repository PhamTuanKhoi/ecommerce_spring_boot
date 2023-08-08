package com.v1.ecommerce.service;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Rating;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.request.RatingRequest;

import java.util.List;

public interface RatingService {
    public Rating create(RatingRequest req, User user) throws ProductException;
    public List<Rating> findRatingByProductId(Long productId);
}
