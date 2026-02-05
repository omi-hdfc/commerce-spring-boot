package com.ecommerce.order.controllers;


import com.ecommerce.order.dtos.CartResult;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.dtos.CartItemRequest;
import com.ecommerce.order.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-Id") String userId, @RequestBody CartItemRequest request) {
        CartResult result = cartService.addToCart(userId, request);
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result.getMessage());
    }

    @DeleteMapping("/item/{productId}")
    public ResponseEntity<Void> removeFromCart(@RequestHeader("X-User-ID") String userId, @PathVariable String productId){
        boolean deleted = cartService.deleteItemFromCart(userId,productId);
        return deleted ? ResponseEntity.noContent().build():ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(@RequestHeader("X-User-ID") String userId){
        return ResponseEntity.ok(cartService.getCart(userId));
    }
}
