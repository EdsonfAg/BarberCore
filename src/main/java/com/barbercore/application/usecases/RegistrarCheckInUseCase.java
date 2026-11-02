package com.barbercore.application.usecases;

import com.barbercore.application.ports.CitaRepositoryPort;
import com.barbercore.application.ports.ClienteRepositoryPort;
import com.barbercore.domain.Cita;
import com.barbercore.domain.Cliente;

/**
 * Bitácora de Asistencias: Guarda cada vez que el cliente asiste
 * efectivamente a su cita (Check-in) y actualiza su historial de frecuencia.
 */
public class RegistrarCheckInUseCase {

    private final CitaRepositoryPort citaRepository;
    private final ClienteRepositoryPort clienteRepository;

    public RegistrarCheckInUseCase(
            CitaRepositoryPort citaRepository,
            ClienteRepositoryPort clienteRepository) {
        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
    }

    public Cita ejecutar(int idCita) {
        Cita cita = citaRepository.buscarPorId(idCita)
                .orElseThrow(() -> new IllegalArgumentException("La cita indicada no existe."));

        cita.registrarCheckIn();
        citaRepository.guardar(cita);

        Cliente cliente = clienteRepository.buscarPorIdUsuario(cita.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("El perfil del cliente asociado a la cita no existe."));

        cliente.registrarAsistenciaEfectiva();
        clienteRepository.guardar(cliente);

        return cita;
    }
}
