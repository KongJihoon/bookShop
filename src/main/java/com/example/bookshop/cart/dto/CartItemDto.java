package com.example.bookshop.cart.dto;


import com.example.bookshop.cart.entity.CartEntity;
import com.example.bookshop.cart.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CartItemDto {

    private String title;

    private int quantity;

    private int unitPrice;

    private int totalPrice;

    public static CartItemDto fromEntity(CartItem cartItem) {

        int unitPrice = cartItem.getBookEntity().getPrice();

        int totalPrice = cartItem.getQuantity() * unitPrice;


        return CartItemDto.builder()
                .title(cartItem.getBookEntity().getTitle())
                .quantity(cartItem.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .build();

    }



}
