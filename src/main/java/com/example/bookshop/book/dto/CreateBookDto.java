package com.example.bookshop.book.dto;

import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.book.entity.BookImageEntity;
import com.example.bookshop.user.entity.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class CreateBookDto {


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "책 이름을 입력해주세요.")
        private String title;

        @NotBlank(message = "저자를 입력해주세요.")
        private String author;

        @NotBlank(message = "출판사를 입력해주세요.")
        private String publisher;

        @NotBlank(message = "상세설명을 입력해주세요.")
        private String details;

        @NotNull(message = "가격을 입력해주세요.")
        @Positive(message = "가격은 0보다 커야 합니다.")
        private Integer price;

        @NotNull(message = "수량을 입력해주세요.")
        @Positive(message = "수량은 0보다 커야 합니다.")
        private Integer quantity;


        private String thumbnailImagePath;

        private List<BookImageDto> imagesPath;

        private List<Long> categoryIds;


        public static BookEntity toEntity(Request request) {



            BookEntity bookEntity = BookEntity.builder()
                    .title(request.getTitle())
                    .author(request.getAuthor())
                    .publisher(request.getPublisher())
                    .details(request.getDetails())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .thumbnailImagePath(request.getThumbnailImagePath())
                    .build();

            List<BookImageEntity> imagesPaths = request.getImagesPath().stream()
                    .map(dto -> BookImageDto.toEntity(dto, bookEntity))
                    .collect(Collectors.toList());

            bookEntity.setImagesPath(imagesPaths);

            return bookEntity;

        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {

        private String title;

        private String author;

        private String publisher;

        private String details;

        private Integer price;

        private Integer quantity;

        private String thumbnailImagePath;

        private List<BookImageDto> imagePath;

        private List<Long> categoryIds;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        public static Response fromDto(BookDto bookDto) {




            return Response.builder()
                    .title(bookDto.getTitle())
                    .author(bookDto.getAuthor())
                    .publisher(bookDto.getPublisher())
                    .details(bookDto.getDetails())
                    .price(bookDto.getPrice())
                    .quantity(bookDto.getQuantity())
                    .thumbnailImagePath(bookDto.getThumbnailImagePath())
                    .imagePath(bookDto.getImagesPath())
                    .categoryIds(bookDto.getCategoryIds())
                    .createdAt(bookDto.getCreatedAt())
                    .updatedAt(bookDto.getUpdatedAt())
                    .build();
        }

    }

}
