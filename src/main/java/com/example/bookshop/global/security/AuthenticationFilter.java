package com.example.bookshop.global.security;

import com.example.bookshop.global.exception.CustomException;
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

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final Set<String> WHITELIST = Set.of(
            "/api/user/login",
            "/api/auth/**"
    );


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String uri = request.getRequestURI();

        if (isWhitelisted(uri)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolvedTokenFromRequest(request);


        try {

            if (tokenProvider.validateToken(token)) {

                Authentication authentication = tokenProvider.getAuthentication(token);
                log.info("인증 객체 principal: {}", authentication.getPrincipal());

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
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
