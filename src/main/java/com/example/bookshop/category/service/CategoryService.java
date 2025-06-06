package com.example.bookshop.category.service;

import com.example.bookshop.category.dto.CategoryDto;
import com.example.bookshop.category.dto.CreateCategoryDto;
import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;

import java.util.List;

public interface CategoryService {


    ResultDto<CreateCategoryDto.Response> createCategory(CreateCategoryDto.Request createCategoryDto);

    ResultDto<List<CategoryDto>> getCategoryTree();


}
