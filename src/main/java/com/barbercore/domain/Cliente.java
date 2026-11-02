package com.barbercore.domain;

public class Cliente {

    private final int idUsuario;
    private String preferenciasCorte;
    private int totalAsistencias;

    public Cliente(int idUsuario, String preferenciasCorte, int totalAsistencias) {
        if (idUsuario <= 0) {
            throw new IllegalArgumentException("El ID de usuario del cliente debe ser válido.");
        }
        if (totalAsistencias < 0) {
            throw new IllegalArgumentException("El total de asistencias no puede ser negativo.");
        }
        this.idUsuario = idUsuario;
        this.preferenciasCorte = (preferenciasCorte == null) ? "" : preferenciasCorte.trim();
        this.totalAsistencias = totalAsistencias;
    }

    public void actualizarPreferenciasCorte(String nuevasPreferencias) {
        if (nuevasPreferencias == null || nuevasPreferencias.isBlank()) {
            throw new IllegalArgumentException("Las preferencias de corte no pueden estar vacías.");
        }
        this.preferenciasCorte = nuevasPreferencias.trim();
    }

    public void registrarAsistenciaEfectiva() {
        this.totalAsistencias++;
    }

    public String calcularFrecuenciaVisita() {
        if (this.totalAsistencias >= 10) {
            return "CLIENTE_VIP_HABITUAL";
        } else if (this.totalAsistencias >= 3) {
            return "CLIENTE_FRECUENTE";
        }
        return "CLIENTE_NUEVO";
    }

    public int getIdUsuario() { return idUsuario; }
    public String getPreferenciasCorte() { return preferenciasCorte; }
    public int getTotalAsistencias() { return totalAsistencias; }
}
