package com.bikesense.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rota {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rota_seq")
    @SequenceGenerator(name = "rota_seq", sequenceName = "rota_seq", allocationSize = 1)
    private Long id;

    private String nome;
    private Double distanciaKm;
    private String nivel;
    private String tipo;
    private String qualidadeAr;
    private String previsaoTempo;
    private String clima;
    private String descricao;
}
