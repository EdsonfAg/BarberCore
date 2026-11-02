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
 * Modalidad 2 (Barbero de Confianza): El cliente elige a su barbero preferido
 * sujeto a sus horarios de turno y disponibilidad sin choques.
 */
public class AgendarCitaConBarberoUseCase {

    private final BarberoRepositoryPort barberoRepository;
    private final CitaRepositoryPort citaRepository;
    private final CatalogoRepositoryPort catalogoRepository;

    public AgendarCitaConBarberoUseCase(
            BarberoRepositoryPort barberoRepository,
            CitaRepositoryPort citaRepository,
            CatalogoRepositoryPort catalogoRepository) {
        this.barberoRepository = barberoRepository;
        this.citaRepository = citaRepository;
        this.catalogoRepository = catalogoRepository;
    }

    public Cita ejecutar(int idNuevaCita, int idCliente, int idBarberoElegido, int idServicio, LocalDateTime fechaHoraInicio) {
        Barbero barbero = barberoRepository.buscarPorId(idBarberoElegido)
                .orElseThrow(() -> new IllegalArgumentException("El barbero seleccionado no existe."));

        ItemCatalogo servicio = catalogoRepository.buscarPorId(idServicio)
                .orElseThrow(() -> new IllegalArgumentException("El servicio solicitado no existe."));

        if (!servicio.esServicio()) {
            throw new IllegalArgumentException("El ítem seleccionado no es un servicio de corte.");
        }

        LocalDateTime fechaHoraFin = fechaHoraInicio.plusMinutes(servicio.getDuracionMin());

        if (!barbero.trabajaEnHorario(fechaHoraInicio.toLocalDate(), fechaHoraInicio.toLocalTime(), fechaHoraFin.toLocalTime())) {
            throw new IllegalStateException("El barbero " + barbero.getNombre() + " no tiene turno laboral en ese horario.");
        }

        List<Cita> citasDelDia = citaRepository.buscarPorFecha(fechaHoraInicio.toLocalDate());
        Cita nuevaCita = new Cita(idNuevaCita, idCliente, barbero.getIdBarbero(), fechaHoraInicio, fechaHoraFin);

        for (Cita citaExistente : citasDelDia) {
            if (nuevaCita.seCruzaCon(citaExistente)) {
                throw new IllegalStateException("El barbero " + barbero.getNombre() + " ya tiene una cita agendada en ese horario.");
            }
        }

        citaRepository.guardar(nuevaCita);
        return nuevaCita;
    }
}
