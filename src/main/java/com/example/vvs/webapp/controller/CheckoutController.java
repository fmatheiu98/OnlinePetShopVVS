package com.example.vvs.webapp.controller;

import com.example.vvs.webapp.service.OrderService;
import com.example.vvs.webapp.web.dto.OrderDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final OrderService orderService;

    public CheckoutController(OrderService orderService) {
        this.orderService = orderService;
    }

    @ModelAttribute("order")
    public OrderDto userCheckoutDto()
    {
        return new OrderDto();
    }

    @GetMapping
    public String showOrderForm()
    {
        return "checkout";
    }

    @PostMapping
    public String registerOrder(@ModelAttribute("order") OrderDto orderDto)
    {
        if(orderDto == null)
        {
            throw new IllegalArgumentException();
        }
        orderService.save(orderDto);
        return "redirect:/checkout?success";
    }

}
