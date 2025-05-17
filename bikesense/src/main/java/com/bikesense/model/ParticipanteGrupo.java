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
public class ParticipanteGrupo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "participante_seq")
    @SequenceGenerator(name = "participante_seq", sequenceName = "participante_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Grupo grupo;
}
