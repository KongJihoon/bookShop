package com.example.bookshop.cart.entity;


import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class CartEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @Builder
    public CartEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }


    public void addItemOrUpdate(BookEntity bookEntity, int quantity) {


        for (CartItem item : cartItems) {

            if (item.getBookEntity().getBookId().equals(bookEntity.getBookId())) {

                item.addQuantity(quantity);
                return;

            }

        }

        CartItem item = CartItem.builder()
                .bookEntity(bookEntity)
                .quantity(quantity)
                .build();

        item.setCart(this);
        cartItems.add(item);


    }

    public void removeCartItem(Long cartItemId) {

        boolean removed = cartItems.removeIf(item -> item.getCartItemId().equals(cartItemId));

        if (!removed) {
            throw new CustomException(ErrorCode.NOT_FOUND_CART_ITEM);
        }

    }

}
