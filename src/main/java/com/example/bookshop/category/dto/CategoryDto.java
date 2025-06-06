package com.example.bookshop.category.dto;


import com.example.bookshop.category.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {


    private Long categoryId;

    private String categoryName;

    private int depth;

    private Long parentId;

    private List<CategoryDto> children;

    public static CategoryDto fromEntity(CategoryEntity category) {

        List<CategoryDto> childrenDto = category.getChildren().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());

        return CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .parentId(category.getParent() != null ? category.getParent().getCategoryId() : null)
                .depth(category.getDepth())
                .children(childrenDto)
                .build();

    }

}
