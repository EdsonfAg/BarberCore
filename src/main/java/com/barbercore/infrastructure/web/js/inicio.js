/*
 * Página de inicio: llena la barra de reserva, el catálogo y los barberos.
 * Si la dirección trae ?servicio=, ?barbero= u ?hora=, la barra se rellena sola.
 * Todavía no envía nada al sistema: solo muestra un aviso.
 */
(async function () {
    "use strict";

    const { escapar, tarjetaItem, tarjetaBarbero, mostrarAviso } = window.Componentes;
    const PENDIENTE = "Las reservas estarán disponibles cuando la página se conecte al sistema.";

    const [catalogo, barberos] = await Promise.all([
        window.Datos.obtenerCatalogo(),
        window.Datos.obtenerBarberos()
    ]);
    const servicios = catalogo.filter((item) => item.tipo === "SERVICIO_CORTE");

    // ---------- Barra de reserva ----------
    const selectBarbero = document.getElementById("reserva-barbero");
    const selectServicio = document.getElementById("reserva-servicio");
    const campoFecha = document.getElementById("reserva-fecha");

    selectBarbero.insertAdjacentHTML("beforeend", barberos
        .map((b) => `<option value="${b.idBarbero}">${escapar(b.nombre)}</option>`).join(""));
    selectServicio.insertAdjacentHTML("beforeend", servicios
        .map((s) => `<option value="${s.idItem}">${escapar(s.nombre)}</option>`).join(""));

    // Fecha local en el formato de <input type="datetime-local">: "2026-11-25T16:00"
    function formatoLocal(fecha) {
        const copia = new Date(fecha);
        copia.setMinutes(copia.getMinutes() - copia.getTimezoneOffset());
        return copia.toISOString().slice(0, 16);
    }

    const ahora = new Date();
    campoFecha.min = formatoLocal(ahora);

    // ---------- Datos que llegan desde otras páginas ----------
    const parametros = new URLSearchParams(location.search);
    if (parametros.has("servicio")) {
        selectServicio.value = parametros.get("servicio");
    }
    if (parametros.has("barbero")) {
        selectBarbero.value = parametros.get("barbero");
    }
    const hora = parametros.get("hora");
    if (/^\d{2}:\d{2}$/.test(hora || "")) {
        // Próxima vez que llegue esa hora: hoy si aún no pasa, si no mañana.
        const [h, m] = hora.split(":").map(Number);
        const fecha = new Date(ahora);
        fecha.setHours(h, m, 0, 0);
        if (fecha <= ahora) {
            fecha.setDate(fecha.getDate() + 1);
        }
        campoFecha.value = formatoLocal(fecha);
    }
    if (parametros.toString()) {
        (selectServicio.value ? campoFecha : selectServicio).focus({ preventScroll: true });
    }

    document.getElementById("form-reserva").addEventListener("submit", (evento) => {
        evento.preventDefault();
        mostrarAviso(PENDIENTE);
    });

    // ---------- Catálogo con pestañas ----------
    const lista = document.getElementById("lista-catalogo");
    const enlaceCatalogo = document.getElementById("enlace-catalogo");
    const pestanas = document.querySelectorAll(".pestanas button");

    function mostrarCatalogo(tipo) {
        pestanas.forEach((p) => p.setAttribute("aria-pressed", String(p.dataset.tipo === tipo)));
        enlaceCatalogo.href = tipo === "SERVICIO_CORTE" ? "servicios.html" : "productos.html";
        lista.innerHTML = catalogo.filter((item) => item.tipo === tipo).slice(0, 3).map(tarjetaItem).join("");
    }

    pestanas.forEach((p) => p.addEventListener("click", () => mostrarCatalogo(p.dataset.tipo)));
    mostrarCatalogo("SERVICIO_CORTE");

    // ---------- Barberos ----------
    document.getElementById("lista-barberos").innerHTML =
        barberos.slice(0, 3).map((b) => tarjetaBarbero(b, false)).join("");
})();
