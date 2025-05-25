package com.example.bookshop.global.service;

import com.example.bookshop.global.dto.CheckDto;
import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

import static com.example.bookshop.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private final RedisService redisService;

    private final UserRepository userRepository;

    private static final Long EMAIL_TOKEN_EXPIRE = 3L;

    private static final int CODE_LENGTH = 6;

    private static final String EMAIL_PREFIX = "Email-Auth: ";


    public CheckDto sendAuthMail(String email) {

        String code = createRandomMail();


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        if (!userRepository.existsByEmail(email)) {
            throw new CustomException(EMAIL_NOT_FOUND);
        }

        try {

            mimeMessageHelper.setTo(email);

            mimeMessageHelper.setSubject("회원가입 이메일 인증번호입니다.");

            String msg = "<div style='margin:20px;'>"
                    + "<h1> 안녕하세요 도서 쇼핑몰 입니다. </h1>"
                    + "<br>"
                    + "<p>아래 코드를 입력해주세요<p>"
                    + "<br>"
                    + "<p>감사합니다.<p>"
                    + "<br>"
                    + "<div align='center' style='border:1px solid black; font-family:verdana';>"
                    + "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>"
                    + "<div style='font-size:130%'>"
                    + "CODE : <strong>" + code + "</strong><div><br/> "
                    + "</div>";

            mimeMessageHelper.setText(msg, true);

            javaMailSender.send(mimeMessage);
            redisService.setDataExpireMinutes(EMAIL_PREFIX + email, code, EMAIL_TOKEN_EXPIRE);

        } catch (MessagingException e) {

            throw new CustomException(INTERNAL_SERVER_ERROR);
        }



        return CheckDto.builder()
                .success(true)
                .message("인증번호를 전송하였습니다.")
                .build();
    }

    public CheckDto checkAuthCode(String email, String code) {

        String data = redisService.getData(EMAIL_PREFIX + email);

        if (data == null || !data.equals(code)) {
            throw new CustomException(INVALID_AUTH_CODE);
        }

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(EMAIL_NOT_FOUND));

        userEntity.setEmailAuth();

        userRepository.save(userEntity);

        redisService.deleteData(EMAIL_PREFIX + email);

        return CheckDto.builder()
                .success(true)
                .message("이메일 인증에 성공하였습니다.")
                .build();
    }

    private String createRandomMail() {

        Random random = new Random();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < CODE_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();

    }


}
