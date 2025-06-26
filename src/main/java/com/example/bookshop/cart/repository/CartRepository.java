package com.example.bookshop.cart.repository;

import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.cart.entity.CartEntity;
import com.example.bookshop.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {


    Optional<CartEntity> findByUserEntity(UserEntity userEntity);

    @Query("""
        select c
        from CartEntity c
        join fetch c.cartItems cartItems
        join fetch cartItems.bookEntity bookEntity
        where c.userEntity = :user
""")
    Optional<CartEntity> findByUserWithItemsAndBooks(@Param("user") UserEntity userEntity);




}
