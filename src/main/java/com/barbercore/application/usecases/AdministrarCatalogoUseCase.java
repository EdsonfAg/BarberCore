package com.barbercore.application.usecases;

import com.barbercore.application.ports.CatalogoRepositoryPort;
import com.barbercore.application.ports.UsuarioRepositoryPort;
import com.barbercore.domain.ItemCatalogo;
import com.barbercore.domain.Usuario;

/**
 * Administración centralizada del catálogo de servicios y artículos de barbería
 * exclusiva para el rol Dueño.
 */
public class AdministrarCatalogoUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final CatalogoRepositoryPort catalogoRepository;

    public AdministrarCatalogoUseCase(
            UsuarioRepositoryPort usuarioRepository,
            CatalogoRepositoryPort catalogoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.catalogoRepository = catalogoRepository;
    }

    public ItemCatalogo registrarItem(
            int idUsuarioEjecutor,
            int idItem,
            String nombre,
            double precio,
            ItemCatalogo.TipoItem tipo,
            int stockInicial,
            int duracionMin) {

        validarPermisoDueno(idUsuarioEjecutor);
        ItemCatalogo nuevoItem = new ItemCatalogo(idItem, nombre, precio, tipo, stockInicial, duracionMin);
        catalogoRepository.guardar(nuevoItem);
        return nuevoItem;
    }

    public ItemCatalogo reabastecerArticulo(int idUsuarioEjecutor, int idItem, int cantidadAdicional) {
        validarPermisoDueno(idUsuarioEjecutor);
        ItemCatalogo item = catalogoRepository.buscarPorId(idItem)
                .orElseThrow(() -> new IllegalArgumentException("El artículo no existe en el catálogo."));

        item.reabastecerStock(cantidadAdicional);
        catalogoRepository.guardar(item);
        return item;
    }

    private void validarPermisoDueno(int idUsuarioEjecutor) {
        Usuario ejecutor = usuarioRepository.buscarPorId(idUsuarioEjecutor)
                .orElseThrow(() -> new IllegalArgumentException("El usuario ejecutor no existe."));

        if (!ejecutor.puedeAdministrarCatalogo()) {
            throw new SecurityException("Acceso denegado: Solo el Dueño puede administrar el catálogo.");
        }
    }
}
