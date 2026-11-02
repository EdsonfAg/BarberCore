package com.barbercore.application.ports;

import com.barbercore.domain.Cliente;
import java.util.Optional;

public interface ClienteRepositoryPort {
    Optional<Cliente> buscarPorIdUsuario(int idUsuario);
    void guardar(Cliente cliente);
}
