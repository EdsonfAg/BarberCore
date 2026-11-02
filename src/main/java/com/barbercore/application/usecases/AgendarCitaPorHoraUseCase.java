package com.barbercore.application.usecases;

import com.barbercore.application.ports.BarberoRepositoryPort;
import com.barbercore.application.ports.CatalogoRepositoryPort;
import com.barbercore.application.ports.CitaRepositoryPort;
import com.barbercore.domain.Barbero;
import com.barbercore.domain.Cita;
import com.barbercore.domain.ItemCatalogo;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Modalidad 1 (Solo por Hora): El cliente elige fecha y hora;
 * el sistema asigna automáticamente a un barbero libre en turno sin choques.
 */
public class AgendarCitaPorHoraUseCase {

    private final BarberoRepositoryPort barberoRepository;
    private final CitaRepositoryPort citaRepository;
    private final CatalogoRepositoryPort catalogoRepository;

    public AgendarCitaPorHoraUseCase(
            BarberoRepositoryPort barberoRepository,
            CitaRepositoryPort citaRepository,
            CatalogoRepositoryPort catalogoRepository) {
        this.barberoRepository = barberoRepository;
        this.citaRepository = citaRepository;
        this.catalogoRepository = catalogoRepository;
    }

    public Cita ejecutar(int idNuevaCita, int idCliente, int idServicio, LocalDateTime fechaHoraInicio) {
        ItemCatalogo servicio = catalogoRepository.buscarPorId(idServicio)
                .orElseThrow(() -> new IllegalArgumentException("El servicio solicitado no existe."));

        if (!servicio.esServicio()) {
            throw new IllegalArgumentException("El ítem seleccionado no es un servicio de corte.");
        }

        LocalDateTime fechaHoraFin = fechaHoraInicio.plusMinutes(servicio.getDuracionMin());
        List<Barbero> barberos = barberoRepository.obtenerTodos();
        List<Cita> citasDelDia = citaRepository.buscarPorFecha(fechaHoraInicio.toLocalDate());

        for (Barbero barbero : barberos) {
            if (estaDisponible(barbero, fechaHoraInicio, fechaHoraFin, citasDelDia)) {
                Cita nuevaCita = new Cita(idNuevaCita, idCliente, barbero.getIdBarbero(), fechaHoraInicio, fechaHoraFin);
                citaRepository.guardar(nuevaCita);
                return nuevaCita;
            }
        }

        throw new IllegalStateException("No hay ningún barbero libre disponible para la fecha y hora seleccionadas.");
    }

    private boolean estaDisponible(Barbero barbero, LocalDateTime inicio, LocalDateTime fin, List<Cita> citasDelDia) {
        if (!barbero.trabajaEnHorario(inicio.toLocalDate(), inicio.toLocalTime(), fin.toLocalTime())) {
            return false;
        }
        for (Cita citaExistente : citasDelDia) {
            if (citaExistente.getIdBarbero() == barbero.getIdBarbero()
                    && citaExistente.seCruzaConHorario(inicio, fin)) {
                return false;
            }
        }
        return true;
    }
}
