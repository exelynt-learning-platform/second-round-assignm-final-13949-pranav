package com.ecommerce.controller;

import com.ecommerce.config.UserPrincipal;
import com.ecommerce.dto.CartItemDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<Cart> getCart(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Cart cart = cartService.getCartByUserId(userPrincipal.getId());
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addItemToCart(@Valid @RequestBody CartItemDTO cartItemDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Cart cart = cartService.addItemToCart(userPrincipal.getId(), cartItemDTO.getProductId(), cartItemDTO.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateCartItem(@Valid @RequestBody CartItemDTO cartItemDTO, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Cart cart = cartService.updateCartItem(userPrincipal.getId(), cartItemDTO.getProductId(), cartItemDTO.getQuantity());
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Cart> removeItemFromCart(@PathVariable Long productId, Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Cart cart = cartService.removeItemFromCart(userPrincipal.getId(), productId);
        return ResponseEntity.ok(cart);
    }
}