package com.example.bookshop.cart.service;

import com.example.bookshop.cart.dto.AddCartDto;
import com.example.bookshop.cart.dto.CartDto;
import com.example.bookshop.cart.dto.UpdateCartDto;
import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;

public interface CartService {


    ResultDto<AddCartDto.Response> addCartItem(Long userId, AddCartDto.Request request);


    ResultDto<CartDto> getCartInfo(Long userId);


    ResultDto<CartDto> updateCartItem(Long userId, UpdateCartDto request);

    CheckDto deleteCartItem(Long userId, Long cartItemId);

    CheckDto deleteAllCartItems(Long userId, Long cartId);
}
