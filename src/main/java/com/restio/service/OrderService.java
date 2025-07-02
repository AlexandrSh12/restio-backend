package com.restio.service;

import com.restio.dto.OrderDTO;
import com.restio.dto.OrderItemDTO;
import com.restio.model.*;
import com.restio.repository.DishRepository;
import com.restio.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final ShiftService shiftService;

    public OrderService(OrderRepository orderRepository, DishRepository dishRepository, ShiftService shiftService) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.shiftService = shiftService;
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<OrderDTO> getOrderById(String id) {
        return orderRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Order order = convertToEntity(orderDTO);

        // Устанавливаем статус по умолчанию
        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus(OrderStatus.SUBMITTED);
        }

        // Получаем активную смену и следующий номер заказа
        Shift activeShift = shiftService.getOrCreateActiveShift();
        Integer orderNumber = shiftService.getNextOrderNumber();

        order.setShift(activeShift);
        order.setOrderNumber(orderNumber);

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Transactional
    public Optional<OrderDTO> updateOrderStatus(String id, String status) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return Optional.of(convertToDTO(updatedOrder));
    }

    @Transactional
    public Optional<OrderDTO> updateOrderComment(String id, String comment) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();
        order.setComment(comment);
        Order updatedOrder = orderRepository.save(order);
        return Optional.of(convertToDTO(updatedOrder));
    }

    // Преобразование Entity в DTO
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setShiftId(order.getShift() != null ? order.getShift().getId() : null);
        dto.setStatus(order.getStatus());
        dto.setComment(order.getComment());

        if (order.getItems() != null) {
            List<OrderItemDTO> itemDTOs = order.getItems().stream()
                    .map(item -> {
                        OrderItemDTO itemDTO = new OrderItemDTO();
                        itemDTO.setId(item.getId());

                        if (item.getDish() != null) {
                            itemDTO.setDishId(item.getDish().getId());
                            itemDTO.setDishName(item.getDish().getName());
                            itemDTO.setPrice(item.getDish().getPrice());
                        }

                        itemDTO.setCount(item.getCount());
                        return itemDTO;
                    })
                    .collect(Collectors.toList());

            dto.setItems(itemDTOs);
        } else {
            dto.setItems(new ArrayList<>());
        }

        return dto;
    }

    // Преобразование DTO в Entity
    private Order convertToEntity(OrderDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setStatus(dto.getStatus());
        order.setComment(dto.getComment());

        if (dto.getItems() != null) {
            List<OrderItem> items = dto.getItems().stream()
                    .map(itemDTO -> {
                        OrderItem item = new OrderItem();
                        //if (itemDTO.getId() != null) {
                            //item.setId(itemDTO.getId());
                        //}

                        item.setCount(itemDTO.getCount());
                        System.out.println("itemDTO.getDishId(): " + itemDTO.getDishId());
                        if (itemDTO.getDishId() != null) {
                            // Получаем информацию о блюде из репозитория
                            Optional<Dish> dishOpt = dishRepository.findById(itemDTO.getDishId());
                            dishOpt.ifPresent(item::setDish);
                        }

                        return item;
                    })
                    .collect(Collectors.toList());

            order.setItems(items);
        } else {
            order.setItems(new ArrayList<>());
        }

        return order;
    }
}