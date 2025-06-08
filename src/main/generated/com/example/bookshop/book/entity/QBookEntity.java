package com.example.bookshop.book.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookEntity is a Querydsl query type for BookEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookEntity extends EntityPathBase<BookEntity> {

    private static final long serialVersionUID = -1816431376L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookEntity bookEntity = new QBookEntity("bookEntity");

    public final com.example.bookshop.global.entity.QBaseEntity _super = new com.example.bookshop.global.entity.QBaseEntity(this);

    public final StringPath author = createString("author");

    public final ListPath<com.example.bookshop.category.entity.BookCategory, com.example.bookshop.category.entity.QBookCategory> bookCategories = this.<com.example.bookshop.category.entity.BookCategory, com.example.bookshop.category.entity.QBookCategory>createList("bookCategories", com.example.bookshop.category.entity.BookCategory.class, com.example.bookshop.category.entity.QBookCategory.class, PathInits.DIRECT2);

    public final NumberPath<Long> bookId = createNumber("bookId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath details = createString("details");

    public final ListPath<BookImageEntity, QBookImageEntity> imagesPath = this.<BookImageEntity, QBookImageEntity>createList("imagesPath", BookImageEntity.class, QBookImageEntity.class, PathInits.DIRECT2);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final StringPath publisher = createString("publisher");

    public final NumberPath<Integer> quantity = createNumber("quantity", Integer.class);

    public final StringPath thumbnailImagePath = createString("thumbnailImagePath");

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.example.bookshop.user.entity.QUserEntity userEntity;

    public QBookEntity(String variable) {
        this(BookEntity.class, forVariable(variable), INITS);
    }

    public QBookEntity(Path<? extends BookEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookEntity(PathMetadata metadata, PathInits inits) {
        this(BookEntity.class, metadata, inits);
    }

    public QBookEntity(Class<? extends BookEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.userEntity = inits.isInitialized("userEntity") ? new com.example.bookshop.user.entity.QUserEntity(forProperty("userEntity")) : null;
    }

}

