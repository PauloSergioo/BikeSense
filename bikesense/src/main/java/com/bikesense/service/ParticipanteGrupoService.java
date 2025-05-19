package com.bikesense.service;

import com.bikesense.model.ParticipanteGrupo;
import com.bikesense.repository.GrupoRepository;
import com.bikesense.repository.ParticipanteGrupoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParticipanteGrupoService {
    private final ParticipanteGrupoRepository participanteRepository;
    private final GrupoRepository grupoRepository;

    @Transactional
    public ParticipanteGrupo criarParticipante(ParticipanteGrupo participante) {

        if (participante.getGrupo() != null && participante.getGrupo().getId() != null) {
            grupoRepository.findById(participante.getGrupo().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Grupo não encontrado"));
        }

        return participanteRepository.save(participante);
    }

    public Optional<ParticipanteGrupo> buscarPorId(Long id) {
        return participanteRepository.findById(id);
    }

    public List<ParticipanteGrupo> buscarPorGrupo(Long grupoId) {
        return participanteRepository.findByGrupoId(grupoId);
    }

    public List<ParticipanteGrupo> listarTodos() {
        return participanteRepository.findAll();
    }

    @Transactional
    public Optional<ParticipanteGrupo> atualizarParticipante(
            Long id,
            ParticipanteGrupo participanteAtualizado) {

        return participanteRepository.findById(id)
                .map(participanteExistente -> {
                    participanteExistente.setNome(participanteAtualizado.getNome());
                    participanteExistente.setTelefone(participanteAtualizado.getTelefone());

                    if (participanteAtualizado.getGrupo() != null) {
                        participanteExistente.setGrupo(participanteAtualizado.getGrupo());
                    }

                    return participanteRepository.save(participanteExistente);
                });
    }

    @Transactional
    public boolean deletarParticipante(Long id) {
        if (participanteRepository.existsById(id)) {
            participanteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void deletarTodosDoGrupo(Long grupoId) {
        participanteRepository.deleteByGrupoId(grupoId);
    }
}
