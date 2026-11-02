package com.barbercore.application.ports;

import com.barbercore.domain.ItemCatalogo;
import java.util.List;
import java.util.Optional;

public interface CatalogoRepositoryPort {
    Optional<ItemCatalogo> buscarPorId(int idItem);
    List<ItemCatalogo> obtenerTodos();
    void guardar(ItemCatalogo item);
}
