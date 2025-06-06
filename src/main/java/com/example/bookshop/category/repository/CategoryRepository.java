package com.example.bookshop.category.repository;

import com.example.bookshop.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findByParentIsNull();



    boolean existsByCategoryNameAndParent(String categoryName, CategoryEntity parent);


}
