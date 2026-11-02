package com.barbercore.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Barbero {

    private final int idBarbero;
    private final String nombre;
    private final List<Turno> horariosTurno;

    public Barbero(int idBarbero, String nombre, List<Turno> horariosTurno) {
        if (idBarbero <= 0) {
            throw new IllegalArgumentException("El ID del barbero debe ser positivo.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del barbero no puede estar vacío.");
        }
        this.idBarbero = idBarbero;
        this.nombre = nombre;
        this.horariosTurno = (horariosTurno == null) ? new ArrayList<>() : new ArrayList<>(horariosTurno);
    }

    public void agregarTurno(Turno nuevoTurno) {
        if (nuevoTurno == null) {
            throw new IllegalArgumentException("El turno no puede ser nulo.");
        }
        this.horariosTurno.add(nuevoTurno);
    }

    public boolean trabajaEnHorario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        if (fecha == null || horaInicio == null || horaFin == null || !horaFin.isAfter(horaInicio)) {
            return false;
        }
        for (Turno turno : this.horariosTurno) {
            if (turno.cubreHorario(fecha.getDayOfWeek(), horaInicio, horaFin)) {
                return true;
            }
        }
        return false;
    }

    public int getIdBarbero() { return idBarbero; }
    public String getNombre() { return nombre; }
    public List<Turno> getHorariosTurno() { return Collections.unmodifiableList(horariosTurno); }
}
