package com.example.bookshop.book.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookImageEntity is a Querydsl query type for BookImageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookImageEntity extends EntityPathBase<BookImageEntity> {

    private static final long serialVersionUID = 928372785L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookImageEntity bookImageEntity = new QBookImageEntity("bookImageEntity");

    public final QBookEntity book;

    public final NumberPath<Long> imageId = createNumber("imageId", Long.class);

    public final StringPath imagePath = createString("imagePath");

    public final NumberPath<Integer> sortOrder = createNumber("sortOrder", Integer.class);

    public QBookImageEntity(String variable) {
        this(BookImageEntity.class, forVariable(variable), INITS);
    }

    public QBookImageEntity(Path<? extends BookImageEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookImageEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookImageEntity(PathMetadata metadata, PathInits inits) {
        this(BookImageEntity.class, metadata, inits);
    }

    public QBookImageEntity(Class<? extends BookImageEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.book = inits.isInitialized("book") ? new QBookEntity(forProperty("book"), inits.get("book")) : null;
    }

}

