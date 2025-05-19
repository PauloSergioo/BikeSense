package com.bikesense.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_postagem")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postagem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
    @SequenceGenerator(name = "post_seq", sequenceName = "post_seq", allocationSize = 1)
    private Long id;

    @Column(name = "nome_usuario", length = 100)
    private String nomeUsuario;

    @Column(name = "foto_perfil_url", length = 255)
    private String fotoPerfilUrl;

    @Column(name = "tempo_postado")
    private LocalDateTime dataHora;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "estado", length = 100)
    private String estado;

    @Column(name = "descricao", columnDefinition = "CLOB")
    private String descricao;

    @Column(name = "observacao", columnDefinition = "CLOB")
    private String observacaoExtra;

    @Column(name = "dificuldade_percorrida", length = 20)
    private String dificuldadePercorrida;

    @Column(name = "qualidade_ar", length = 50)
    private String qualidadeAr;

    @Column(name = "temperatura_estimada", length = 50)
    private String temperaturaEstimada;

    @Column(name = "imagem_url", length = 255)
    private String imagemUrl;

    @ManyToOne
    @JoinColumn(name = "rota_id", foreignKey = @ForeignKey(name = "fk_postagem_rota"))
    private Rota rota;

    @ManyToOne
    @JoinColumn(name = "grupo_id", foreignKey = @ForeignKey(name = "fk_postagem_grupo"))
    private Grupo grupo;

    @ManyToOne
    @JoinColumn(name = "cpf_usuario", foreignKey = @ForeignKey(name = "fk_postagem_usuario"))
    private Usuario usuario;
}
