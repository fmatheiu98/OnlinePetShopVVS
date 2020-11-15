package com.example.vvs.webapp.service;

import com.example.vvs.webapp.model.Order;
import com.example.vvs.webapp.repository.OrderRepo;
import com.example.vvs.webapp.web.dto.OrderDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
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

    @BeforeEach
    private void beforeEach()
    {
        orderRepo.deleteAll();
    }

    @AfterEach
    private void afterAll()
    {
        orderRepo.deleteAll();
    }

    @Test
    void saveOrderInDatabase_OK() {
        Order order = new Order();
        order.setCountry("RO");
        when(orderRepo.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderServiceImplementation.save(dto);

        verify(orderRepo).save(any(Order.class));
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getCountry()).isEqualTo("RO");
    }
}