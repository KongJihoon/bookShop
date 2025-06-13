package com.example.bookshop.book.controller;

import com.example.bookshop.book.dto.BookDto;
import com.example.bookshop.book.dto.CreateBookDto;
import com.example.bookshop.book.dto.UpdateBookDto;
import com.example.bookshop.book.service.BookService;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.user.entity.UserEntity;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/book")
public class BookController {

    private final BookService bookService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<CreateBookDto.Response>> createBook(
            @RequestPart("request") @Valid CreateBookDto.Request request,
            @RequestPart("thumbnail") MultipartFile thumbnailImagePath,
            @RequestPart("images") List<MultipartFile> detailImagesPaths,
            @AuthenticationPrincipal UserEntity userEntity
            ) throws IOException {



        ResultDto<CreateBookDto.Response> book = bookService.createBook(request, thumbnailImagePath, detailImagesPaths, userEntity.getUserId());


        return ResponseEntity.ok(book);
    }

    @GetMapping("/detail/{bookId}")
    public ResponseEntity<ResultDto<BookDto>> getBookDetails(
            @PathVariable Long bookId
    ) {

        ResultDto<BookDto> bookDetails = bookService.getBookDetails(bookId);

        return ResponseEntity.ok(bookDetails);

    }

    @GetMapping("/search")
    public ResponseEntity<ResultDto<Page<BookDto>>> searchBook(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageRequest pageRequest = PageRequest.of(page, size);

        ResultDto<Page<BookDto>> pageResultDto = bookService.searchBook(keyword, pageRequest);

        return ResponseEntity.ok(pageResultDto);

    }

    @GetMapping("/search/category/{categoryId}")
    public ResponseEntity<ResultDto<Page<BookDto>>> searchCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        ResultDto<Page<BookDto>> pageResultDto = bookService.searchCategory(categoryId, pageRequest);

        return ResponseEntity.ok(pageResultDto);

    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<ResultDto<BookDto>> updateBook (
            @RequestPart("request") UpdateBookDto request,
            @RequestPart("thumbnail") MultipartFile thumbnailImagePath,
            @RequestPart("images") List<MultipartFile> detailImagesPaths,
            @AuthenticationPrincipal UserEntity userEntity

    ) throws IOException {

        ResultDto<BookDto> bookDtoResultDto = bookService.updateBook(request, thumbnailImagePath, detailImagesPaths, userEntity.getUserId());

        return ResponseEntity.ok(bookDtoResultDto);

    }


}
