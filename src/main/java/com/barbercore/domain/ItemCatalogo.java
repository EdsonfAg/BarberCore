package com.barbercore.domain;

public class ItemCatalogo {

    public enum TipoItem {
        SERVICIO_CORTE,
        ARTICULO_RETAIL
    }

    private final int idItem;
    private final String nombre;
    private double precio;
    private final TipoItem tipo;
    private int stock;
    private final int duracionMin;

    public ItemCatalogo(int idItem, String nombre, double precio, TipoItem tipo, int stock, int duracionMin) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del ítem del catálogo no puede estar vacío.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de ítem es obligatorio.");
        }
        if (tipo == TipoItem.SERVICIO_CORTE && duracionMin <= 0) {
            throw new IllegalArgumentException("Un servicio de corte debe tener una duración en minutos mayor a cero.");
        }
        if (tipo == TipoItem.ARTICULO_RETAIL && stock < 0) {
            throw new IllegalArgumentException("El stock de un artículo no puede ser negativo.");
        }

        this.idItem = idItem;
        this.nombre = nombre;
        this.precio = precio;
        this.tipo = tipo;
        this.stock = (tipo == TipoItem.SERVICIO_CORTE) ? 0 : stock;
        this.duracionMin = (tipo == TipoItem.ARTICULO_RETAIL) ? 0 : duracionMin;
    }

    public boolean esServicio() {
        return this.tipo == TipoItem.SERVICIO_CORTE;
    }

    public boolean esArticuloRetail() {
        return this.tipo == TipoItem.ARTICULO_RETAIL;
    }

    public void descontarStock(int cantidad) {
        if (!esArticuloRetail()) {
            return;
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a descontar debe ser mayor a cero.");
        }
        if (this.stock < cantidad) {
            throw new IllegalStateException("Stock insuficiente para el artículo: " + this.nombre);
        }
        this.stock -= cantidad;
    }

    public void reabastecerStock(int cantidad) {
        if (!esArticuloRetail()) {
            throw new IllegalStateException("No se puede agregar stock físico a un servicio de corte.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a reabastecer debe ser positiva.");
        }
        this.stock += cantidad;
    }

    public int getIdItem() { return idItem; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public TipoItem getTipo() { return tipo; }
    public int getStock() { return stock; }
    public int getDuracionMin() { return duracionMin; }
}
