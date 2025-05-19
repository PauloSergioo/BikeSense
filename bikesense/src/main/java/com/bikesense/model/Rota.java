package com.bikesense.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_rota")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rota {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rota_seq")
    @SequenceGenerator(name = "rota_seq", sequenceName = "rota_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "distancia_km")
    private Double distanciaKm;

    @Column(name = "nivel_dificuldade", length = 20)
    private String nivel;

    @Column(name = "tipo_trajeto", length = 30)
    private String tipo;

    @Column(name = "qualidade_ar", length = 30)
    private String qualidadeAr;

    @Column(name = "previsao", length = 50)
    private String previsaoTempo;

    @Column(length = 30)
    private String clima;

    @Column(columnDefinition = "CLOB")
    private String descricao;
}