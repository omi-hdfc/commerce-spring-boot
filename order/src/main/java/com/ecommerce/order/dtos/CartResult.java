package com.ecommerce.order.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CartResult {
    private boolean success;
    private String message;

    public static CartResult success() {
        return new CartResult(true, "Item added to cart successfully");
    }

    public static CartResult failure(String message) {
        return new CartResult(false, message);
    }
}
