package com.example.bookshop.category.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookCategory is a Querydsl query type for BookCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookCategory extends EntityPathBase<BookCategory> {

    private static final long serialVersionUID = -1110657696L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookCategory bookCategory = new QBookCategory("bookCategory");

    public final com.example.bookshop.book.entity.QBookEntity bookEntity;

    public final QCategoryEntity categoryEntity;

    public final NumberPath<Long> productCategoryId = createNumber("productCategoryId", Long.class);

    public QBookCategory(String variable) {
        this(BookCategory.class, forVariable(variable), INITS);
    }

    public QBookCategory(Path<? extends BookCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookCategory(PathMetadata metadata, PathInits inits) {
        this(BookCategory.class, metadata, inits);
    }

    public QBookCategory(Class<? extends BookCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bookEntity = inits.isInitialized("bookEntity") ? new com.example.bookshop.book.entity.QBookEntity(forProperty("bookEntity"), inits.get("bookEntity")) : null;
        this.categoryEntity = inits.isInitialized("categoryEntity") ? new QCategoryEntity(forProperty("categoryEntity"), inits.get("categoryEntity")) : null;
    }

}

