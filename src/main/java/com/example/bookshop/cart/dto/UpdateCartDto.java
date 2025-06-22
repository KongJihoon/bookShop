package com.example.bookshop.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateCartDto {

    private Long bookId;

    private int quantity;
}
