package com.recorder.config;

import com.recorder.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

	private final JwtService jwtService;
	private final CustomUserDetailsService userDetailsService;

	public JwtAuthFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
									@NonNull HttpServletResponse response,
									@NonNull FilterChain filterChain)
			throws ServletException, IOException {

		// Configura headers CORS
		// Configura headers CORS
		response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:5000");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
		response.setHeader("Access-Control-Expose-Headers", "Authorization");


		// Para requisições OPTIONS, retorne imediatamente
		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			return;
		}

		// Ignora endpoints públicos (ajuste conforme suas rotas públicas)
		if (request.getServletPath().startsWith("/api/auth/") ||
				request.getServletPath().equals("/api/usuario/registrar")) {
			filterChain.doFilter(request, response);
			return;
		}

		final String authHeader = request.getHeader("Authorization");

		// Se não houver token, continue (o SecurityConfig bloqueará endpoints protegidos)
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			logger.warn("Requisição sem token JWT para: {}", request.getRequestURI());
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			final String userEmail = jwtService.extractUsername(jwt);

			if (userEmail == null) {
				logger.error("Token JWT não contém email válido");
				SecurityContextHolder.clearContext();
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
				return;
			}

			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

				if (userDetails == null) {
					logger.error("Usuário não encontrado para o email: {}", userEmail);
					SecurityContextHolder.clearContext();
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuário não encontrado");
					return;
				}

				if (!jwtService.isTokenValid(jwt, userDetails)) {
					logger.error("Token inválido para o usuário: {}", userEmail);
					SecurityContextHolder.clearContext();
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
					return;
				}

				String role = jwtService.extractRole(jwt);
				if (role == null) {
					logger.error("Token sem role definida para usuário: {}", userEmail);
					SecurityContextHolder.clearContext();
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "Token sem permissões definidas");
					return;
				}

				UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(
								userDetails,
								null,
								Collections.singletonList(new SimpleGrantedAuthority(role)));

				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				logger.debug("Usuário autenticado: {} com role: {}", userEmail, role);
			}

			filterChain.doFilter(request, response);

		} catch (Exception e) {
			logger.error("Falha na autenticação JWT", e);
			SecurityContextHolder.clearContext();
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Falha na autenticação: " + e.getMessage());
		}
	}
}