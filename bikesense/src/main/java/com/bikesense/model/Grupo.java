package com.bikesense.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "tb_grupo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grupo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "grupo_seq")
    @SequenceGenerator(name = "grupo_seq", sequenceName = "grupo_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "origem", length = 100)
    private String origem;

    @Column(name = "destino", length = 100)
    private String destino;

    @Column(name = "data")
    private LocalDate dia;

    @Column(name = "horario")
    private LocalTime horario;

    @Column(name = "nivel_dificuldade", length = 20)
    private String nivel;

    @Column(name = "tipo_trajeto", length = 20)
    private String tipoTrajeto;

    @Column(name = "comentario", columnDefinition = "CLOB")
    private String comentario;

    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    private List<ParticipanteGrupo> participantes;
}