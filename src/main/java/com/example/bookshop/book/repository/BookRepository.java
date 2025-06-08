package com.example.bookshop.book.repository;

import com.example.bookshop.book.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<BookEntity, Long> {

    boolean existsByTitle(String title);

}
