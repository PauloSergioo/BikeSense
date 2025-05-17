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
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @SequenceGenerator(name = "post_seq", sequenceName = "post_seq", allocationSize = 1)
    private Long id;

    private LocalDateTime dataHora;
    private String cidade;
    private String estado;
    private String descricao;
    private String observacaoExtra;
    private String dificuldadePercorrida;
    private String qualidadeAr;
    private String temperaturaEstimada;
    private String imagemUrl;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne(optional = true)
    private Rota rota;

    @ManyToOne(optional = true)
    private Grupo grupo;
}

