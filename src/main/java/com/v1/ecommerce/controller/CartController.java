package com.v1.ecommerce.controller;

import com.v1.ecommerce.exception.ProductException;
import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.Cart;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.request.AddItemRequest;
import com.v1.ecommerce.service.CartService;
import com.v1.ecommerce.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cart")
public class CartController {
    private CartService cartService;
    private UserService userService;

    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/")
    public Cart findCartByUserId(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) throws UserException {
        User user = this.userService.findUserProviderByJwt(jwt);
        return cartService.findCartByUserId(user.getId());
    };

    @PostMapping("add-item")
    public String addCartItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @RequestBody AddItemRequest req)
            throws ProductException, UserException {
        User user = this.userService.findUserProviderByJwt(jwt);
        return this.cartService.addCartItem(user.getId(), req);
    };

    @PostMapping
    public Cart create(@RequestBody User user){
        return cartService.create(user);
    };
}
