package com.recorder.repository;

import com.recorder.controller.entity.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findByEmail(String email);

    // Métodos para verificar a existência de CPF e CNPJ
    boolean existsByCpf(String cpf); // <-- ADICIONE ESTA LINHA
    boolean existsByCnpj(String cnpj); // <-- ADICIONE ESTA LINHA

    // Se você tiver, também pode adicionar findByCpf e findByCnpj
    Optional<Usuario> findByCpf(String cpf);
    Optional<Usuario> findByCnpj(String cnpj);
}