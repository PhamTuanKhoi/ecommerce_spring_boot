package com.v1.ecommerce.service.impl;

import com.v1.ecommerce.exception.CartItemException;
import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.Cart;
import com.v1.ecommerce.model.CartItem;
import com.v1.ecommerce.model.Product;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.repository.CartItemRepository;
import com.v1.ecommerce.repository.CartRepository;
import com.v1.ecommerce.service.CartItemService;
import com.v1.ecommerce.service.UserService;

import java.util.Optional;

public class CartItemServiceImpl implements CartItemService {
    private CartItemRepository cartItemRepository;
    private CartRepository cartRepository;
    private UserService userService;

    public CartItemServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository, UserService userService) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    @Override
    public CartItem create(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

        return this.cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = this.findById(id);
        User user = this.userService.findUserById(item.getUserId());

        if(user.getId().equals(userId)){
            item.setQuantity(cartItem.getQuantity());
//          TODO: check item.getQuantity()
            item.setPrice(item.getProduct().getPrice() * item.getQuantity());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice() * item.getQuantity());
        }
        return this.cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        CartItem cartItem = this.cartItemRepository.isCartItemExist(cart, product, size, userId);
        return cartItem;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem cartItem = this.findById(cartItemId);
        User userCI = this.userService.findUserById(cartItem.getUserId());
        User user = this.userService.findUserById(userId);

        if(!userCI.getId().equals(user.getId())){
            throw new UserException("User can't remove another users item");
        }

        this.cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartItem findById(Long id) throws CartItemException {
        Optional<CartItem> cartItem = this.cartItemRepository.findById(id);

        if(cartItem == null){
            throw new CartItemException("CartItem not found by id# " + id);
        }
        return cartItem.get();
    }
}
