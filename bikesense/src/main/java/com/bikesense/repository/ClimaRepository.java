package com.bikesense.repository;

import com.bikesense.model.Clima;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClimaRepository extends JpaRepository<Clima, Long> {

    @Query("SELECT c FROM Clima c ORDER BY c.dataHoraRegistro DESC LIMIT 1")
    Optional<Clima> findClimaMaisRecente();
}