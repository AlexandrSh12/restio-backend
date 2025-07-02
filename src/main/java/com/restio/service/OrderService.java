package com.restio.service;

import com.restio.dto.OrderDTO;
import com.restio.dto.OrderItemDTO;
import com.restio.model.*;
import com.restio.repository.DishRepository;
import com.restio.repository.OrderRepository;
import com.restio.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ShiftService shiftService;

    public OrderService(OrderRepository orderRepository, DishRepository dishRepository,
                        UserRepository userRepository, ShiftService shiftService) {
        this.orderRepository = orderRepository;
        this.dishRepository = dishRepository;
        this.userRepository = userRepository;
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
        if (order.getStatus() == null) {
            order.setStatus(OrderStatus.DRAFT);
        }

        // Получаем активную смену и следующий номер заказа
        Shift activeShift = shiftService.getOrCreateActiveShift();
        Integer orderNumber = shiftService.getNextOrderNumber();

        order.setShift(activeShift);
        order.setOrderNumber(orderNumber);

        // Устанавливаем официанта (в реальном приложении получаем из Security Context)
        if (orderDTO.getWaiterId() != null) {
            Optional<User> waiterOpt = userRepository.findById(orderDTO.getWaiterId());
            waiterOpt.ifPresent(order::setWaiter);
        }

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Transactional
    public Optional<OrderDTO> updateOrderStatus(String id, String statusString) {
        Optional<Order> orderOpt = orderRepository.findById(id);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();

        // Преобразуем строку в enum
        try {
            OrderStatus status = OrderStatus.valueOf(statusString.toUpperCase());
            order.setStatus(status);
        } catch (IllegalArgumentException e) {
            // Обработка неверного статуса
            throw new IllegalArgumentException("Invalid order status: " + statusString);
        }

        Order updatedOrder = orderRepository.save(order);
        return Optional.of(convertToDTO(updatedOrder));
    }

    @Transactional
    public Optional<OrderDTO> updateOrderStatus(String id, OrderStatus status) {
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

    @Transactional
    public Optional<OrderDTO> updateOrderItemStatus(String orderId, Long itemId, String statusString) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            return Optional.empty();
        }

        Order order = orderOpt.get();
        Optional<OrderItem> itemOpt = order.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst();

        if (itemOpt.isEmpty()) {
            return Optional.empty();
        }

        OrderItem item = itemOpt.get();
        try {
            OrderItemStatus status = OrderItemStatus.valueOf(statusString.toUpperCase());
            item.setStatus(status);

            // Устанавливаем временные метки
            if (status == OrderItemStatus.PREPARING && item.getStartCookingAt() == null) {
                item.setStartCookingAt(java.time.LocalDateTime.now());
            } else if (status == OrderItemStatus.READY && item.getReadyAt() == null) {
                item.setReadyAt(java.time.LocalDateTime.now());
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order item status: " + statusString);
        }

        Order updatedOrder = orderRepository.save(order);
        return Optional.of(convertToDTO(updatedOrder));
    }

    // Преобразование Entity в DTO
    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setShiftId(order.getShift() != null ? order.getShift().getId() : null);
        dto.setStatus(order.getStatus() != null ? order.getStatus().name() : null);
        dto.setComment(order.getComment());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setSubmittedAt(order.getSubmittedAt());
        dto.setReadyAt(order.getReadyAt());
        dto.setDeliveredAt(order.getDeliveredAt());

        if (order.getWaiter() != null) {
            dto.setWaiterId(order.getWaiter().getId());
            dto.setWaiterName(order.getWaiter().getFullName());
        }

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
                        itemDTO.setStatus(item.getStatus() != null ? item.getStatus().name() : null);
                        itemDTO.setComment(item.getComment());
                        itemDTO.setStartCookingAt(item.getStartCookingAt());
                        itemDTO.setReadyAt(item.getReadyAt());

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

        // Преобразуем строковый статус в enum
        if (dto.getStatus() != null) {
            try {
                OrderStatus status = OrderStatus.valueOf(dto.getStatus().toUpperCase());
                order.setStatus(status);
            } catch (IllegalArgumentException e) {
                order.setStatus(OrderStatus.DRAFT); // значение по умолчанию
            }
        }

        order.setComment(dto.getComment());

        if (dto.getItems() != null) {
            List<OrderItem> items = dto.getItems().stream()
                    .map(itemDTO -> {
                        OrderItem item = new OrderItem();
                        item.setCount(itemDTO.getCount());
                        item.setComment(itemDTO.getComment());

                        // Устанавливаем статус позиции
                        if (itemDTO.getStatus() != null) {
                            try {
                                OrderItemStatus status = OrderItemStatus.valueOf(itemDTO.getStatus().toUpperCase());
                                item.setStatus(status);
                            } catch (IllegalArgumentException e) {
                                item.setStatus(OrderItemStatus.PENDING); // значение по умолчанию
                            }
                        }

                        if (itemDTO.getDishId() != null) {
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