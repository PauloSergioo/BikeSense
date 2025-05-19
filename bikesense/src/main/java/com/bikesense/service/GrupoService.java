package com.bikesense.service;

import com.bikesense.model.Grupo;
import com.bikesense.repository.GrupoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GrupoService {
    private final GrupoRepository grupoRepository;

    @Transactional
    public Grupo criarGrupo(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public Optional<Grupo> buscarPorId(Long id) {
        return grupoRepository.findById(id);
    }

    public List<Grupo> listarTodosGrupos() {
        return grupoRepository.findAll();
    }

    @Transactional
    public Optional<Grupo> atualizarGrupo(Long id, Grupo grupoAtualizado) {
        return grupoRepository.findById(id)
                .map(grupoExistente -> {
                    grupoExistente.setNome(grupoAtualizado.getNome());
                    grupoExistente.setOrigem(grupoAtualizado.getOrigem());
                    grupoExistente.setDestino(grupoAtualizado.getDestino());
                    grupoExistente.setDia(grupoAtualizado.getDia());
                    grupoExistente.setHorario(grupoAtualizado.getHorario());
                    grupoExistente.setNivel(grupoAtualizado.getNivel());
                    grupoExistente.setTipoTrajeto(grupoAtualizado.getTipoTrajeto());
                    grupoExistente.setComentario(grupoAtualizado.getComentario());
                    return grupoRepository.save(grupoExistente);
                });
    }

    @Transactional
    public boolean deletarGrupo(Long id) {
        if (grupoRepository.existsById(id)) {
            grupoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}