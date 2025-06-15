package com.example.bookshop.book.service.impl;

import com.example.bookshop.book.dto.BookDto;
import com.example.bookshop.book.dto.CreateBookDto;
import com.example.bookshop.book.dto.UpdateBookDto;
import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.book.repository.BookQueryRepository;
import com.example.bookshop.book.repository.BookRepository;
import com.example.bookshop.book.service.BookService;
import com.example.bookshop.book.type.BookStatus;
import com.example.bookshop.category.entity.CategoryEntity;
import com.example.bookshop.category.repository.CategoryRepository;
import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.service.UserService;
import com.example.bookshop.user.type.UserState;
import com.example.bookshop.user.type.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static com.example.bookshop.global.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BookServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookQueryRepository bookQueryRepository;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {

        UserEntity userEntity = new UserEntity();

        if (!userRepository.existsByEmail("test@example.com")) {


            SignUpUserDto.Request build = SignUpUserDto.Request.builder()
                    .loginId("testuser")
                    .password("1111@11")
                    .checkPassword("1111@11")
                    .email("test@example.com")
                    .nickname("testuser")
                    .birth(LocalDate.parse("2000-01-01"))
                    .phone("010-1111-1111")
                    .address("테스트 주소")
                    .build();


            userService.signUpUser(build);

            userEntity = userRepository.findByLoginId("testuser").orElseThrow();
            userEntity.setEmailAuth();
            userRepository.save(userEntity);

        }

        if (!bookRepository.existsByTitle("테스트 도서 제목")) {
            CategoryEntity category = categoryRepository.save(new CategoryEntity("프로그래밍", null));


            List<Long> categoryId = List.of(category.getCategoryId());

            CreateBookDto.Request request = CreateBookDto.Request.builder()
                    .title("테스트 도서 제목")
                    .author("테스트 도서 저자")
                    .publisher("테스트 도서 출판사")
                    .price(10000)
                    .quantity(50)
                    .details("테스트 상세 설명")
                    .categoryIds(categoryId)
                    .build();

            MultipartFile mockImage = createMockImage("thumbnail.jpg");

            List<MultipartFile> details = List.of(
                    createMockImage("detail1.jpg"),
                    createMockImage("detail2.jpg")
            );

            bookService.createBook(request, mockImage, details, userEntity.getUserId());
        }

    }


    @Test
    @DisplayName("도서 생성 API 테스트")
    void createBook() throws IOException {
        // given
        UserEntity userEntity = userRepository.findByLoginId("testuser")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


        CategoryEntity category = categoryRepository.save(new CategoryEntity("프로그래밍", null));


        List<Long> categoryId = List.of(category.getCategoryId());

        CreateBookDto.Request request = CreateBookDto.Request.builder()
                .title("테스트 도서 제목")
                .author("테스트 도서 저자")
                .publisher("테스트 도서 출판사")
                .price(10000)
                .quantity(50)
                .details("테스트 상세 설명")
                .categoryIds(categoryId)
                .build();

        MultipartFile mockImage = createMockImage("thumbnail.jpg");

        List<MultipartFile> details = List.of(
                createMockImage("detail1.jpg"),
                createMockImage("detail2.jpg")
        );

        // when

        ResultDto<CreateBookDto.Response> result = bookService.createBook(request, mockImage, details, userEntity.getUserId());

        // then

        assertNotNull(result);
        assertEquals("테스트 도서 제목", result.getData().getTitle());

        List<BookEntity> books = bookRepository.findAll();

        assertEquals(1, books.size());

    }

    @Test
    @DisplayName("도서 상세 조회 API")
    void bookDetails() {
        // given

        BookEntity bookEntity = bookRepository.findById(1L)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));


        // when

        ResultDto<BookDto> bookDetails = bookService.getBookDetails(bookEntity.getBookId());

        // then

        assertEquals(bookEntity.getBookId(), bookDetails.getData().getBookId());


        assertEquals("도서 상세조회 완료.", bookDetails.getMessage());

    }

    @Test
    @DisplayName("도서 상세 조회 실패")
    void getBookDetailFail_NotFound() {

        assertThrows(CustomException.class, () -> {
            bookService.getBookDetails(999L);
        });
    }


    @Test
    @DisplayName("도서 검색 테스트")
    void searchBookTest() {
        // given

        BookEntity bookEntity = bookRepository.findByTitle("테스트 도서 제목")
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));


        // when

        ResultDto<Page<BookDto>> pageResultDto = bookService.searchBook("테스트 도서", PageRequest.of(0, 10));

        // then

        assertEquals(bookEntity.getTitle(), pageResultDto.getData().getContent().get(0).getTitle());

        assertEquals("도서 검색이 완료되었습니다.", pageResultDto.getMessage());

    }

    @Test
    @DisplayName("도서 검색 실패 테스트")
    void searchBook_Fail_WhenNoResult() {
        // given
        String keyword = "존재하지않는도서";
        Pageable pageable = PageRequest.of(0, 10);

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            bookService.searchBook(keyword, pageable);
        });


        assertEquals(NOT_FOUND_BOOK, exception.getErrorCode());
    }

    @Test
    @DisplayName("카테고리로 도서 검색 테스트")
    void searchCategoryTest() throws IOException {
        // given
        UserEntity user = userRepository.findByLoginId("testuser").orElseThrow();
        CategoryEntity category = categoryRepository.save(new CategoryEntity("프론트엔드", null));

        CreateBookDto.Request request = CreateBookDto.Request.builder()
                .title("프론트엔드 마스터")
                .author("홍길동")
                .publisher("프론출판사")
                .price(15000)
                .quantity(10)
                .details("카테고리 테스트용 도서입니다.")
                .categoryIds(List.of(category.getCategoryId()))
                .build();

        MultipartFile thumbnail = createMockImage("thumb.jpg");
        List<MultipartFile> images = List.of(createMockImage("img1.jpg"));

        bookService.createBook(request, thumbnail, images, user.getUserId());

        // when
        ResultDto<Page<BookDto>> result = bookService.searchCategory(category.getCategoryId(), PageRequest.of(0, 10));

        // then
        assertEquals(1, result.getData().getTotalElements());
        assertEquals("프론트엔드 마스터", result.getData().getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("도서 수정 테스트")
    void updateBookTest() throws IOException {
        // given
        UserEntity user = userRepository.findByLoginId("testuser").orElseThrow();

        BookEntity bookEntity = bookRepository.findByTitle("테스트 도서 제목")
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));

        UpdateBookDto request = UpdateBookDto.builder()
                .bookId(bookEntity.getBookId())
                .title("수정된 제목")
                .author("수정된 저자")
                .deleteImageIds(List.of(1L, 2L))
                .build();


        MultipartFile newThumbnail = createMockImage("new-thumb.jpg");
        List<MultipartFile> images = List.of(createMockImage("new-img1.jpg"),
                createMockImage("new-img2.jpg"));


        // when

        ResultDto<BookDto> result = bookService.updateBook(request, newThumbnail, images, user.getUserId());


        // then
        assertEquals("도서 수정이 완료되었습니다.", result.getMessage());
        assertEquals("수정된 제목", result.getData().getTitle());
        assertEquals("수정된 저자", result.getData().getAuthor());
        assertNotNull(result.getData().getThumbnailImagePath()); // 새 썸네일 반영 확인
        assertEquals(2, result.getData().getImagesPath().size()); // 상세 이미지 반영 확인




    }

    @Test
    @DisplayName("도서 수정 실패")
    void updateBook_fail_noPermission() throws IOException {
        // given
        BookEntity book = bookRepository.findByTitle("테스트 도서 제목")
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));

        // 다른 사용자
        UserEntity anotherUser = userRepository.save(UserEntity.builder()
                .loginId("1111")
                .password("1111")
                .email("1111@example.com")
                .birth(LocalDate.of(1111, 11, 5))
                .nickname("111")
                .address("1111")
                .phone("010-0000-0000")
                        .userState(UserState.ACTIVE)
                        .userType(UserType.USER)
                .build());
        anotherUser.setEmailAuth();

        UpdateBookDto request = UpdateBookDto.builder()
                .bookId(book.getBookId())
                .title("불법 수정")
                .author("해커")
                .publisher("해커출판사")
                .price(0)
                .quantity(0)
                .details("불법 시도")
                .deleteImageIds(List.of())
                .build();

        // when & then
        CustomException exception = assertThrows(CustomException.class, () -> {
            bookService.updateBook(request, null, List.of(), anotherUser.getUserId());
        });

        assertEquals(NOT_PERMISSION_BOOK, exception.getErrorCode());
    }

    @Test
    @DisplayName("도서 삭제 테스트")
    void deleteBook() {
        // given

        UserEntity userEntity = userRepository.findByLoginId("testuser").orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        BookEntity bookEntity = bookRepository.findByTitle("테스트 도서 제목")
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));



        // when

        CheckDto checkDto = bookService.deleteBook(bookEntity.getBookId(), userEntity.getUserId());

        CustomException exception = assertThrows(CustomException.class, () -> {
            bookService.searchBook("테스트 도서 제목", PageRequest.of(0, 10));
        });

        // then

        assertEquals("도서 삭제를 완료했습니다.", checkDto.getMessage());

        assertEquals(BookStatus.DELETED, bookEntity.getBookStatus());

        assertEquals(true, checkDto.isSuccess());

        // 삭제된 도서 검색 x
        assertEquals(NOT_FOUND_BOOK, exception.getErrorCode());
    }





    private MultipartFile createMockImage(String fileName) throws IOException {

        Path path = tempDir.resolve(fileName);

        Files.write(path, "fake-image-content".getBytes());

        return new MockMultipartFile(
                "file", fileName, "image/jpeg", Files.readAllBytes(path)
        );

    }

}