package com.ecommerce.service;

import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderStatus;
import com.ecommerce.repository.OrderRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public String createPaymentIntent(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        Stripe.apiKey = stripeApiKey;

        long amount = order.getTotalPrice().multiply(BigDecimal.valueOf(100)).longValue(); // Convert to cents

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount)
                .setCurrency("usd")
                .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            order.setPaymentIntentId(paymentIntent.getId());
            orderRepository.save(order);
            return paymentIntent.getClientSecret();
        } catch (Exception e) {
            throw new RuntimeException("Payment creation failed: " + e.getMessage());
        }
    }

    public void confirmPayment(String paymentIntentId) {
        Order order = orderRepository.findAll().stream()
                .filter(o -> paymentIntentId.equals(o.getPaymentIntentId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Order not found for payment"));

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }
}