package com.v1.ecommerce.service;

import com.v1.ecommerce.exception.CartItemException;
import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.Cart;
import com.v1.ecommerce.model.CartItem;
import com.v1.ecommerce.model.Product;

public interface CartItemService {
    public CartItem create(CartItem cartItem);
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
    public CartItem findById(Long id) throws CartItemException;
}
