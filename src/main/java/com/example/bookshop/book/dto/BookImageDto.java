package com.example.bookshop.book.dto;

import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.book.entity.BookImageEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookImageDto {

    private String imagePath;

    private int sortOrder;

    public static BookImageEntity toEntity(BookImageDto dto, BookEntity book) {
        return BookImageEntity.builder()
                .imagePath(dto.getImagePath())
                .sortOrder(dto.getSortOrder())
                .book(book)
                .build();
    }

    public static BookImageDto fromEntity(BookImageEntity entity) {
        return BookImageDto.builder()
                .imagePath(entity.getImagePath())
                .sortOrder(entity.getSortOrder())
                .build();
    }
}
