package com.example.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.dto.response.ExceptionResponse;
import com.example.exception.AuthenticationExceptionCustom;
import com.example.service.JwtService;
import com.example.service.TokenService;
import com.example.service.UserService;

import java.io.IOException;
import java.util.Date;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static com.example.enums.TokenType.ACCESS_TOKEN;

@Slf4j
@Component
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {
    private final UserService userService;

    private final JwtService jwtService;

    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader(AUTHORIZATION);
        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String token = authorization.substring("Bearer ".length());
        try {
            final String username = this.jwtService.extractUsername(token, ACCESS_TOKEN);
            if (this.tokenService.getToken(username) == null) {
                throw new AuthenticationExceptionCustom("Authentication failed");
            }
            if (StringUtils.isNotEmpty(username) & SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userService.userDetailsService().loadUserByUsername(username);
                if (this.jwtService.isValid(token, ACCESS_TOKEN, userDetails)) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authenticationToken);
                    SecurityContextHolder.setContext(context);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            this.handleException(e, request, response);
        }
    }

    private void handleException(Exception e, @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws IOException {
        switch (e) {
            case ExpiredJwtException expiredJwtException -> {
                log.error("Token expired");
                ExceptionResponse exceptionResponse = new ExceptionResponse();
                exceptionResponse.setTimestamp(new Date());
                exceptionResponse.setStatus(HttpServletResponse.SC_GONE);
                exceptionResponse.setPath(request.getRequestURI());
                exceptionResponse.setError("Token expired");
                exceptionResponse.setMessage(e.getMessage());

                response.setStatus(HttpServletResponse.SC_GONE);
                response.setContentType("application/json");
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
            }
            case SignatureException signatureException -> {
                log.error("Invalid signature message={}", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
            case MalformedJwtException malformedJwtException -> {
                ExceptionResponse exceptionResponse = new ExceptionResponse();
                exceptionResponse.setTimestamp(new Date());
                exceptionResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                exceptionResponse.setPath(request.getRequestURI());
                exceptionResponse.setError("Fraud during authentication");
                exceptionResponse.setMessage("Please do not use tokens provided by others!");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                ObjectMapper objectMapper = new ObjectMapper();
                response.getWriter().write(objectMapper.writeValueAsString(exceptionResponse));
            }
            case null, default -> {
                assert e != null;
                log.error("error={}", e.getMessage(), e.getCause());
            }
        }
    }
}