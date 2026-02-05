package com.ecommerce.order.services;

import com.ecommerce.order.clients.ProductServiceClient;
import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dtos.CartResult;
import com.ecommerce.order.dtos.ProductResponse;
import com.ecommerce.order.dtos.UserResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.dtos.CartItemRequest;
import com.ecommerce.order.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CartService {


    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;


    public CartResult addToCart(String userId, CartItemRequest request) {
        // Validate user exists
        UserResponse userResponse;
        try {
            userResponse = userServiceClient.getUserDetails(userId);
        } catch (WebClientResponseException e) {
            // User not found or other client error (4xx)
            return CartResult.failure("User not found");
        }

        if (userResponse == null) {
            return CartResult.failure("User not found");
        }

        // Validate product exists and is active
        ProductResponse productResponse;
        try {
            productResponse = productServiceClient.getProductDetails(request.getProductId());
        } catch (WebClientResponseException e) {
            // Product not found or other client error (4xx)
            return CartResult.failure("Product not found");
        }

        if(productResponse == null) {
            return CartResult.failure("Product not found");
        }

        if(!productResponse.getActive()) {
            return CartResult.failure("Product is not available");
        }

        if(productResponse.getStockQuantity() < request.getQuantity()) {
            return CartResult.failure("Insufficient stock. Available: " + productResponse.getStockQuantity());
        }
//
//        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
//
//        if(userOpt.isEmpty())
//            return false;
//
//        User user = userOpt.get();

        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId ,request.getProductId());

        if(existingCartItem != null){
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(existingCartItem);
        }else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(1000.00));
            cartItemRepository.save(cartItem);


        }
        return CartResult.success();

    }

    public boolean deleteItemFromCart(String userId, String productId) {
       CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if(cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }


    public List<CartItem> getCart(String userId) {
        return  cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
