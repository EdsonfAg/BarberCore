/*
 * Página de inicio: llena la barra de reserva, el catálogo y los barberos.
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

    const ahora = new Date();
    ahora.setMinutes(ahora.getMinutes() - ahora.getTimezoneOffset());
    campoFecha.min = ahora.toISOString().slice(0, 16);

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
    document.getElementById("lista-barberos").innerHTML = barberos.slice(0, 3).map(tarjetaBarbero).join("");

    // ---------- Botones de las tarjetas ----------
    document.querySelector("main").addEventListener("click", (evento) => {
        const boton = evento.target.closest("[data-accion]");
        if (!boton) {
            return;
        }
        if (boton.dataset.accion === "reservar") {
            selectServicio.value = boton.dataset.idItem;
        } else if (boton.dataset.accion === "agendar-barbero") {
            selectBarbero.value = boton.dataset.idBarbero;
        } else {
            mostrarAviso("El apartado de productos estará disponible cuando la página se conecte al sistema.");
            return;
        }
        document.getElementById("reservar").scrollIntoView();
        campoFecha.focus({ preventScroll: true });
    });
})();
