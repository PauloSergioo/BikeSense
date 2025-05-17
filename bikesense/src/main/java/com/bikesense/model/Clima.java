package com.bikesense.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clima {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clima_seq")
    @SequenceGenerator(name = "clima_seq", sequenceName = "clima_seq", allocationSize = 1)
    private Long id;

    private String cidade;
    private String estado;
    private Double temperaturaAtual;
    private String qualidadeDoAr;
    private String previsao;
    private LocalDateTime horario;
}
