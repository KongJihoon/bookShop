package com.example.bookshop.category.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;

    private int depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryEntity> children = new ArrayList<>();

    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductCategory> productCategories = new ArrayList<>();


    /**
     * 카테고리 생성자 부모가 없을 시 최상위 부모 설정
     * 부모 존재 시 부모의 depth에서 1증가
     */
    public CategoryEntity(String categoryName, CategoryEntity parent) {

        this.categoryName = categoryName;
        this.parent = parent;
        this.depth = parent == null? 1: parent.getDepth() + 1;

    }

    public void addChild(CategoryEntity child) {
        children.add(child);
        child.parent = this;
        child.depth = depth + 1;
    }



}
