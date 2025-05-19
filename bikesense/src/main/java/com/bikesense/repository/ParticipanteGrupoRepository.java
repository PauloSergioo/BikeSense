package com.bikesense.repository;

import com.bikesense.model.ParticipanteGrupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipanteGrupoRepository extends JpaRepository<ParticipanteGrupo, Long> {

    List<ParticipanteGrupo> findByGrupoId(Long grupoId);

    List<ParticipanteGrupo> findByNomeContainingIgnoreCase(String nome);

    Optional<ParticipanteGrupo> findByTelefone(String telefone);

    @Modifying
    @Query("DELETE FROM ParticipanteGrupo p WHERE p.grupo.id = :grupoId")
    void deleteByGrupoId(@Param("grupoId") Long grupoId);
}