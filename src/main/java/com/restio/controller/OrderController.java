package com.restio.controller;

import java.util.List;
import com.restio.model.Order;
import com.restio.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepo;

    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @PostMapping
    public Order submit(@RequestBody Order order) {
        order.setStatus("submitted");
        return orderRepo.save(order);
    }

    @GetMapping
    public List<Order> getAll() {
        return orderRepo.findAll();
    }
}