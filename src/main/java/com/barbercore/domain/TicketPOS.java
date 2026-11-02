package com.barbercore.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPOS {

    public static class LineaVenta {
        private final ItemCatalogo item;
        private final int cantidad;
        private final double precioUnitarioAplicado;

        public LineaVenta(ItemCatalogo item, int cantidad) {
            if (item == null) {
                throw new IllegalArgumentException("El ítem de la línea de venta no puede ser nulo.");
            }
            if (cantidad <= 0) {
                throw new IllegalArgumentException("La cantidad debe ser al menos 1.");
            }
            this.item = item;
            this.cantidad = cantidad;
            this.precioUnitarioAplicado = item.getPrecio();
        }

        public double calcularSubtotal() {
            return this.precioUnitarioAplicado * this.cantidad;
        }

        public ItemCatalogo getItem() { return item; }
        public int getCantidad() { return cantidad; }
        public double getPrecioUnitarioAplicado() { return precioUnitarioAplicado; }
    }

    private final int idTicket;
    private final int idCliente;
    private final Integer idCitaOrigen;
    private final List<LineaVenta> lineasVenta;

    public TicketPOS(int idTicket, int idCliente, Integer idCitaOrigen) {
        if (idCliente <= 0) {
            throw new IllegalArgumentException("El ticket debe estar vinculado a un cliente válido.");
        }
        this.idTicket = idTicket;
        this.idCliente = idCliente;
        this.idCitaOrigen = idCitaOrigen;
        this.lineasVenta = new ArrayList<>();
    }

    public void agregarLinea(ItemCatalogo item, int cantidad) {
        if (item == null) {
            throw new IllegalArgumentException("El ítem no puede ser nulo.");
        }
        if (item.esArticuloRetail()) {
            item.descontarStock(cantidad);
        }
        this.lineasVenta.add(new LineaVenta(item, cantidad));
    }

    public double calcularTotal() {
        double total = 0.0;
        for (LineaVenta linea : this.lineasVenta) {
            total += linea.calcularSubtotal();
        }
        return total;
    }

    public int getIdTicket() { return idTicket; }
    public int getIdCliente() { return idCliente; }
    public Integer getIdCitaOrigen() { return idCitaOrigen; }
    public List<LineaVenta> getLineasVenta() { return Collections.unmodifiableList(lineasVenta); }
}
