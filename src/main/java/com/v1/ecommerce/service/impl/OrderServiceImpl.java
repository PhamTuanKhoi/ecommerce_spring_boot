package com.v1.ecommerce.service.impl;

import com.v1.ecommerce.exception.OrderException;
import com.v1.ecommerce.model.Address;
import com.v1.ecommerce.model.Order;
import com.v1.ecommerce.model.User;
import com.v1.ecommerce.service.OrderService;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    @Override
    public Order create(User userReq, Address shippingAddress) {
        return null;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order findById(Long id) throws OrderException {
        return null;
    }

    @Override
    public List<Order> findUserOrderHistory(Long userId) {
        return null;
    }

    @Override
    public Order placedOrder(Long id) throws OrderException {
        return null;
    }

    @Override
    public Order confirmedOrder(Long id) throws OrderException {
        return null;
    }

    @Override
    public Order shippedOrder(Long id) throws OrderException {
        return null;
    }

    @Override
    public Order deliveredOrder(Long id) throws OrderException {
        return null;
    }

    @Override
    public Order cancledOrder(Long id) throws OrderException {
        return null;
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {

    }
}
