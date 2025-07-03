package com.recorder.controller.entity.enuns;

/**
 * Enum que representa os tipos de roles/perfis de usuário no sistema.
 * Armazena as roles no formato que o Spring Security espera (ROLE_*).
 */
public enum Roles {
    
    ROLE_USUARIO("ROLE_USUARIO", "Usuário comum do sistema"),
    ROLE_ADMIN("ROLE_ADMIN", "Administrador com acesso total"),
    ROLE_PROFISSIONAL("ROLE_PROFISSIONAL", "Profissional com acesso especializado");
    
    private final String descricao;  // Formato ROLE_* para Spring Security
    private final String nomeExibicao;  // Nome amigável para exibição
    
    /**
     * Construtor do enum
     * @param descricao - No formato ROLE_* (ex: ROLE_USUARIO)
     * @param nomeExibicao - Nome amigável para exibição (ex: "Usuário")
     */
    Roles(String descricao, String nomeExibicao) {
        this.descricao = descricao;
        this.nomeExibicao = nomeExibicao;
    }
    
    /**
     * @return A descrição no formato ROLE_* (Spring Security compatible)
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * @return Nome amigável para exibição (sem ROLE_)
     */
    public String getNomeExibicao() {
        return nomeExibicao;
    }
    
    /**
     * Método para converter de String para enum
     * @param texto Pode ser com ou sem ROLE_ prefix
     * @return A instância do enum correspondente
     * @throws IllegalArgumentException Se não encontrar correspondência
     */
    public static Roles fromString(String texto) {
        for (Roles role : Roles.values()) {
            if (role.descricao.equalsIgnoreCase(texto) || 
                role.name().equalsIgnoreCase(texto) ||
                role.descricao.equalsIgnoreCase("ROLE_" + texto)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Nenhuma role encontrada para: " + texto);
    }
    
    /**
     * @return Versão simplificada para exibição (sem ROLE_)
     */
    public String getRoleSimplificada() {
        return descricao.replace("ROLE_", "");
    }
}