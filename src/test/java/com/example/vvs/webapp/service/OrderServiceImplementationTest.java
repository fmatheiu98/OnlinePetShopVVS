package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.Order;
import com.example.vvs.webapp.model.User;
import com.example.vvs.webapp.repository.OrderRepo;
import com.example.vvs.webapp.repository.ProductRepo;
import com.example.vvs.webapp.repository.UserRepo;
import com.example.vvs.webapp.web.dto.OrderDto;
import com.example.vvs.webapp.web.dto.UserRegistrationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplementationTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private OrderDto dto;

    @InjectMocks
    private OrderServiceImplementation orderServiceImplementation;

    @Test
    void saveOrderInDatabase_OK() {
        Order order = new Order();
        when(orderRepo.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderServiceImplementation.save(dto);

        verify(orderRepo).save(any(Order.class));
        assertThat(savedOrder).isNotNull();
    }
}