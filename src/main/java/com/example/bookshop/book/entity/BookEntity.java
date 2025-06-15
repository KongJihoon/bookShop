package com.example.bookshop.book.entity;


import com.example.bookshop.book.type.BookStatus;
import com.example.bookshop.category.entity.BookCategory;
import com.example.bookshop.global.entity.BaseEntity;
import com.example.bookshop.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private String publisher;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer quantity;


    private String thumbnailImagePath;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookImageEntity> imagesPath;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "bookEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    List<BookCategory> bookCategories;




}
