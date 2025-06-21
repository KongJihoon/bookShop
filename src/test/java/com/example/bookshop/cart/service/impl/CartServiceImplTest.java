package com.example.bookshop.cart.service.impl;

import com.example.bookshop.book.dto.CreateBookDto;
import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.book.repository.BookQueryRepository;
import com.example.bookshop.book.repository.BookRepository;
import com.example.bookshop.book.service.BookService;
import com.example.bookshop.cart.dto.AddCartDto;
import com.example.bookshop.cart.entity.CartEntity;
import com.example.bookshop.cart.repository.CartRepository;
import com.example.bookshop.cart.service.CartService;
import com.example.bookshop.category.entity.CategoryEntity;
import com.example.bookshop.category.repository.CategoryRepository;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.user.dto.SignUpUserDto;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CartServiceImplTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartRepository cartRepository;

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
    @DisplayName("장바구니 상품 추가 테스트")
    void addCartItem() {
        // given
        UserEntity userEntity = userRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        BookEntity bookEntity = bookRepository.findById(1L)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));


        AddCartDto.Request request = new AddCartDto.Request(bookEntity.getBookId(), 3);

        // when

        cartService.addCartItem(userEntity.getUserId(), request);

        CartEntity cartEntity = cartRepository.findByUserEntity(userEntity)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // then

        assertEquals(cartEntity.getUserEntity().getUsername(), userEntity.getUsername());
        assertEquals(3, cartEntity.getCartItems().get(0).getQuantity());



    }

    @Test
    @DisplayName("장바구니에 동일한 도서를 두 번 추가하면 수량이 누적된다")
    void addSameBookTwice_quantityIncreases() {
        // given
        UserEntity user = userRepository.findByLoginId("testuser")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        BookEntity book = bookRepository.findByTitle("테스트 도서 제목")
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        AddCartDto.Request firstRequest = new AddCartDto.Request(book.getBookId(), 2);
        AddCartDto.Request secondRequest = new AddCartDto.Request(book.getBookId(), 5);

        // when
        cartService.addCartItem(user.getUserId(), firstRequest); // 2개 추가
        cartService.addCartItem(user.getUserId(), secondRequest); // 5개 추가

        CartEntity cart = cartRepository.findByUserEntity(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // then
        assertEquals(1, cart.getCartItems().size());
        assertEquals(7, cart.getCartItems().get(0).getQuantity());
        assertEquals(book.getTitle(), cart.getCartItems().get(0).getBookEntity().getTitle());
    }






    private MultipartFile createMockImage(String fileName) throws IOException {

        Path path = tempDir.resolve(fileName);

        Files.write(path, "fake-image-content".getBytes());

        return new MockMultipartFile(
                "file", fileName, "image/jpeg", Files.readAllBytes(path)
        );

    }

}