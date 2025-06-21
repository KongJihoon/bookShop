package com.example.bookshop.cart.controller;


import com.example.bookshop.cart.dto.AddCartDto;
import com.example.bookshop.cart.dto.CartDto;
import com.example.bookshop.cart.service.CartService;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;


    @PostMapping()
    public ResponseEntity<ResultDto<AddCartDto.Response>> addCartItem(
            @RequestBody AddCartDto.Request request,
            @AuthenticationPrincipal UserEntity userEntity
    ) {

        ResultDto<AddCartDto.Response> resultDto = cartService.addCartItem(userEntity.getUserId(), request);

        return ResponseEntity.ok(resultDto);
    }


    @GetMapping()
    public ResponseEntity<ResultDto<CartDto>> getCartInfo(
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        ResultDto<CartDto> resultDto = cartService.getCartInfo(userEntity.getUserId());

        return ResponseEntity.ok(resultDto);
    }

}
