package com.bikesense.repository;

import com.bikesense.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByUsernameIgnoreCase(String username);

    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    Optional<Usuario> findByTelefone(String telefone);

    boolean existsByUsernameIgnoreCase(String username);

    @Query("SELECT u FROM Usuario u WHERE " +
            "(:nome IS NULL OR LOWER(u.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:username IS NULL OR LOWER(u.username) = LOWER(:username))")
    List<Usuario> buscarComFiltros(
            @Param("nome") String nome,
            @Param("username") String username);
}