package com.v1.ecommerce.service;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Cart;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.request.AddItemRequest;

public interface CartService {
    public Cart create(User user);
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException;
    public Cart findCartByUserId(Long userId);
}
