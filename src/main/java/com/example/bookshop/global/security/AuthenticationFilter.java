package com.example.bookshop.global.security;

import com.example.bookshop.global.exception.CustomException;
import com.example.bookshop.global.exception.ErrorCode;
import com.example.bookshop.global.service.RedisService;
import com.example.bookshop.user.entity.UserEntity;
import com.example.bookshop.user.repository.UserRepository;
import com.example.bookshop.user.type.UserState;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {


    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final UserRepository userRepository;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final Set<String> WHITELIST = Set.of(
            "/api/auth/login",
            "/api/user/signup",
            "/api/user/check-email",
            "/api/user/send-mail",
            "/api/user/check-auth-code",
            "/api/book/detail/**",
            "/api/book/search",
            "/api/book/search/category/**"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String uri = request.getRequestURI();

        if (isWhitelisted(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolvedTokenFromRequest(request);

        if (token != null && redisService.getData("logoutUser:" + token) != null) {
            log.warn("로그아웃 유저 접근: {}", token);
            handleAccessDenied(response, "로그아웃 유저");
            return;

        }



        try {


            if (tokenProvider.validateToken(token)) {

                Authentication authentication = tokenProvider.getAuthentication(token);
                log.info("인증 객체 principal: {}", authentication.getPrincipal());

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            String loginId = tokenProvider.getUsernameFromToken(token);

            UserEntity userEntity = userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));


            if (userEntity.getUserState() == UserState.WITHDRAW) {
                throw new CustomException(ErrorCode.WITHDRAW_USER);
            }

            filterChain.doFilter(request, response);


        } catch (JwtException | CustomException e) {
            handleAccessDenied(response, e.getMessage());
        }

    }


    private String resolvedTokenFromRequest(HttpServletRequest request) {

        String token = request.getHeader(TOKEN_HEADER);

        if (!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)) {

            return token.substring(TOKEN_PREFIX.length()).trim();
        }

        return null;
    }

    private void handleAccessDenied(HttpServletResponse response, String message) throws IOException {

        response.setContentType("application/json;charset=UTF-8");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();

        String errorMessage = objectMapper.writeValueAsString(
                Map.of("statusCode", HttpServletResponse.SC_UNAUTHORIZED,
                        "errorCode", "UNAUTHORIZED",
                        "errorMessage",  message)
        );


        response.getWriter().write(errorMessage);
    }

    private boolean isWhitelisted(String uri) {
        return WHITELIST.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, uri));
    }
}
