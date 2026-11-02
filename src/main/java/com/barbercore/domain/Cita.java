package com.barbercore.domain;

import java.time.LocalDateTime;

public class Cita {

    public enum EstadoCita {
        AGENDADA,
        ASISTIDA,
        CANCELADA
    }

    private final int idCita;
    private final int idCliente;
    private final int idBarbero;
    private final LocalDateTime fechaHoraInicio;
    private final LocalDateTime fechaHoraFin;
    private EstadoCita estado;

    public Cita(int idCita, int idCliente, int idBarbero, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin) {
        if (fechaHoraInicio == null || fechaHoraFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin de la cita son obligatorias.");
        }
        if (!fechaHoraFin.isAfter(fechaHoraInicio)) {
            throw new IllegalArgumentException("La fecha/hora de fin debe ser posterior a la de inicio.");
        }
        this.idCita = idCita;
        this.idCliente = idCliente;
        this.idBarbero = idBarbero;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = EstadoCita.AGENDADA;
    }

    public boolean seCruzaCon(Cita otraCita) {
        if (otraCita == null) {
            return false;
        }
        if (this.estado == EstadoCita.CANCELADA || otraCita.getEstado() == EstadoCita.CANCELADA) {
            return false;
        }
        if (this.idBarbero != otraCita.getIdBarbero()) {
            return false;
        }
        return this.seCruzaConHorario(otraCita.getFechaHoraInicio(), otraCita.getFechaHoraFin());
    }

    public boolean seCruzaConHorario(LocalDateTime otroInicio, LocalDateTime otroFin) {
        if (this.estado == EstadoCita.CANCELADA) {
            return false;
        }
        return this.fechaHoraInicio.isBefore(otroFin) && otroInicio.isBefore(this.fechaHoraFin);
    }

    public void registrarCheckIn() {
        if (this.estado == EstadoCita.CANCELADA) {
            throw new IllegalStateException("No se puede hacer Check-in de una cita cancelada.");
        }
        this.estado = EstadoCita.ASISTIDA;
    }

    public void cancelar() {
        if (this.estado == EstadoCita.ASISTIDA) {
            throw new IllegalStateException("No se puede cancelar una cita a la que el cliente ya asistió.");
        }
        this.estado = EstadoCita.CANCELADA;
    }

    public int getIdCita() { return idCita; }
    public int getIdCliente() { return idCliente; }
    public int getIdBarbero() { return idBarbero; }
    public LocalDateTime getFechaHoraInicio() { return fechaHoraInicio; }
    public LocalDateTime getFechaHoraFin() { return fechaHoraFin; }
    public EstadoCita getEstado() { return estado; }
}
