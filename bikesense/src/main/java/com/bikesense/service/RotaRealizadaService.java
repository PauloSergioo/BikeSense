package com.bikesense.service;

import com.bikesense.model.RotaRealizada;
import com.bikesense.repository.RotaRealizadaRepository;
import com.bikesense.repository.RotaRepository;
import com.bikesense.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RotaRealizadaService {
    private final RotaRealizadaRepository rotaRealizadaRepository;
    private final UsuarioRepository usuarioRepository;
    private final RotaRepository rotaRepository;

    @Transactional
    public RotaRealizada registrarRotaRealizada(RotaRealizada rotaRealizada) {

        if (!usuarioRepository.existsById(rotaRealizada.getUsuario().getCpf())) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        if (!rotaRepository.existsById(rotaRealizada.getRota().getId())) {
            throw new IllegalArgumentException("Rota não encontrada");
        }

        if (rotaRealizadaRepository.existsByUsuarioCpfAndRotaIdAndData(
                rotaRealizada.getUsuario().getCpf(),
                rotaRealizada.getRota().getId(),
                rotaRealizada.getData())) {
            throw new IllegalStateException("Esta rota já foi registrada para o usuário nesta data");
        }

        if (rotaRealizada.getData() == null) {
            rotaRealizada.setData(LocalDate.now());
        }

        return rotaRealizadaRepository.save(rotaRealizada);
    }

    public Optional<RotaRealizada> buscarPorId(Long id) {
        return rotaRealizadaRepository.findById(id);
    }

    public List<RotaRealizada> listarTodas() {
        return rotaRealizadaRepository.findAll();
    }

    public List<RotaRealizada> buscarPorUsuario(String cpf) {
        return rotaRealizadaRepository.findByUsuarioCpf(cpf);
    }

    public List<RotaRealizada> buscarPorRota(Long rotaId) {
        return rotaRealizadaRepository.findByRotaId(rotaId);
    }

    public List<RotaRealizada> buscarPorData(LocalDate data) {
        return rotaRealizadaRepository.findByData(data);
    }

    public List<RotaRealizada> buscarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return rotaRealizadaRepository.findByDataBetween(inicio, fim);
    }

    public List<RotaRealizada> buscarPorUsuarioEData(String cpf, LocalDate data) {
        return rotaRealizadaRepository.findByUsuarioCpfAndData(cpf, data);
    }

    @Transactional
    public Optional<RotaRealizada> atualizarRotaRealizada(Long id, RotaRealizada rotaAtualizada) {
        return rotaRealizadaRepository.findById(id)
                .map(rotaExistente -> {

                    if (rotaAtualizada.getData() != null) {
                        rotaExistente.setData(rotaAtualizada.getData());
                    }

                    if (rotaAtualizada.getUsuario() != null) {
                        if (!usuarioRepository.existsById(rotaAtualizada.getUsuario().getCpf())) {
                            throw new IllegalArgumentException("Novo usuário não encontrado");
                        }
                        rotaExistente.setUsuario(rotaAtualizada.getUsuario());
                    }

                    if (rotaAtualizada.getRota() != null) {
                        if (!rotaRepository.existsById(rotaAtualizada.getRota().getId())) {
                            throw new IllegalArgumentException("Nova rota não encontrada");
                        }
                        rotaExistente.setRota(rotaAtualizada.getRota());
                    }

                    return rotaRealizadaRepository.save(rotaExistente);
                });
    }

    @Transactional
    public boolean deletarRotaRealizada(Long id) {
        if (rotaRealizadaRepository.existsById(id)) {
            rotaRealizadaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public long contarRotasRealizadasPorUsuario(String cpf) {
        return rotaRealizadaRepository.countByUsuarioCpf(cpf);
    }
}