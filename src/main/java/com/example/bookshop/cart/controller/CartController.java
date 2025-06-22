package com.example.bookshop.cart.controller;


import com.example.bookshop.cart.dto.AddCartDto;
import com.example.bookshop.cart.dto.CartDto;
import com.example.bookshop.cart.dto.UpdateCartDto;
import com.example.bookshop.cart.service.CartService;
import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;


    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<AddCartDto.Response>> addCartItem(
            @RequestBody AddCartDto.Request request,
            @AuthenticationPrincipal UserEntity userEntity
    ) {

        ResultDto<AddCartDto.Response> resultDto = cartService.addCartItem(userEntity.getUserId(), request);

        return ResponseEntity.ok(resultDto);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<CartDto>> getCartInfo(
            @AuthenticationPrincipal UserEntity userEntity
    ) {
        ResultDto<CartDto> resultDto = cartService.getCartInfo(userEntity.getUserId());

        return ResponseEntity.ok(resultDto);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<CartDto>> updateCartItem(
            @RequestBody UpdateCartDto request,
            @AuthenticationPrincipal UserEntity userEntity
    ) {

        ResultDto<CartDto> cartDtoResultDto = cartService.updateCartItem(userEntity.getUserId(), request);

        return ResponseEntity.ok(cartDtoResultDto);
    }


    @PatchMapping
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CheckDto> deleteCartItem(
            @RequestParam Long cartItemId,
            @AuthenticationPrincipal UserEntity userEntity
    ) {

        CheckDto checkDto = cartService.deleteCartItem(userEntity.getUserId(), cartItemId);

        return ResponseEntity.ok(checkDto);
    }

    @PatchMapping("/delete-all")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CheckDto> deleteAllCartItems(
            @RequestParam Long cartId,
            @AuthenticationPrincipal UserEntity userEntity
    ) {

        CheckDto checkDto = cartService.deleteAllCartItems(userEntity.getUserId(), cartId);

        return ResponseEntity.ok(checkDto);
    }

}
