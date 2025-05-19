package com.bikesense.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_clima")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clima {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clima_seq")
    @SequenceGenerator(name = "clima_seq", sequenceName = "clima_seq", allocationSize = 1)
    private Long id;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 100)
    private String estado;

    @Column(name = "qualidade_ar", length = 50)
    private String qualidadeDoAr;

    @Column(name = "temperatura", length = 50)
    private String temperatura;

    @Column(name = "previsao", length = 100)
    private String previsao;

    @Column(name = "indice_uv", length = 50)
    private String indiceUv;

    @Column(name = "data_hora_registro")
    private LocalDateTime dataHoraRegistro;
}