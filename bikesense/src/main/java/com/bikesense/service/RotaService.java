package com.bikesense.service;

import com.bikesense.model.Rota;
import com.bikesense.repository.RotaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RotaService {
    private final RotaRepository rotaRepository;

    @Transactional
    public Rota criarRota(Rota rota) {

        if (rota.getNome() == null || rota.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da rota é obrigatório");
        }
        if (rota.getDistanciaKm() != null && rota.getDistanciaKm() <= 0) {
            throw new IllegalArgumentException("Distância deve ser maior que zero");
        }

        return rotaRepository.save(rota);
    }

    public Optional<Rota> buscarPorId(Long id) {
        return rotaRepository.findById(id);
    }

    public List<Rota> listarTodas() {
        return rotaRepository.findAll();
    }

    public List<Rota> buscarPorNome(String nome) {
        return rotaRepository.findByNomeContainingIgnoreCase(nome);
    }

    public List<Rota> buscarPorNivel(String nivel) {
        return rotaRepository.findByNivel(nivel);
    }

    public List<Rota> buscarPorTipo(String tipo) {
        return rotaRepository.findByTipo(tipo);
    }

    public List<Rota> buscarPorDistancia(Double min, Double max) {
        return rotaRepository.findByDistanciaBetween(min, max);
    }

    public List<Rota> buscarComFiltros(String nome, String nivel, String tipo) {
        return rotaRepository.buscarComFiltros(nome, nivel, tipo);
    }

    @Transactional
    public Optional<Rota> atualizarRota(Long id, Rota rotaAtualizada) {
        return rotaRepository.findById(id)
                .map(rotaExistente -> {

                    if (rotaAtualizada.getNome() != null) {
                        rotaExistente.setNome(rotaAtualizada.getNome());
                    }
                    if (rotaAtualizada.getDistanciaKm() != null) {
                        rotaExistente.setDistanciaKm(rotaAtualizada.getDistanciaKm());
                    }
                    if (rotaAtualizada.getNivel() != null) {
                        rotaExistente.setNivel(rotaAtualizada.getNivel());
                    }
                    if (rotaAtualizada.getTipo() != null) {
                        rotaExistente.setTipo(rotaAtualizada.getTipo());
                    }
                    if (rotaAtualizada.getQualidadeAr() != null) {
                        rotaExistente.setQualidadeAr(rotaAtualizada.getQualidadeAr());
                    }
                    if (rotaAtualizada.getPrevisaoTempo() != null) {
                        rotaExistente.setPrevisaoTempo(rotaAtualizada.getPrevisaoTempo());
                    }
                    if (rotaAtualizada.getClima() != null) {
                        rotaExistente.setClima(rotaAtualizada.getClima());
                    }
                    if (rotaAtualizada.getDescricao() != null) {
                        rotaExistente.setDescricao(rotaAtualizada.getDescricao());
                    }

                    return rotaRepository.save(rotaExistente);
                });
    }

    @Transactional
    public boolean deletarRota(Long id) {
        if (rotaRepository.existsById(id)) {
            rotaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}