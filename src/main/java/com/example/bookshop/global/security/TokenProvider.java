package com.example.bookshop.global.security;

import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.service.RedisService;
import com.example.bookshop.user.service.UserService;
import com.example.bookshop.user.type.UserType;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static com.example.bookshop.global.exception.ErrorCode.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final RedisService redisService;
    private final UserService userService;

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    
    
    
    @Value("${spring.jwt.secret}")
    private String secretKey;

    // Access 토큰 유효시간 30분
    private static final Long ACCESS_TOKEN_EXPIRED = 1800000L;


    // Refresh 토큰 유효시간 1시간
    private static final long REFRESH_TOKEN_EXPIRED = 3600000L;

    private Key key;

    // JWT 서명용 Key를 애플리케이션 시작 시 단 한 번만 초기화
    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // 로그인 시 사용할 AccessToken 생성 (사용자 정보 + 30분 유효)
    public String createAccessToken(String loginId, String email, UserType userType) {

        log.info("Create Access Token");

        String accessToken = generateAccessToken(loginId, email, userType, ACCESS_TOKEN_EXPIRED);

        log.info("Created Access Token");

        return accessToken;
    }

    // Redis에 저장할 RefreshToken 생성 (로그인 ID만 포함, 1시간 유효)
    public String createRefreshToken(String loginId) {

        log.info("Create Refresh Token");

        String refreshToken = generateRefreshToken(loginId, REFRESH_TOKEN_EXPIRED);

        log.info("Created Refresh Token");


        redisService.setDataExpireMillis("refresh_token:" + loginId, refreshToken, REFRESH_TOKEN_EXPIRED);


        return refreshToken;
    }

    public Authentication getAuthentication(String token) {

        UserDetails userDetails = userService.loadUserByUsername(getUsernameFromToken(token));

        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );
    }


    public String getUsernameFromToken(String token) {


        return parseToken(token).getSubject();

    }


    public Claims parseToken(String token) {

        try {

            log.info("토큰 파싱 완료");

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (ExpiredJwtException e) {

            log.error("토큰 파싱 오류");

            return e.getClaims();
        }

    }







    // AccessToken용 Claims 설정 (loginId = sub, 이메일, userType)
    private String generateAccessToken(String loginId, String email, UserType userType, long expiredTime) {

        Claims claims = Jwts.claims().setSubject(loginId);

        claims.put("email", email);
        claims.put("userType", String.valueOf(userType));



        return returnToken(claims, expiredTime);
    }


    // RefreshToken용 Claims 설정 (loginId = sub)
    private String generateRefreshToken(String loginId, Long expiredTime) {

        Claims claims = Jwts.claims().setSubject(loginId);


        return returnToken(claims, expiredTime);

    }




    private String returnToken(Claims claims, Long expireTime) {

        var now = new Date();

        var expireDate = new Date(now.getTime() + expireTime);


        // 설정된 Claims와 서명 키를 바탕으로 JWT 토큰 문자열로 생성하여 반환
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(key)
                .compact();

    }


    public boolean validateToken(String token) {

        try {

            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            return !claims.getBody().getExpiration().before(new Date());

        } catch (IllegalArgumentException e) {
            throw new JwtException(NOT_FOUND_TOKEN.getMessage());
        } catch (MalformedJwtException e) {
            throw new JwtException(UNSUPPORTED_TOKEN.getMessage());
        } catch (ExpiredJwtException e) {
            throw new JwtException(EXPIRED_TOKEN.getMessage());
        } catch (JwtException e) {
            throw new JwtException(INVALID_TOKEN.getMessage());
        } catch (CustomException e) {
            throw e;
        }

    }


    // RefreshToken 갱신 시 검증
    public void validateRefreshToken(String refreshToken) {

        if (refreshToken == null) {
            throw new CustomException(NOT_FOUND_TOKEN);
        }

        Claims claims = parseToken(refreshToken);

        if (claims.getExpiration().before(new Date())) {
            throw new CustomException(EXPIRED_TOKEN);
        }

    }


    public String extractToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public long getRemainExpireTime(String token) {

        return parseToken(token).getExpiration().getTime() - System.currentTimeMillis();
    }

}
