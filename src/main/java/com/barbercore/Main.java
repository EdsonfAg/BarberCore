package com.barbercore;

import com.barbercore.domain.ItemCatalogo;
import com.barbercore.domain.TicketPOS;
import com.barbercore.domain.Usuario;

public class Main {
    public static void main(String[] args) {
        // Variables de entorno inyectadas desde docker-compose.yml para la Capa 4
        String serverPort = System.getenv().getOrDefault("SERVER_PORT", "8080");
        String dbHost = System.getenv().getOrDefault("DB_HOST", "localhost");
        String dbPort = System.getenv().getOrDefault("DB_PORT", "3306");
        String dbName = System.getenv().getOrDefault("DB_NAME", "barbercore_db");

        System.out.println("====================================================");
        System.out.println("   BARBERCORE - CLEAN ARCHITECTURE CONTAINER READY  ");
        System.out.println("====================================================");
        System.out.println("[Config] Puerto Web asignado : " + serverPort);
        System.out.println("[Config] Destino Base Datos  : " + dbHost + ":" + dbPort + "/" + dbName);

        // Verificación rápida de las capas centrales (Dominio)
        Usuario admin = new Usuario(1, "Dueño Barbería", "admin@barbercore.com", Usuario.Rol.DUENO);
        ItemCatalogo corte = new ItemCatalogo(10, "Corte Clásico", 200.0, ItemCatalogo.TipoItem.SERVICIO_CORTE, 0, 30);
        ItemCatalogo cera = new ItemCatalogo(20, "Cera Mate", 150.0, ItemCatalogo.TipoItem.ARTICULO_RETAIL, 10, 0);

        TicketPOS ticketPrueba = new TicketPOS(1, 99, null);
        ticketPrueba.agregarLinea(corte, 1);
        ticketPrueba.agregarLinea(cera, 1);

        System.out.println("[Core OK] Permiso Dueño verificado: " + admin.puedeGestionarPersonal());
        System.out.println("[Core OK] Simulación POS Total    : $" + ticketPrueba.calcularTotal()
                + " (Stock restante de cera: " + cera.getStock() + ")");

        // =========================================================================
        // ZONA DE ENSAMBLAJE PARA CAPA 4 (INFRAESTRUCTURA: BD y WEB)
        // Aquí los compañeros de Infraestructura conectarán sus clases:
        // 1. Instanciar repositorios SQL implementando los puertos de application/ports
        // 2. Inyectar los repositorios en los Casos de Uso de application/usecases
        // 3. Levantar el servidor Web en el puerto "serverPort"
        // =========================================================================
        System.out.println("====================================================");
    }
}
