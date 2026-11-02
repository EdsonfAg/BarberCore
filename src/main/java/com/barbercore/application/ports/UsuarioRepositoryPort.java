package com.barbercore.application.ports;

import com.barbercore.domain.Usuario;
import java.util.Optional;

public interface UsuarioRepositoryPort {
    Optional<Usuario> buscarPorId(int idUsuario);
    void guardar(Usuario usuario);
}
