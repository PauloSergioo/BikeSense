package com.bikesense.repository;

import com.bikesense.model.Rota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {

    List<Rota> findByNomeContainingIgnoreCase(String nome);

    List<Rota> findByNivel(String nivel);

    List<Rota> findByTipo(String tipo);

    List<Rota> findByQualidadeAr(String qualidadeAr);

    @Query("SELECT r FROM Rota r WHERE r.distanciaKm BETWEEN :min AND :max")
    List<Rota> findByDistanciaBetween(@Param("min") Double min, @Param("max") Double max);

    @Query("SELECT r FROM Rota r WHERE " +
            "(:nome IS NULL OR LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) AND " +
            "(:nivel IS NULL OR r.nivel = :nivel) AND " +
            "(:tipo IS NULL OR r.tipo = :tipo)")
    List<Rota> buscarComFiltros(
            @Param("nome") String nome,
            @Param("nivel") String nivel,
            @Param("tipo") String tipo);
}
