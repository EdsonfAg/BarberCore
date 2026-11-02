package com.barbercore.application.ports;

import com.barbercore.domain.TicketPOS;
import java.util.List;

public interface VentaPOSRepositoryPort {
    List<TicketPOS> buscarPorCliente(int idCliente);
    void guardar(TicketPOS ticket);
}
