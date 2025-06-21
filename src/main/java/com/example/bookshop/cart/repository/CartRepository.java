package com.example.bookshop.cart.repository;

import com.example.bookshop.cart.entity.CartEntity;
import com.example.bookshop.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserEntity(UserEntity userEntity);

}
