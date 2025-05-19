package com.bikesense.service;

import com.bikesense.model.Postagem;
import com.bikesense.repository.GrupoRepository;
import com.bikesense.repository.PostagemRepository;
import com.bikesense.repository.RotaRepository;
import com.bikesense.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostagemService {
    private final PostagemRepository postagemRepository;
    private final UsuarioRepository usuarioRepository;
    private final RotaRepository rotaRepository;
    private final GrupoRepository grupoRepository;

    @Transactional
    public Postagem criarPostagem(Postagem postagem) {

        if (postagem.getUsuario() != null && !usuarioRepository.existsById(postagem.getUsuario().getCpf())) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        if (postagem.getRota() != null && !rotaRepository.existsById(postagem.getRota().getId())) {
            throw new IllegalArgumentException("Rota não encontrada");
        }
        if (postagem.getGrupo() != null && !grupoRepository.existsById(postagem.getGrupo().getId())) {
            throw new IllegalArgumentException("Grupo não encontrado");
        }

        if (postagem.getDataHora() == null) {
            postagem.setDataHora(LocalDateTime.now());
        }

        return postagemRepository.save(postagem);
    }

    public Optional<Postagem> buscarPorId(Long id) {
        return postagemRepository.findById(id);
    }

    public List<Postagem> listarTodas() {
        return postagemRepository.findAll();
    }

    public List<Postagem> buscarPorUsuario(String cpfUsuario) {
        return postagemRepository.findByUsuarioCpf(cpfUsuario);
    }

    public List<Postagem> buscarPorGrupo(Long grupoId) {
        return postagemRepository.findByGrupoId(grupoId);
    }

    public List<Postagem> buscarPorRota(Long rotaId) {
        return postagemRepository.findByRotaId(rotaId);
    }

    public List<Postagem> buscarRecentes() {
        LocalDateTime limite = LocalDateTime.now().minusHours(24);
        return postagemRepository.findPostagensRecentes(limite);
    }

    @Transactional
    public boolean deletarPostagem(Long id) {
        if (postagemRepository.existsById(id)) {
            postagemRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Postagem> buscarPorCidade(String cidade) {
        return postagemRepository.findByCidadeIgnoreCase(cidade);
    }

    public List<Postagem> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return postagemRepository.findByDataHoraBetween(inicio, fim);
    }
}