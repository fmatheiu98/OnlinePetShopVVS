package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.Order;
import com.example.vvs.webapp.repository.OrderRepo;
import com.example.vvs.webapp.web.dto.OrderDto;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImplementation implements OrderService{

    private OrderRepo orderRepository;

    public OrderServiceImplementation(OrderRepo orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order save(OrderDto orderDto) {
        Order order = new Order(orderDto.getFirst_name(),
                orderDto.getLast_name(),
                orderDto.getEmail(),
                orderDto.getCountry(),
                orderDto.getAddress());
        return orderRepository.save(order);
    }
}
