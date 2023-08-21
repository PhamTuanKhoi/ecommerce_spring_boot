package com.v1.ecommerce.service.impl;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.model.Cart;
import com.v1.ecommerce.model.CartItem;
import com.v1.ecommerce.model.Product;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.repository.CartRepository;
import com.v1.ecommerce.request.AddItemRequest;
import com.v1.ecommerce.service.CartItemService;
import com.v1.ecommerce.service.CartService;
import com.v1.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CartItemService cartItemService;
    private ProductService productService;

    public CartServiceImpl(CartRepository cartRepository, CartItemService cartItemService, ProductService productService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
        this.productService = productService;
    }

    @Override
    public Cart create(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return this.cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest req) throws ProductException {
        Cart cart = this.cartRepository.findCartByUserId(userId);

        Product product = this.productService.findById(req.getProductId());

        CartItem isPresent = this.cartItemService.isCartItemExist(cart, product, req.getSize(), userId);
        if(isPresent == null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(req.getQuantity());
            cartItem.setUserId(userId);

            int price = product.getDiscountedPrice() * req.getQuantity();
            cartItem.setPrice(price);
            cartItem.setSize(req.getSize());

            System.out.println("cartItem =============================== " + cartItem.getQuantity());

            CartItem saved = this.cartItemService.create(cartItem);
            cart.getCartItems().add(saved);
        }
        return "Item add to cart";
    }

    @Override
    public Cart findCartByUserId(Long userId) {
        Cart cart = this.cartRepository.findCartByUserId(userId);

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()){
            totalPrice = totalPrice + cartItem.getPrice();
            totalDiscountedPrice = totalDiscountedPrice + cartItem.getDiscountedPrice();
            totalItem = totalItem + cartItem.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setDiscounted(totalPrice - totalDiscountedPrice);

//        TODO: why save?
        return cartRepository.save(cart);
    }
}
