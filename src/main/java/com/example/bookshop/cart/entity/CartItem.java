package com.example.bookshop.cart.entity;

import com.example.bookshop.book.entity.BookEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @Column(nullable = false)
    private int quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private BookEntity bookEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cartEntity;


    @Builder
    public CartItem(BookEntity bookEntity, int quantity, String title) {
        this.bookEntity = bookEntity;
        this.quantity = quantity;


    }

    public void setCart(CartEntity cartEntity) {
        this.cartEntity = cartEntity;
    }


    public void addQuantity(int quantity) {

        this.quantity += quantity;


    }

    public void updateQuantity(int newQuantity) {

        this.quantity = newQuantity;
    }

}
