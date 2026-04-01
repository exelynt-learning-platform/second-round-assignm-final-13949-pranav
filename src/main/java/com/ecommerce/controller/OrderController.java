package com.ecommerce.controller;

import com.ecommerce.config.UserPrincipal;
import com.ecommerce.dto.OrderRequest;
import com.ecommerce.entity.Order;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest orderRequest, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Order order = orderService.createOrderFromCart(userPrincipal.getId(), orderRequest.getShippingAddress());
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getUserOrders(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        List<Order> orders = orderService.getOrdersByUserId(userPrincipal.getId());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Order order = orderService.getOrderById(id);
        if (!order.getUser().getId().equals(userPrincipal.getId())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/{id}/payment")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@PathVariable Long id, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Order order = orderService.getOrderById(id);
        if (!order.getUser().getId().equals(userPrincipal.getId())) {
            return ResponseEntity.status(403).build();
        }
        String clientSecret = paymentService.createPaymentIntent(id);
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<?> confirmPayment(@RequestBody Map<String, String> payload) {
        String paymentIntentId = payload.get("paymentIntentId");
        paymentService.confirmPayment(paymentIntentId);
        return ResponseEntity.ok("Payment confirmed");
    }
}