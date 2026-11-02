package com.barbercore.application.ports;

import com.barbercore.domain.Barbero;
import java.util.List;
import java.util.Optional;

public interface BarberoRepositoryPort {
    Optional<Barbero> buscarPorId(int idBarbero);
    List<Barbero> obtenerTodos();
    void guardar(Barbero barbero);
}
