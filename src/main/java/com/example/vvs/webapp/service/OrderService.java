package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.Order;
import com.example.vvs.webapp.web.dto.OrderDto;


public interface OrderService {
    Order save(OrderDto orderDto);
}
