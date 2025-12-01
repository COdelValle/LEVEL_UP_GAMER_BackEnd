package com.level_up_gamer.BackEnd.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.level_up_gamer.BackEnd.Service.UsuarioService;
import com.level_up_gamer.BackEnd.Model.Usuario.Usuario;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UsuarioService usuarioService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String bearerToken = request.getHeader("Authorization");
            String apiKey = request.getHeader("X-API-Key");

            if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                if (jwtProvider.validateToken(token)) {
                    String email = jwtProvider.getEmailFromToken(token);
                    Long usuarioId = jwtProvider.getUsuarioIdFromToken(token);

                    // Crear autenticación desde JWT y asignar roles desde el claim
                    String rol = jwtProvider.getRolFromToken(token);
                    List<SimpleGrantedAuthority> authorities;
                    if (rol != null && !rol.isEmpty()) {
                        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
                    } else {
                        authorities = Collections.emptyList();
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);
                    authentication.setDetails(usuarioId);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else if (apiKey != null && !apiKey.isEmpty()) {
                try {
                    // Buscar usuario por API key y asignar su rol
                    Usuario usuario = usuarioService.getUsuarioByApiKey(apiKey);
                    if (usuario != null) {
                        String email = usuario.getEmail();
                        Long usuarioId = usuario.getId();
                        String rol = usuario.getRol() != null ? usuario.getRol().toString() : null;

                        List<SimpleGrantedAuthority> authorities = rol != null
                                ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()))
                                : Collections.emptyList();

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(email, null, authorities);
                        authentication.setDetails(usuarioId);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                } catch (Exception ex) {
                    logger.warn("API Key no válida o error buscando usuario: " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Error en autenticación JWT: ", e);
        }
        
        filterChain.doFilter(request, response);
    }
    
    
}
