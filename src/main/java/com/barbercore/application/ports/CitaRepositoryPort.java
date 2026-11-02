package com.barbercore.application.ports;

import com.barbercore.domain.Cita;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CitaRepositoryPort {
    Optional<Cita> buscarPorId(int idCita);
    List<Cita> buscarPorFecha(LocalDate fecha);
    List<Cita> buscarPorCliente(int idCliente);
    void guardar(Cita cita);
}
