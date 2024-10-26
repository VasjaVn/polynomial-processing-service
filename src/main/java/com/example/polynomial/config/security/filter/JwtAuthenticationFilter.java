package com.example.polynomial.config.security.filter;

import com.example.polynomial.exception.MissingJwtTokenException;
import com.example.polynomial.util.validator.JwtTokenValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Set<String> SWAGGER_URLS;

    static {
        SWAGGER_URLS = new HashSet<>();
        SWAGGER_URLS.add("/swagger-ui/index.html");
        SWAGGER_URLS.add("/swagger-ui/index.css");
        SWAGGER_URLS.add("/swagger-ui/swagger-ui.css");
        SWAGGER_URLS.add("/swagger-ui/swagger-ui-standalone-preset.js.html");
        SWAGGER_URLS.add("/swagger-ui/swagger-ui-bundle.js");
        SWAGGER_URLS.add("/swagger-ui/swagger-ui-standalone-preset.js");
        SWAGGER_URLS.add("/swagger-ui/swagger-initializer.js");
        SWAGGER_URLS.add("/swagger-ui/favicon-32x32.png");
        SWAGGER_URLS.add("/v3/api-docs/swagger-config");
        SWAGGER_URLS.add("/v3/api-docs");
        SWAGGER_URLS.add("/goform/set_LimitClient_cfg");
    }

    private final JwtTokenValidator jwtTokenValidator;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (!SWAGGER_URLS.contains(request.getRequestURI())) {
            String header = request.getHeader(AUTHORIZATION);
            String jwtToken = null;

            if (header != null && header.startsWith("Bearer ")) {
                jwtToken = header.substring(7);
                try {
                    if (!jwtToken.isEmpty() && jwtTokenValidator.validate(jwtToken)) {
                        log.info("Jwt Token is valid");
                    }
                } catch (Exception ex) {
                    JwtAuthenticationFilterExceptionHolder.set(ex);
                    log.warn("Jwt Token is not valid: {}", ex.getMessage());
                }

            } else {
                JwtAuthenticationFilterExceptionHolder.set(new MissingJwtTokenException("Missing JWT token"));
                log.warn("Missing JWT token: [ url = {} ]", request.getRequestURI());
            }
        }

        addJwtUserToSecurityContext();

        filterChain.doFilter(request, response);
    }

    private void addJwtUserToSecurityContext() {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("JWT_USER", null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
