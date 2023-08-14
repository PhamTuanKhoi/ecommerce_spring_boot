package com.v1.ecommerce.service.impl;

import com.v1.ecommerce.exception.OrderException;
import com.v1.ecommerce.model.*;
import com.v1.ecommerce.model.enums.OrderStatusEnum;
import com.v1.ecommerce.model.enums.PaymentStatusEnum;
import com.v1.ecommerce.repository.AddressRepository;
import com.v1.ecommerce.repository.OrderItemRepository;
import com.v1.ecommerce.repository.OrderRepository;
import com.v1.ecommerce.repository.UserRepository;
import com.v1.ecommerce.service.CartService;
import com.v1.ecommerce.service.OrderItemService;
import com.v1.ecommerce.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private CartService cartService;
    private AddressRepository addressRepository;
    private OrderItemService orderItemService;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CartService cartService, AddressRepository addressRepository, OrderItemService orderItemService, OrderItemRepository orderItemRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.orderItemService = orderItemService;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Order create(User user, Address shippingAddress) {
        shippingAddress.setUser(user);
        Address address = this.addressRepository.save(shippingAddress);
        user.getAddress().add(address);
        this.userRepository.save(user);

        Cart cart = this.cartService.findCartByUserId(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();

        for (CartItem item : cart.getCartItems()){
            OrderItem orderItem = new OrderItem();
            modelMapper.map(item, orderItem);

            OrderItem created_orderItem = this.orderItemService.create(orderItem);

            orderItems.add(created_orderItem);
        }

        Order order = new Order();
        modelMapper.map(cart, order);
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setShippingAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatusEnum.PENDING.name());
//        TODO: seen db handler.
        order.getPaymentDetails().setStatus(OrderStatusEnum.PENDING.name());
        order.setCreateAt(LocalDateTime.now());

        Order created_order = this.orderRepository.save(order);

        for (OrderItem item: orderItems){
            item.setOrder(created_order);
            this.orderItemService.create(item);
        }

        return created_order;
    }

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order findById(Long id) throws OrderException {
        Optional<Order> order = this.orderRepository.findById(id);

        if(order == null || order.isEmpty()){
            throw new OrderException("order not found by id# " + id);
        }

        return order.get();
    }

    @Override
    public List<Order> findUserOrderHistory(Long userId) {
        System.out.println("userId = " + userId);
        List<Order> orders = this.orderRepository.findOrderByUserId(userId);
        return orders;
    }

    @Override
    public Order placedOrder(Long id) throws OrderException {
        Order order = this.findById(id);
        order.setOrderStatus(OrderStatusEnum.PLACED.name());
        order.getPaymentDetails().setStatus(PaymentStatusEnum.COMPLETED.name());
        return order;
    }

    @Override
    public Order confirmedOrder(Long id) throws OrderException {
        Order order = this.findById(id);
        order.setOrderStatus(OrderStatusEnum.CONFIRMED.name());
        return this.orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long id) throws OrderException {
        Order order = this.findById(id);
        order.setOrderStatus(OrderStatusEnum.SHIPPED.name());
        return this.orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long id) throws OrderException {
        Order order = this.findById(id);
        order.setOrderStatus(OrderStatusEnum.DELIVERED.name());
        return this.orderRepository.save(order);
    }

    @Override
    public Order cancledOrder(Long id) throws OrderException {
        Order order = this.findById(id);
        order.setOrderStatus(OrderStatusEnum.CANCELLED.name());
        return this.orderRepository.save(order);
    }

    @Override
    public void delete(Long orderId) throws OrderException {
        Order order = this.findById(orderId);
        this.orderRepository.delete(order);
    }
}
