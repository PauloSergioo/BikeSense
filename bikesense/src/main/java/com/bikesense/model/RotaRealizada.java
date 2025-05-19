package com.bikesense.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RotaRealizada {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "realizada_seq")
    @SequenceGenerator(name = "realizada_seq", sequenceName = "realizada_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cpf")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "rota_id")
    private Rota rota;

    @Column(name = "data")
    private LocalDate data;
}