package com.barbercore.application.usecases;

import com.barbercore.application.ports.CitaRepositoryPort;
import com.barbercore.application.ports.ClienteRepositoryPort;
import com.barbercore.application.ports.VentaPOSRepositoryPort;
import com.barbercore.domain.Cita;
import com.barbercore.domain.Cliente;
import com.barbercore.domain.TicketPOS;
import java.util.List;

/**
 * Comunidad de Clientes Frecuentes (B2C): Consolida el perfil del cliente,
 * preferencias de corte, frecuencia de visita, asistencias e historial de compras POS.
 */
public class ObtenerExpedienteClienteUseCase {

    public static class ExpedienteCliente {
        private final Cliente cliente;
        private final String clasificacionFrecuencia;
        private final List<Cita> historialCitas;
        private final List<TicketPOS> historialComprasPOS;

        public ExpedienteCliente(
                Cliente cliente,
                String clasificacionFrecuencia,
                List<Cita> historialCitas,
                List<TicketPOS> historialComprasPOS) {
            this.cliente = cliente;
            this.clasificacionFrecuencia = clasificacionFrecuencia;
            this.historialCitas = historialCitas;
            this.historialComprasPOS = historialComprasPOS;
        }

        public Cliente getCliente() { return cliente; }
        public String getClasificacionFrecuencia() { return clasificacionFrecuencia; }
        public List<Cita> getHistorialCitas() { return historialCitas; }
        public List<TicketPOS> getHistorialComprasPOS() { return historialComprasPOS; }
    }

    private final ClienteRepositoryPort clienteRepository;
    private final CitaRepositoryPort citaRepository;
    private final VentaPOSRepositoryPort ventaPOSRepository;

    public ObtenerExpedienteClienteUseCase(
            ClienteRepositoryPort clienteRepository,
            CitaRepositoryPort citaRepository,
            VentaPOSRepositoryPort ventaPOSRepository) {
        this.clienteRepository = clienteRepository;
        this.citaRepository = citaRepository;
        this.ventaPOSRepository = ventaPOSRepository;
    }

    public ExpedienteCliente ejecutar(int idCliente) {
        Cliente cliente = clienteRepository.buscarPorIdUsuario(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("El cliente con ID " + idCliente + " no está registrado."));

        List<Cita> citas = citaRepository.buscarPorCliente(idCliente);
        List<TicketPOS> compras = ventaPOSRepository.buscarPorCliente(idCliente);

        return new ExpedienteCliente(
                cliente,
                cliente.calcularFrecuenciaVisita(),
                citas,
                compras
        );
    }
}
