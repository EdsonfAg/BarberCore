package com.barbercore.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class Turno {

    private final DayOfWeek diaSemana;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;

    public Turno(DayOfWeek diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        if (diaSemana == null || horaInicio == null || horaFin == null) {
            throw new IllegalArgumentException("El día, hora de inicio y hora de fin son obligatorios.");
        }
        if (!horaFin.isAfter(horaInicio)) {
            throw new IllegalArgumentException("La hora de fin del turno debe ser posterior a la hora de inicio.");
        }
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public boolean cubreHorario(DayOfWeek dia, LocalTime inicioSolicitado, LocalTime finSolicitado) {
        if (this.diaSemana != dia) {
            return false;
        }
        boolean iniciaDentro = !inicioSolicitado.isBefore(this.horaInicio);
        boolean terminaDentro = !finSolicitado.isAfter(this.horaFin);
        return iniciaDentro && terminaDentro;
    }

    public DayOfWeek getDiaSemana() { return diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
}
