package com.barbercore.domain;

public class Usuario {

    public enum Rol {
        DUENO(3),
        BARBERO(2),
        CLIENTE(1);

        private final int nivelJerarquico;

        Rol(int nivelJerarquico) {
            this.nivelJerarquico = nivelJerarquico;
        }

        public int getNivelJerarquico() {
            return nivelJerarquico;
        }
    }

    private final int id;
    private final String nombre;
    private final String email;
    private final Rol rol;

    public Usuario(int id, String nombre, String email, Rol rol) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del usuario no puede estar vacío.");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("El email del usuario no es válido.");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El usuario debe tener un rol asignado.");
        }
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    public boolean puedeGestionarPersonal() {
        return this.rol == Rol.DUENO;
    }

    public boolean puedeAdministrarCatalogo() {
        return this.rol == Rol.DUENO;
    }

    public boolean tieneJerarquiaMayorOIgualQue(Rol otroRol) {
        return this.rol.getNivelJerarquico() >= otroRol.getNivelJerarquico();
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
    public Rol getRol() { return rol; }
}
