package com.v1.ecommerce.controller;

import com.v1.ecommerce.exception.CartItemException;
import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.service.CartItemService;
import com.v1.ecommerce.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_item")
public class CartItemController {
    private final CartItemService cartItemService;
    private final UserService userService;

    public CartItemController(CartItemService cartItemService, UserService userService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    @DeleteMapping("/{id}")
    public String removeCartItem(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @PathVariable("id") Long cartItemId) throws CartItemException, UserException {
        User user = this.userService.findUserProviderByJwt(jwt);
        this.cartItemService.removeCartItem(user.getId(), cartItemId);

        return "Deleted cart item success by id# " + cartItemId;
    }


}
