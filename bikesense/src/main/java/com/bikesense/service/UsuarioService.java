package com.bikesense.service;

import com.bikesense.model.Usuario;
import com.bikesense.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario criarUsuario(Usuario usuario) {

        if (usuario.getCpf() == null || usuario.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username é obrigatório");
        }

        if (usuarioRepository.existsByUsernameIgnoreCase(usuario.getUsername())) {
            throw new IllegalStateException("Username já está em uso");
        }

        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> buscarPorCpf(String cpf) {
        return usuarioRepository.findById(cpf);
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsernameIgnoreCase(username);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public List<Usuario> buscarPorNome(String nome) {
        return usuarioRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Optional<Usuario> buscarPorTelefone(String telefone) {
        return usuarioRepository.findByTelefone(telefone);
    }

    public List<Usuario> buscarComFiltros(String nome, String username) {
        return usuarioRepository.buscarComFiltros(nome, username);
    }

    @Transactional
    public Optional<Usuario> atualizarUsuario(String cpf, Usuario usuarioAtualizado) {
        return usuarioRepository.findById(cpf)
                .map(usuarioExistente -> {

                    if (usuarioAtualizado.getNome() != null) {
                        usuarioExistente.setNome(usuarioAtualizado.getNome());
                    }
                    if (usuarioAtualizado.getTelefone() != null) {
                        usuarioExistente.setTelefone(usuarioAtualizado.getTelefone());
                    }
                    if (usuarioAtualizado.getFotoPerfil() != null) {
                        usuarioExistente.setFotoPerfil(usuarioAtualizado.getFotoPerfil());
                    }

                    if (usuarioAtualizado.getUsername() != null &&
                            !usuarioAtualizado.getUsername().equalsIgnoreCase(usuarioExistente.getUsername())) {

                        if (usuarioRepository.existsByUsernameIgnoreCase(usuarioAtualizado.getUsername())) {
                            throw new IllegalStateException("Novo username já está em uso");
                        }
                        usuarioExistente.setUsername(usuarioAtualizado.getUsername());
                    }

                    return usuarioRepository.save(usuarioExistente);
                });
    }

    @Transactional
    public boolean deletarUsuario(String cpf) {
        if (usuarioRepository.existsById(cpf)) {
            usuarioRepository.deleteById(cpf);
            return true;
        }
        return false;
    }

    public boolean existePorUsername(String username) {
        return usuarioRepository.existsByUsernameIgnoreCase(username);
    }
}
