package com.example.bookshop.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CreateCategoryDto {

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Request {

        private Long parentId;

        @NotBlank(message = "카테고리 이름을 입력해주세요.")
        private String categoryName;

    }


    @AllArgsConstructor
    @Getter
    @Builder
    public static class Response {

        private Long categoryId;

        private String categoryName;

        private Long parentId;

        private int depth;

        private List<Response> child = new ArrayList<>();


        public static Response fromDto(CategoryDto categoryDto) {

            return Response.builder()
                    .categoryId(categoryDto.getCategoryId())
                    .categoryName(categoryDto.getCategoryName())
                    .parentId(categoryDto.getParentId())
                    .depth(categoryDto.getDepth())
                    .child(categoryDto.getChildren()
                            .stream()
                            .map(Response::fromDto)
                            .collect(Collectors.toList()))
                    .build();


        }




    }


}
