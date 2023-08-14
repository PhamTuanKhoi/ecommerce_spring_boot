package com.v1.ecommerce.service;

import com.v1.ecommerce.exception.OrderException;
import com.v1.ecommerce.model.Address;
import com.v1.ecommerce.model.Order;
import com.v1.ecommerce.model.User;

import java.util.List;

public interface OrderService {
    public Order create(User user, Address shippingAddress);

    public List<Order> findAll();

    public Order findById(Long id) throws OrderException;
    public List<Order> findUserOrderHistory(Long userId);
    public Order placedOrder(Long id) throws OrderException;
    public Order confirmedOrder(Long id) throws OrderException;
    public Order shippedOrder(Long id) throws OrderException;
    public Order deliveredOrder(Long id) throws OrderException;
    public Order cancledOrder(Long id) throws OrderException;

    public void delete(Long id) throws OrderException;
}
