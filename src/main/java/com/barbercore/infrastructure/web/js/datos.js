/*
 * DATOS DE EJEMPLO (todavía sin conexión con Java).
 *
 * Los nombres de los campos coinciden con las clases de com.barbercore.domain
 * (ItemCatalogo y Barbero). Estos campos aparecen en el diseño pero todavía
 * no existen en Java: "descripcion", "paquete", "especialidad" y
 * "horariosDisponibles".
 *
 * Para conectar más adelante solo hay que cambiar el objeto "Datos" del final:
 * las páginas piden la información únicamente a través de él.
 */
(function () {
    "use strict";

    function servicio(idItem, nombre, precio, descripcion, paquete) {
        return { idItem, nombre, precio, tipo: "SERVICIO_CORTE", stock: 0, duracionMin: 30, descripcion, paquete };
    }

    function articulo(idItem, nombre, precio, descripcion) {
        return { idItem, nombre, precio, tipo: "ARTICULO_RETAIL", stock: 10, duracionMin: 0, descripcion };
    }

    const catalogo = [
        servicio(1, "Corte Clásico / Fade", 200,
            "Corte personalizado con máquina y tijera, desvanecido limpio y lavado refrescante.", false),
        servicio(2, "Corte Infantil / Escolar", 200,
            "Corte detallado y cómodo para niños menores de 12 años con acabado natural y peinado.", false),
        servicio(3, "Ritual de Barba y Vapor", 200,
            "Delineado a navaja, exfoliación facial con ozono y aplicación de bálsamo hidratante.", false),
        servicio(4, "Paquete Completo (Corte + Barba)", 200,
            "Corte de precisión más perfilado de barba con navaja tradicional y toalla caliente.", true),
        servicio(5, "Corte Exprés", 150,
            "Recorte rápido y estilizado con máquina profesional y acabado con peine.", true),
        servicio(6, "Experiencia Relax y Estilo", 250,
            "Tratamiento facial hidratante con masaje relajante y contorno de barba definido.", true),

        articulo(101, "Cera Mate Fijación Fuerte (100g)", 200,
            "Moldea el cabello con acabado natural sin brillo ni residuos grasos. Base agua."),
        articulo(102, "Gel Fijación Extra Fuerte (150ml)", 180,
            "Gel fijador de secado rápido con efecto mojado y brillo intenso."),
        articulo(103, "Pomada Texturizadora (90g)", 220,
            "Pomada ligera que aporta volumen y textura, ideal para peinados casuales."),
        // Pendiente: en el diseño este artículo tiene la descripción del gel.
        articulo(104, "Aceite para Barba 30ml", 180,
            "Descripción pendiente."),
        articulo(105, "Bálsamo Modelador de Barba (60g)", 210,
            "Control de frizz y nutrición profunda con karité."),
        articulo(106, "Tónico Capilar Refrescante (250ml)", 190,
            "Loción revitalizante con mentol para cuero cabelludo y peinado ligero.")
    ];

    // Horarios de ejemplo en formato 24 h ("HH:mm"), como LocalTime de Java.
    const barberos = [
        "Mateo Eliot",
        "Edson Aguilar",
        "Rodrigo Salazar",
        "Fernando Ortiz",
        "Alejandro Mendoza",
        "Carlos Eduardo Vega"
    ].map((nombre, indice) => ({
        idBarbero: indice + 1,
        nombre,
        especialidad: "Master Barber · Fade & Diseño",
        horariosDisponibles: ["11:00", "16:00", "18:30"]
    }));

    window.Datos = {
        obtenerCatalogo: () => Promise.resolve(catalogo),
        obtenerBarberos: () => Promise.resolve(barberos)
    };
})();
