package com.bikesense.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_participante_grupo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipanteGrupo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participante_seq")
    @SequenceGenerator(name = "participante_seq", sequenceName = "participante_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "grupo_id", foreignKey = @ForeignKey(name = "fk_participante_grupo"))
    private Grupo grupo;
}