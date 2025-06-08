package com.example.bookshop.book.dto;


import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.book.entity.BookImageEntity;
import com.example.bookshop.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDto {

    private Long bookId;

    private String title;

    private String author;

    private String publisher;

    private String details;

    private Integer price;

    private Integer quantity;

    private String thumbnailImagePath;

    private List<BookImageDto> imagesPath;

    private Long userId;

    private List<Long> categoryIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static BookDto fromEntity(BookEntity bookEntity) {


        List<Long> categoryIds = bookEntity.getBookCategories().stream()
                .map(bookCategory -> bookCategory.getCategoryEntity()
                        .getCategoryId())
                .toList();

        List<BookImageDto> imagePaths = bookEntity.getImagesPath().stream()
                .map(BookImageDto::fromEntity)
                .collect(Collectors.toList());


        return BookDto.builder()
                .bookId(bookEntity.getBookId())
                .title(bookEntity.getTitle())
                .author(bookEntity.getAuthor())
                .publisher(bookEntity.getPublisher())
                .details(bookEntity.getDetails())
                .price(bookEntity.getPrice())
                .quantity(bookEntity.getQuantity())
                .thumbnailImagePath(bookEntity.getThumbnailImagePath())
                .imagesPath(imagePaths)
                .userId(bookEntity.getUserEntity().getUserId())
                .categoryIds(categoryIds)
                .createdAt(bookEntity.getCreatedAt())
                .updatedAt(bookEntity.getUpdatedAt())
                .build();
    }


}
