package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.Order;
import com.example.vvs.webapp.model.Product;
import com.example.vvs.webapp.web.dto.OrderDto;

import java.util.List;

public interface OrderService {
    Order save(OrderDto orderDto);
}
