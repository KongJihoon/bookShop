package com.example.bookshop.global.security;

import com.example.bookshop.global.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            String message = e.getMessage();

            if (ErrorCode.NOT_FOUND_TOKEN.getMessage().equals(message)) {
                setResponse(response, ErrorCode.NOT_FOUND_TOKEN);
            } else if (ErrorCode.UNSUPPORTED_TOKEN.getMessage().equals(e.getMessage())) {
                setResponse(response, ErrorCode.UNSUPPORTED_TOKEN);
            } else if (ErrorCode.INVALID_TOKEN.getMessage().equals(e.getMessage())) {

                setResponse(response, ErrorCode.INVALID_TOKEN);
            } else if (ErrorCode.EXPIRED_TOKEN.getMessage().equals(e.getMessage())) {
                setResponse(response, ErrorCode.EXPIRED_TOKEN);
            } else {
                setResponse(response, ErrorCode.ACCESS_DENIED);
            }

        }






    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(errorCode.statusCode());
        ObjectMapper objectMapper = new ObjectMapper();

        String errorMessage = objectMapper.writeValueAsString(
                Map.of(
                        "statusCode", errorCode.statusCode(),
                        "errorCode", errorCode,
                        "errorMessage", errorCode.getMessage()
                )
        );

        response.getWriter().print(errorMessage);

    }
}
