package com.bikesense.repository;

import com.bikesense.model.RotaRealizada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RotaRealizadaRepository extends JpaRepository<RotaRealizada, Long> {

    List<RotaRealizada> findByUsuarioCpf(String cpf);

    List<RotaRealizada> findByRotaId(Long rotaId);

    List<RotaRealizada> findByData(LocalDate data);

    List<RotaRealizada> findByDataBetween(LocalDate inicio, LocalDate fim);

    List<RotaRealizada> findByUsuarioCpfAndData(String cpf, LocalDate data);

    boolean existsByUsuarioCpfAndRotaIdAndData(String cpf, Long rotaId, LocalDate data);

    @Query("SELECT COUNT(rr) FROM RotaRealizada rr WHERE rr.usuario.id = :cpf")
    long countByUsuarioCpf(@Param("cpf") String cpf);
}
