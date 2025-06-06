package com.example.bookshop.category.controller;

import com.example.bookshop.category.dto.CategoryDto;
import com.example.bookshop.category.dto.CreateCategoryDto;
import com.example.bookshop.category.service.CategoryService;
import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<CreateCategoryDto.Response>> createCategory(@RequestBody @Valid CreateCategoryDto.Request request) {

        ResultDto<CreateCategoryDto.Response> category = categoryService.createCategory(request);

        return ResponseEntity.ok(category);
    }

    @GetMapping("/categories")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<List<CategoryDto>>> getCategoryTree() {


        ResultDto<List<CategoryDto>> categoryTree = categoryService.getCategoryTree();

        return ResponseEntity.ok(categoryTree);

    }
}
