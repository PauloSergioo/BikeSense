package com.bikesense.repository;

import com.bikesense.model.Postagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {

    List<Postagem> findByUsuarioCpf(String cpfUsuario);

    List<Postagem> findByGrupoId(Long grupoId);

    List<Postagem> findByRotaId(Long rotaId);

    List<Postagem> findByDataHoraBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Postagem> findByCidadeIgnoreCase(String cidade);

    @Query("SELECT p FROM Postagem p WHERE p.dataHora >= :limite")
    List<Postagem> findPostagensRecentes(@Param("limite") LocalDateTime limite);
}