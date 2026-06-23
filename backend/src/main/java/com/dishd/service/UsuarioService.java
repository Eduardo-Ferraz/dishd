package com.dishd.service;

import com.dishd.dto.UsuarioDTO;
import com.dishd.exception.ResourceNotFoundException;
import com.dishd.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Consulta de usuarios (perfis publicos). */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(UsuarioDTO::from).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscar(Long id) {
        return usuarioRepository.findById(id)
                .map(UsuarioDTO::from)
                .orElseThrow(() -> ResourceNotFoundException.of("Usuario", id));
    }
}
