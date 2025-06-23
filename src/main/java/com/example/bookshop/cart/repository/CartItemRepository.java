package com.example.bookshop.cart.repository;

import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.cart.entity.CartEntity;
import com.example.bookshop.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByCartEntityAndBookEntity(CartEntity cartEntity, BookEntity bookEntity);
}
