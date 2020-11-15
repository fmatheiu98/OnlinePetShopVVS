package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.model.Order;
import com.example.vvs.webapp.service.OrderService;
import com.example.vvs.webapp.web.dto.OrderDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
class CheckoutControllerTest {

    @Mock
    private OrderDto orderDto;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CheckoutController checkoutController;

    @Autowired
    MockMvc mockMvc;

    @Test
    void usercheckoutDtoWorksAsExpected() {
        assertThat(checkoutController.userCheckoutDto()).isInstanceOf(OrderDto.class);
    }

    @Test
    void showOrderFormWorkAsExpected() {
        assertThat(checkoutController.showOrderForm()).isEqualTo("checkout");
    }

    @Test
    void saveNewOrderThenReturnTheCorrectLink() {
        Order order = new Order();
        when(orderService.save(any(OrderDto.class))).thenReturn(order);

        String result = checkoutController.registerOrder(orderDto);

        verify(orderService).save(any(OrderDto.class));
        assertThat(result).isEqualTo("redirect:/checkout?success");
    }

    @Test
    void saveNewOrderWithNullDTO() {
        assertThrows(IllegalArgumentException.class,()-> checkoutController.registerOrder(null));
    }

    @Test
    @DisplayName("Get to Checkout Page -> Page responds with 200 and contains expected text")
    public void whenGetToCheckoutPage_then200WithContent() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/checkout")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("checkout"))
                .andExpect(content().string(containsString("Place Order")));
    }

    @Test
    public void whenGetToCheckout_FillInInfoThenRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/checkout")
                .param("first_name","George")
                .param("last_name","Daniel")
                .param("email","test")
                .param("country","test_country")
                .param("address","testAddress"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/checkout?success"));
    }
}