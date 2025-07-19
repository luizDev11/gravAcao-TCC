package com.recorder.config;

import com.recorder.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays; // Importar Arrays

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;
	private final JwtAuthFilter jwtAuthFilter;

	public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthFilter jwtAuthFilter) {
		this.userDetailsService = userDetailsService;
		this.jwtAuthFilter = jwtAuthFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						// 1. ROTAS QUE EXIGEM AUTENTICAÇÃO E/OU AUTORIZAÇÃO ESPECÍFICA (MAIS ESPECÍFICAS PRIMEIRO)
						// Esta deve vir antes de qualquer .permitAll() que inclua /api/auth/**
						.requestMatchers("/api/auth/validate-token").authenticated() // EXIGE TOKEN VÁLIDO

						// Rotas que EXIGEM autenticação (outras rotas que requerem token, como criar2)
						.requestMatchers("/api/agendamentos2/criar2").authenticated()

						// ✨ NOVO: Rotas de agendamento2 agora exigem autenticação.
						// A autorização por role será feita via @PreAuthorize no AgendamentoController.
						.requestMatchers("/api/agendamentos2/**").authenticated() // ✨ ADICIONADO/AJUSTADO AQUI ✨

						// Rotas administrativas (requerem ROLE_ADMIN)
						.requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

						// Rotas para profissionais (requerem ROLE_PROFISSIONAL)
						.requestMatchers("/api/profissional/**").hasAuthority("ROLE_PROFISSIONAL")

						// Rotas de agendamento (requerem qualquer uma das roles especificadas)
						.requestMatchers("/api/agendamentos/**")
						.hasAnyAuthority("ROLE_USUARIO", "ROLE_PROFISSIONAL", "ROLE_ADMIN")

						// 2. ROTAS PÚBLICAS (NÃO EXIGEM AUTENTICAÇÃO OU AUTORIZAÇÃO)
						// O login ('/api/auth/authenticate') DEVE estar aqui.
						// Todas as rotas genéricas .permitAll() devem vir DEPOIS das regras específicas.
						.requestMatchers("/api/auth/authenticate", // Rota de login (sem token)
								"/api/usuarios/**",       // Criação de usuário, etc.
								"/swagger-ui/**",         // Swagger UI
								"/v3/api-docs/**")        // OpenAPI Docs
						// REMOVIDO: "/api/agendamentos2" daqui, pois agora é .authenticated()
						.permitAll()

						// 3. TODAS AS OUTRAS REQUISIÇÕES (EXIGEM AUTENTICAÇÃO)
						.anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		// ✨ Ajuste das Origens Permitidas ✨
		// O erro indica que "http://127.0.0.1:5000" está sendo bloqueado.
		// Listar ambas as variações de localhost é bom.
		// Adicionar o próprio backend como origem permitida é redundante e pode mascarar o problema.
		// Se seu frontend está em http://127.0.0.1:5000, essa é a única origem que importa para o CORS.
		config.setAllowedOrigins(Arrays.asList(
				"http://127.0.0.1:5000",
				"http://localhost:5000"
		));

		// Se você quisesse usar padrões (mais flexível para desenvolvimento), seria:
		// config.setAllowedOriginPatterns(Arrays.asList(
		//     "http://127.0.0.1:*", // Permite qualquer porta para 127.0.0.1
		//     "http://localhost:*"  // Permite qualquer porta para localhost
		// ));

		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos HTTP permitidos
		config.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type")); // Cabeçalhos permitidos
		config.setAllowCredentials(true); // Crucial: Permite o envio de cookies/cabeçalhos de autorização
		config.setMaxAge(3600L); // Tempo de cache para preflight requests (em segundos)

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config); // Aplica esta configuração CORS a todas as rotas
		return source;
	}
}