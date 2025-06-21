package com.example.bookshop.cart.service.impl;

import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.book.repository.BookRepository;
import com.example.bookshop.book.type.BookStatus;
import com.example.bookshop.cart.dto.AddCartDto;
import com.example.bookshop.cart.dto.CartDto;
import com.example.bookshop.cart.entity.CartEntity;
import com.example.bookshop.cart.repository.CartItemRepository;
import com.example.bookshop.cart.repository.CartRepository;
import com.example.bookshop.cart.service.CartService;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bookshop.global.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {


    private final CartRepository cartRepository;


    private final UserRepository userRepository;

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public ResultDto<AddCartDto.Response> addCartItem(Long userId, AddCartDto.Request request) {

        log.info("[장바구니 상품 등록 시작] userId={}, bookId={}", userId, request.getBookId());

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        BookEntity bookEntity = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));


        if (bookEntity.getBookStatus().equals(BookStatus.DELETED) || bookEntity.getQuantity() < request.getQuantity()) {
            throw new CustomException(CANNOT_ADD_TO_CART);
        }

        CartEntity newCart = cartRepository.findByUserEntity(userEntity)
                .orElseGet(
                        () -> {
                            CartEntity cartEntity = CartEntity.builder()
                                    .userEntity(userEntity)
                                    .build();
                            return cartRepository.save(cartEntity);
                        }
                );

        newCart.addItemOrUpdate(bookEntity, request.getQuantity());
        cartRepository.save(newCart);

        CartDto cartDto = CartDto.fromEntity(newCart);

        log.info("[장바구니 상품 담기 완료] userId={}, bookId={}", userId, request.getBookId());

        return ResultDto.of("장바구니 담기에 성공하였습니다.", AddCartDto.Response.fromDto(cartDto));
    }

    @Override
    public ResultDto<CartDto> getCartInfo(Long userId) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity)
                .orElseThrow(() -> new CustomException(CART_NOT_FOUND));

        CartDto cartDto = CartDto.fromEntity(cartEntity);


        return ResultDto.of("장바구니 조회 완료.", cartDto);
    }
}
