package com.barbercore.application.usecases;

import com.barbercore.application.ports.BarberoRepositoryPort;
import com.barbercore.application.ports.UsuarioRepositoryPort;
import com.barbercore.domain.Barbero;
import com.barbercore.domain.Turno;
import com.barbercore.domain.Usuario;
import java.util.List;

/**
 * Control Jerárquico (Rol del Dueño): El Dueño tiene el control exclusivo
 * para dar de alta nuevos barberos y definir sus turnos.
 */
public class RegistrarBarberoYTurnoUseCase {

    private final UsuarioRepositoryPort usuarioRepository;
    private final BarberoRepositoryPort barberoRepository;

    public RegistrarBarberoYTurnoUseCase(
            UsuarioRepositoryPort usuarioRepository,
            BarberoRepositoryPort barberoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.barberoRepository = barberoRepository;
    }

    public Barbero ejecutar(int idUsuarioEjecutor, int idNuevoBarbero, String nombreBarbero, List<Turno> turnosIniciales) {
        Usuario ejecutor = usuarioRepository.buscarPorId(idUsuarioEjecutor)
                .orElseThrow(() -> new IllegalArgumentException("El usuario ejecutor no existe."));

        if (!ejecutor.puedeGestionarPersonal()) {
            throw new SecurityException("Acceso denegado: Solo el Dueño puede dar de alta barberos y definir turnos.");
        }

        Barbero nuevoBarbero = new Barbero(idNuevoBarbero, nombreBarbero, turnosIniciales);
        barberoRepository.guardar(nuevoBarbero);
        return nuevoBarbero;
    }
}
