package com.example.bookshop.global.security;

import com.example.bookshop.global.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {


    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = resolvedTokenFromRequest(request);


        try {

            if (tokenProvider.validateToken(token)) {

                Authentication authentication = tokenProvider.getAuthentication(token);

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

            return token.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

    private void handleAccessDenied(HttpServletResponse response, String message) throws IOException {

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ObjectMapper objectMapper = new ObjectMapper();

        String errorMessage = objectMapper.writeValueAsString(
                Map.of("statusCode", HttpServletResponse.SC_UNAUTHORIZED,
                        "errorCode", "UNAUTHORIZED",
                        "errorMessage",  message)
        );


        response.getWriter().write(errorMessage);
    }

}
