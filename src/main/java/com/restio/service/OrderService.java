package com.restio.service;

import com.restio.exception.ResourceNotFoundException;
import com.restio.model.Order;
import com.restio.model.OrderItem;
import com.restio.repository.DishRepository;
import com.restio.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, DishRepository dishRepository) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public Order createOrder(Order order) {
        // Генерируем UUID если не указан
        if (order.getId() == null || order.getId().isEmpty()) {
            order.setId(UUID.randomUUID().toString());
        }

        // Проверяем наличие блюд
        if (order.getItems() != null) {
            for (OrderItem item : order.getItems()) {
                dishRepository.findById(item.getDish().getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Dish", "id", item.getDish().getId()));
            }
        }

        // Устанавливаем начальный статус
        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("draft");
        }

        return orderRepository.save(order);
    }

    public Order updateOrderStatus(String id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public void deleteOrder(String id) {
        Order order = getOrderById(id);
        orderRepository.delete(order);
    }
}