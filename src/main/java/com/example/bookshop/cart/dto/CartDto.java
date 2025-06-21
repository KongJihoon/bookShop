package com.example.bookshop.cart.dto;


import com.example.bookshop.cart.entity.CartEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Getter
@Builder
public class CartDto {


    private String username;

    private List<CartItemDto> cartItems;


    public static CartDto fromEntity(CartEntity cartEntity) {
        return CartDto.builder()
                .username(cartEntity.getUserEntity().getUsername())
                .cartItems(cartEntity.getCartItems().stream()
                        .map(CartItemDto::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }



}
