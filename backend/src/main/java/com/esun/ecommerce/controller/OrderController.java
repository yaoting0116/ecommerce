package com.esun.ecommerce.controller;

import com.esun.ecommerce.dto.OrderRequestDTO;
import com.esun.ecommerce.entity.OrderDetail;
import com.esun.ecommerce.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public List<OrderDetail> create(@RequestBody OrderRequestDTO request) {
        return service.createOrder(request);
    }

    @GetMapping("/test")
    public String test() {
        return "Order API is working";
    }
}
