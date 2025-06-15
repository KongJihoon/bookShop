package com.example.bookshop.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class UpdateBookDto {

    private Long bookId;

    private String title;

    private String author;

    private String details;

    private String publisher;

    private Integer price;

    private Integer quantity;

    private List<Long> deleteImageIds;

}
