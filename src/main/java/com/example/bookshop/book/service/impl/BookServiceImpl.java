package com.example.bookshop.book.service.impl;

import com.example.bookshop.book.dto.BookDto;
import com.example.bookshop.book.dto.BookImageDto;
import com.example.bookshop.book.dto.CreateBookDto;
import com.example.bookshop.book.entity.BookEntity;
import com.example.bookshop.book.repository.BookQueryRepository;
import com.example.bookshop.book.repository.BookRepository;
import com.example.bookshop.book.service.BookService;
import com.example.bookshop.category.entity.BookCategory;
import com.example.bookshop.category.entity.CategoryEntity;
import com.example.bookshop.category.repository.CategoryRepository;
import com.example.bookshop.global.dto.ResultDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.bookshop.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final CategoryRepository categoryRepository;

    private final UserRepository userRepository;

    private final BookQueryRepository  bookQueryRepository;

    // 썸네일 이미지를 저장할 디렉터리 경로
    private static final String THUMBNAIL_DIR = "uploads/book-thumbnail/";

    // 상세 이미지를 저장할 디렉터리 경로
    private static final String DETAIL_IMAGE_DIR = "uploads/book-detail/";


    /**
     * 도서 등록 API
     */
    @Override
    @Transactional
    public ResultDto<CreateBookDto.Response> createBook(
            CreateBookDto.Request request,
            MultipartFile thumbnailImagePath,                // 썸네일 이미지 파일
            List<MultipartFile> imagesPaths,                 // 상세 이미지 리스트
            Long userId
    ) throws IOException {

        log.info("[도서 등록] 제목: {}", request.getTitle());


        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));


        // 등록된 도서 확인
        if (bookRepository.existsByTitle(request.getTitle())) {
            throw new CustomException(ALREADY_CREATE_BOOK);
        }


        // 썸네일 이미지 파일을 저장하고, 저장된 파일명을 경로로 받음
        String thumbnailFileName = saveImage(thumbnailImagePath, THUMBNAIL_DIR);

        // DTO에 썸네일 경로 설정
        request.setThumbnailImagePath(thumbnailFileName);

        // 상세 이미지 정보를 담을 리스트
        List<BookImageDto> imageDtos = new ArrayList<>();

        int sortOrder = 0;

        // 상세 이미지 리스트 반복 처리
        for (MultipartFile imagePath : imagesPaths) {
            String detailImagePaths = saveImage(imagePath, DETAIL_IMAGE_DIR);
            imageDtos.add(new BookImageDto(detailImagePaths, sortOrder++));
        }

        // DTO에 상세 이미지 경로들 설정
        request.setImagesPath(imageDtos);

        // DTO → Entity 변환
        BookEntity bookEntity = CreateBookDto.Request.toEntity(request);


        List<CategoryEntity> categories = categoryRepository.findAllById(request.getCategoryIds());

        if (categories.isEmpty()) {
            throw new CustomException(NOT_FOUND_CATEGORY);
        }
        List<BookCategory> bookCategories = categories.stream()
                        .map(category -> BookCategory.builder()
                                .bookEntity(bookEntity)
                                .categoryEntity(category)
                                .build())
                .collect(Collectors.toList());

        bookEntity.setBookCategories(bookCategories);

        bookEntity.setUserEntity(userEntity);

        BookEntity savedBook = bookRepository.save(bookEntity);

        log.info("[도서 등록 완료] 제목: {}", savedBook.getTitle());

        // 저장된 결과를 응답 형태로 변환
        return ResultDto.of(
                "도서 등록에 성공하였습니다.",
                CreateBookDto.Response.fromDto(BookDto.fromEntity(savedBook))
        );
    }


    /**
     * 도서 상세 조회 API
     */
    @Override
    @Transactional
    public ResultDto<BookDto> getBookDetails(Long bookId) {


        BookEntity bookEntity = bookRepository.findById(bookId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_BOOK));



        return ResultDto.of("도서 상세조회 완료.", BookDto.fromEntity(bookEntity));

    }

    /**
     * 도서 검색 API
     */
    @Override
    public ResultDto<Page<BookDto>> searchBook(String keyword, Pageable pageable) {


        Page<BookDto> result = bookQueryRepository.searchByKeyword(keyword, pageable);

        if (result.isEmpty()) {
            throw new CustomException(NOT_FOUND_BOOK);
        }

        return ResultDto.of("도서 검색이 완료되었습니다.", result);
    }

    @Override
    public ResultDto<Page<BookDto>> searchCategory(Long categoryId, Pageable pageable) {

        Page<BookDto> bookDtos = bookQueryRepository.searchCategory(categoryId, pageable);

        if (bookDtos.isEmpty()) {
            throw new CustomException(NOT_FOUND_CATEGORY);
        }


        return ResultDto.of("카테고리 별 도서 조회 완료", bookDtos);
    }


    /**
     * 이미지 파일을 지정된 경로에 저장하고 저장 경로를 문자열로 반환하는 메서드
     */
    private String saveImage(MultipartFile file, String uploadDir) throws IOException {
        if (file == null || file.isEmpty()) {
            log.error("이미지 업로드 실패 - 이미지가 존재하지 않습니다.");
            throw new CustomException(IMAGE_NOT_FOUND);
        }

        try {
            // UUID: 파일명 중복 방지를 위해 고유값 생성
            String uuid = UUID.randomUUID().toString();

            // 공백 있는 파일명은 오류 방지를 위해 "_"로 변환
            String fileName = uuid + "_" + file.getOriginalFilename().replaceAll(" ", "_");

            // 업로드 경로를 절대경로로 변환 (보안 및 정합성)
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

            // 경로가 없다면 폴더 생성
            Files.createDirectories(uploadPath);

            // 최종 저장 경로 구성
            Path filePath = uploadPath.resolve(fileName);

            // 실제 파일 저장 (Spring 제공 메서드)
            file.transferTo(filePath.toFile());


            // 저장된 경로 문자열 반환 (DB 저장용)
            return uploadDir + fileName;
        } catch (IOException e) {
            log.error("IOException : {}", e.getMessage());
            throw new CustomException(IMAGE_UPLOAD_FAIL);
        }



    }
}

