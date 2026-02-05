package com.ecommerce.order.services;


import com.ecommerce.order.clients.UserServiceClient;
import com.ecommerce.order.dtos.OrderItemDTO;
import com.ecommerce.order.dtos.OrderResponse;
import com.ecommerce.order.dtos.UserResponse;
import com.ecommerce.order.models.CartItem;
import com.ecommerce.order.models.Order;
import com.ecommerce.order.models.OrderItem;
import com.ecommerce.order.models.OrderStatus;
import com.ecommerce.order.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final UserServiceClient userServiceClient;
    private final OrderRepository orderRepository;
    public Optional<OrderResponse> createOrder(String userId) {
        //Validate for cart items
        List<CartItem> cartItems = cartService.getCart(userId);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }

        //validate user exists
        UserResponse userResponse;
        try {
            userResponse = userServiceClient.getUserDetails(userId);
        } catch (WebClientResponseException e) {
            // User not found or other client error (4xx)
            return Optional.empty();
        }

        if (userResponse == null) {
            return Optional.empty();
        }

       //calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        //
        cartService.clearCart(userId);

        return Optional.of(mapToOrderResponse(savedOrder));

    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getItems() == null ? List.of() :
                        order.getItems().stream()
                                .map(orderItem -> {
                                    BigDecimal price = orderItem.getPrice() == null
                                            ? BigDecimal.ZERO
                                            : orderItem.getPrice();

                                    return new OrderItemDTO(
                                            orderItem.getId(),
                                            orderItem.getProductId(),
                                            orderItem.getQuantity(),
                                            price,
                                            price.multiply(BigDecimal.valueOf(orderItem.getQuantity()))
                                    );
                                })
                                .toList(),
                order.getCreatedAt()
        );
    }

}
