package org.example.service;

import org.example.entityes.Order;
import org.example.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public void save(Order order) {
        orderRepository.save(order);
    }

    public Order findById(long id) {
        return orderRepository.findById(id);
    }

    public void delete(long id) {
        orderRepository.deleteById(id);
    }
}
