package com.example.bookshop.category.service.impl;

import com.example.bookshop.category.dto.CategoryDto;
import com.example.bookshop.category.dto.CreateCategoryDto;
import com.example.bookshop.category.entity.CategoryEntity;
import com.example.bookshop.category.repository.CategoryRepository;
import com.example.bookshop.category.service.CategoryService;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    /**
     * 도서 카테고리 생성 API
     */
    @Override
    @Transactional
    public ResultDto<CreateCategoryDto.Response> createCategory(CreateCategoryDto.Request request) {

        log.info("[카테고리 생성 시작] : {}", request.getCategoryName());

        CategoryEntity parent = null;

        // 요청에 parentId가 있으면 해당 카테고리를 부모로 설정
        if (request.getParentId() != null) {

            parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY));

        }

        // 같은 이름과 부모를 가진 카테고리가 이미 존재하면 예외 발생
        if (categoryRepository.existsByCategoryNameAndParent(request.getCategoryName(), parent)) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_CATEGORY);
        }

        /*
         * 자식 카테고리 생성 부모 객체와 연결
         */
        CategoryEntity categoryEntity = new CategoryEntity(request.getCategoryName(), parent);

        categoryRepository.save(categoryEntity);

        log.info("[카테고리 생성 완료] : {}", request.getCategoryName());

        return ResultDto.of("카테고리 생성 완료", CreateCategoryDto.Response
                .fromDto(CategoryDto.fromEntity(categoryEntity)));
    }

    @Override
    public ResultDto<List<CategoryDto>> getCategoryTree() {

        /*
         * 부모객체가 없는 최상위 부모를 탐색 후 자식 객체와 함께 리스트로 반환
         */
        List<CategoryDto> categoryLists = categoryRepository.findByParentIsNull().stream()
                .map(CategoryDto::fromEntity)
                .collect(Collectors.toList());

        return ResultDto.of("카테고리 목록 조회 완료", categoryLists);
    }


}
