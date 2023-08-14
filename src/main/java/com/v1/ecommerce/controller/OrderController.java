package com.v1.ecommerce.controller;

import com.v1.ecommerce.exception.OrderException;
import com.v1.ecommerce.exception.UserException;
import com.v1.ecommerce.model.Address;
import com.v1.ecommerce.model.Order;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.service.OrderService;
import com.v1.ecommerce.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    private OrderService orderService;
    private UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    public Order create(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt,
            @RequestBody Address shippingAddress) throws UserException {
        User user = this.userService.findUserProviderByJwt(jwt);

        return this.orderService.create(user, shippingAddress);
    };

    @GetMapping
    public List<Order> findAll(){
        return this.orderService.findAll();
    };

    @GetMapping("/{id}")
    public Order findById(@PathVariable Long id) throws OrderException {
        return this.orderService.findById(id);
    };

    @GetMapping("/history")
    public List<Order> findUserOrderHistory(@RequestHeader(HttpHeaders.AUTHORIZATION) String jwt) throws UserException {
        User user = this.userService.findUserProviderByJwt(jwt);
        return this.orderService.findUserOrderHistory(user.getId());
    };
    public Order placedOrder(Long id) throws OrderException{
        return this.orderService.placedOrder(id);
    };
    public Order confirmedOrder(Long id) throws OrderException{
        return this.orderService.confirmedOrder(id);
    };
    public Order shippedOrder(Long id) throws OrderException{
        return  this.orderService.shippedOrder(id);
    };
    public Order deliveredOrder(Long id) throws OrderException {
        return this.orderService.deliveredOrder(id);
    };
    public Order cancledOrder(Long id) throws OrderException{
        return this.orderService.cancledOrder(id);
    };
    public String delete(Long id) throws OrderException {
        this.orderService.delete(id);
        return "Deleted order success by id#" + id;
    };
}
