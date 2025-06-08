package com.example.bookshop.book.service;


import com.example.bookshop.book.dto.BookDto;
import com.example.bookshop.book.dto.CreateBookDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {


    ResultDto<CreateBookDto.Response> createBook(CreateBookDto.Request request, MultipartFile thumbnailImagePath, List<MultipartFile> imagesPaths, Long userId) throws IOException;

    ResultDto<BookDto> getBookDetails(Long bookId);

    ResultDto<Page<BookDto>> searchBook(String keyword, Pageable pageable);


    ResultDto<Page<BookDto>> searchCategory(Long categoryId, Pageable pageable);
}
