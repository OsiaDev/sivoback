package com.coljuegos.sivo.config.security;

import com.coljuegos.sivo.data.dto.UserDTO;
import com.coljuegos.sivo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        try {
            String header = request.getHeader(AUTHORIZATION_HEADER);

            if (header == null || !header.startsWith(BEARER_PREFIX)) {
                if (this.isProtectedEndpoint(requestURI)) {
                    log.debug("Request sin token JWT válido: {} {}", method, requestURI);
                }
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = this.extractToken(header);
            if (jwt == null || jwt.trim().isEmpty()) {
                log.warn("Token JWT vacío en request: {} {}", method, requestURI);
                filterChain.doFilter(request, response);
                return;
            }

            if (this.processJwtToken(jwt, request)) {
                log.debug("Autenticación JWT exitosa para request: {} {}", method, requestURI);
            } else {
                log.debug("Falló la autenticación JWT para request: {} {}", method, requestURI);
            }

        } catch (Exception ex) {
            log.error("Error crítico al procesar token JWT para request: {} {} - Error: {}",
                    method, requestURI, ex.getMessage(), ex);
            SecurityContextHolder.clearContext();

            if (isProtectedEndpoint(requestURI)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"Token JWT inválido\"}");
                response.setContentType("application/json");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractToken(String header) {
        try {
            if (header.length() <= BEARER_PREFIX.length()) {
                return null;
            }
            return header.substring(BEARER_PREFIX.length()).trim();
        } catch (Exception ex) {
            logger.warn("Error al extraer token del header de autorización", ex);
            return null;
        }
    }

    private boolean processJwtToken(String jwt, HttpServletRequest request) {
        try {
            String username = this.jwtUtil.extractUsername(jwt);
            Long perCodigo = this.jwtUtil.extractPerCodigo(jwt);
            Collection<String> roles = this.jwtUtil.extractRoles(jwt);

            if (username == null || username.trim().isEmpty()) {
                log.warn("Token JWT no contiene username válido");
                return false;
            }

            if (roles == null) {
                log.warn("Token JWT no contiene roles válidos para usuario: {}", username);
                roles = Collections.emptyList();
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.debug("Usuario {} ya está autenticado en el contexto de seguridad", username);
                return true;
            }

            Collection<SimpleGrantedAuthority> authorities = this.convertRolesToAuthorities(roles, username);

            UserDTO user = new UserDTO(username, authorities);
            user.setPerCodigo(perCodigo);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user, null, authorities);

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);

            log.debug("Usuario autenticado exitosamente: {} con {} roles", username, authorities.size());
            return true;

        } catch (Exception ex) {
            log.error("Error al procesar token JWT: {}", ex.getMessage(), ex);
            return false;
        }
    }

    private Collection<SimpleGrantedAuthority> convertRolesToAuthorities(Collection<String> roles, String username) {
        try {
            Collection<SimpleGrantedAuthority> authorities = roles.stream()
                    .filter(role -> role != null && !role.trim().isEmpty())
                    .map(role -> {
                        try {
                            return new SimpleGrantedAuthority(role.trim());
                        } catch (Exception ex) {
                            log.warn("Error al procesar rol '{}' para usuario '{}': {}", role, username, ex.getMessage());
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            if (authorities.isEmpty()) {
                log.warn("Usuario '{}' no tiene roles válidos después del filtrado", username);
            }

            return authorities;
        } catch (Exception ex) {
            log.error("Error al convertir roles para usuario '{}': {}", username, ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    private boolean isProtectedEndpoint(String requestURI) {
        if (requestURI == null) return false;

        return !requestURI.startsWith("/auth/") &&
                !requestURI.equals("/public_resource") &&
                !requestURI.startsWith("/actuator/") &&
                !requestURI.startsWith("/swagger-") &&
                !requestURI.startsWith("/v3/api-docs");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        String method = request.getMethod();

        boolean shouldSkip = path.startsWith("/auth/") ||
                path.equals("/public_resource") ||
                (path.startsWith("/actuator/") && "GET".equals(method));

        if (shouldSkip) {
            log.debug("Skipping JWT filter for: {} {}", method, path);
        }

        return shouldSkip;
    }

}
