package com.recorder.service;

import com.recorder.controller.entity.CustomUserDetails;
import com.recorder.controller.entity.UserPrincipal;
import com.recorder.controller.entity.Usuario;
import com.recorder.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final UsuarioRepository usuarioRepository;

	public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		// Adiciona "ROLE_" se não existir
		List<GrantedAuthority> authorities = usuario.getRoles().stream().map(role -> {
			String rolesName = role.getDescricao(); // ou role.getDescricao()
			if (!rolesName.startsWith("ROLE_")) {
				rolesName = "ROLE_" + rolesName;
			}
			return new SimpleGrantedAuthority(rolesName);
		}).collect(Collectors.toList());

		return new User(usuario.getEmail(), usuario.getSenha(), authorities);
	}
}