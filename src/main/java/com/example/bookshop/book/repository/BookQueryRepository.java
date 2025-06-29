package com.example.bookshop.book.repository;


import com.example.bookshop.book.dto.BookDto;
import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.book.entity.QBookEntity;
import com.example.bookshop.book.type.BookStatus;
import com.example.bookshop.category.entity.QBookCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.bookshop.book.type.BookStatus.AVAILABLE;

@Repository
@RequiredArgsConstructor
public class BookQueryRepository {


    private final JPAQueryFactory jpaQueryFactory;

    public Page<BookDto> searchByKeyword(String keyword, Pageable pageable) {


        QBookEntity qBookEntity = QBookEntity.bookEntity;


        BooleanBuilder builder = getKeywordCondition(keyword, qBookEntity);

        List<BookEntity> content = jpaQueryFactory
                .select(qBookEntity)
                .from(qBookEntity)
                .where(builder)
                .orderBy(qBookEntity.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookDto> bookDto = content.stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());

        Long total = Optional.ofNullable(
                jpaQueryFactory
                        .select(qBookEntity.count())
                        .from(qBookEntity)
                        .where(builder)
                        .fetchOne()).orElse(0L);

        return new PageImpl<>(bookDto, pageable, total);


    }


    public Page<BookDto> searchCategory(Long categoryId, Pageable pageable) {

        QBookEntity qBookEntity = QBookEntity.bookEntity;
        QBookCategory qBookCategory = QBookCategory.bookCategory;

        List<BookEntity> content = jpaQueryFactory
                .select(qBookEntity)
                .from(qBookEntity)
                .join(qBookEntity.bookCategories, qBookCategory)
                .fetchJoin()
                .where(qBookCategory.categoryEntity.categoryId.eq(categoryId)
                        .and(qBookEntity.bookStatus.eq(AVAILABLE)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qBookEntity.createdAt.desc())
                .fetch();

        List<BookDto> result = content.stream()
                .map(BookDto::fromEntity)
                .collect(Collectors.toList());

        Long total = jpaQueryFactory
                .select(qBookEntity.count())
                .from(qBookEntity)
                .join(qBookEntity.bookCategories, qBookCategory)
                .where(qBookCategory.categoryEntity.categoryId.eq(categoryId))
                .fetchOne();

        return new PageImpl<>(result, pageable, total);


    }

    private static BooleanBuilder getKeywordCondition(String keyword, QBookEntity qBookEntity) {
        BooleanBuilder builder = new BooleanBuilder();

        // 상태 먼저
        builder.and(qBookEntity.bookStatus.eq(AVAILABLE));

        // 키워드가 있으면 그 조건도 and로 묶어줌
        if (StringUtils.hasText(keyword)) {
            BooleanBuilder keywordCondition = new BooleanBuilder();
            keywordCondition.or(qBookEntity.title.containsIgnoreCase(keyword));
            keywordCondition.or(qBookEntity.author.containsIgnoreCase(keyword));
            keywordCondition.or(qBookEntity.publisher.containsIgnoreCase(keyword));

            builder.and(keywordCondition);
        }

        return builder;
    }

}
