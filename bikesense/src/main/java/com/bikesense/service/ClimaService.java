package com.bikesense.service;

import com.bikesense.model.Clima;
import com.bikesense.repository.ClimaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClimaService {

    private final ClimaRepository climaRepository;

    public Clima getClimaAtual() {
        return climaRepository.findClimaMaisRecente()
                .orElseGet(() -> Clima.builder()
                        .cidade("Ilhabela")
                        .estado("SP")
                        .qualidadeDoAr("Boa")
                        .temperatura("28°C")
                        .previsao("Ensolarado")
                        .indiceUv("Alto")
                        .dataHoraRegistro(LocalDateTime.now())
                        .build());
    }
}
