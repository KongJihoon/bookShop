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

    int totalPrice;


    public static CartDto fromEntity(CartEntity cartEntity) {

        List<CartItemDto> itemDtos = cartEntity.getCartItems().stream()
                .map(CartItemDto::fromEntity)
                .toList();

        int total = itemDtos.stream()
                .mapToInt(CartItemDto::getTotalPrice)
                .sum();

        return CartDto.builder()
                .username(cartEntity.getUserEntity().getUsername())
                .cartItems(itemDtos)
                .totalPrice(total)
                .build();
    }



}
