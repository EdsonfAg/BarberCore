package com.barbercore.application.usecases;

import com.barbercore.application.ports.CatalogoRepositoryPort;
import com.barbercore.application.ports.VentaPOSRepositoryPort;
import com.barbercore.domain.ItemCatalogo;
import com.barbercore.domain.TicketPOS;
import java.util.Map;

/**
 * Venta Cruzada (Retail/POS): Registra los servicios y si el cliente compró
 * productos (ceras, aceites, bálsamos) descontando inventario y vinculándolo a su expediente.
 */
public class ProcesarVentaPOSUseCase {

    private final CatalogoRepositoryPort catalogoRepository;
    private final VentaPOSRepositoryPort ventaPOSRepository;

    public ProcesarVentaPOSUseCase(
            CatalogoRepositoryPort catalogoRepository,
            VentaPOSRepositoryPort ventaPOSRepository) {
        this.catalogoRepository = catalogoRepository;
        this.ventaPOSRepository = ventaPOSRepository;
    }

    public TicketPOS ejecutar(int idNuevoTicket, int idCliente, Integer idCitaOrigen, Map<Integer, Integer> itemsYCantidades) {
        if (itemsYCantidades == null || itemsYCantidades.isEmpty()) {
            throw new IllegalArgumentException("El ticket de venta debe contener al menos un servicio o artículo.");
        }

        TicketPOS ticket = new TicketPOS(idNuevoTicket, idCliente, idCitaOrigen);

        for (Map.Entry<Integer, Integer> entrada : itemsYCantidades.entrySet()) {
            int idItem = entrada.getKey();
            int cantidad = entrada.getValue();

            ItemCatalogo item = catalogoRepository.buscarPorId(idItem)
                    .orElseThrow(() -> new IllegalArgumentException("El ítem con ID " + idItem + " no existe en el catálogo."));

            // Al agregar la línea al ticket, si es artículo retail valida y descuenta el stock automáticamente
            ticket.agregarLinea(item, cantidad);

            if (item.esArticuloRetail()) {
                catalogoRepository.guardar(item);
            }
        }

        ventaPOSRepository.guardar(ticket);
        return ticket;
    }
}
