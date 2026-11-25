/*
 * Piezas reutilizables: tarjetas de catálogo, tarjetas de barbero y avisos.
 *
 * Los botones "Reservar" y "Agendar con..." llevan a la barra de reserva de
 * la página de inicio con los datos elegidos en la dirección, por ejemplo:
 *   index.html?servicio=3#reservar
 *   index.html?barbero=2&hora=16:00#reservar
 */
(function () {
    "use strict";

    const PENDIENTE_APARTADO = "El apartado de productos estará disponible cuando la página se conecte al sistema.";

    function escapar(texto) {
        const div = document.createElement("div");
        div.textContent = String(texto);
        return div.innerHTML;
    }

    function precio(valor) {
        return "$" + Number(valor).toLocaleString("es-MX");
    }

    // "16:00" -> "4:00 pm"
    function hora12(hhmm) {
        const [h, m] = hhmm.split(":").map(Number);
        const sufijo = h < 12 ? "am" : "pm";
        return `${((h + 11) % 12) + 1}:${String(m).padStart(2, "0")} ${sufijo}`;
    }

    function tarjetaItem(item) {
        const esServicio = item.tipo === "SERVICIO_CORTE";
        const duracion = esServicio ? `${item.duracionMin} min` : "";
        const accion = esServicio
            ? `<a class="boton" href="index.html?servicio=${item.idItem}#reservar">Reservar</a>`
            : `<button type="button" class="boton" data-accion="apartar" data-id-item="${item.idItem}">Apartar en sucursal</button>`;
        return `
            <article class="tarjeta">
                <div class="tarjeta-imagen" role="img" aria-label="Imagen de ${escapar(item.nombre)}"></div>
                <div class="tarjeta-cuerpo">
                    <h3>${escapar(item.nombre)}</h3>
                    <p class="tarjeta-duracion">${duracion}</p>
                    <p class="tarjeta-descripcion">${escapar(item.descripcion)}</p>
                    <p class="tarjeta-precio">${precio(item.precio)}</p>
                    ${accion}
                </div>
            </article>`;
    }

    function tarjetaBarbero(barbero, conHorarios) {
        const nombreCorto = barbero.nombre.split(" ")[0];
        const horarios = conHorarios ? `
                <h4>Horarios Disponibles</h4>
                <div class="horarios" role="group" aria-label="Horarios de ${escapar(barbero.nombre)}">
                    ${barbero.horariosDisponibles.map((h) =>
                        `<button type="button" class="horario" data-hora="${h}" aria-pressed="false">${hora12(h)}</button>`).join("")}
                </div>` : "";
        return `
            <article class="barbero">
                <div class="barbero-foto" role="img" aria-label="Foto de ${escapar(barbero.nombre)}"></div>
                <h3>${escapar(barbero.nombre)}</h3>
                <p>${escapar(barbero.especialidad)}</p>
                ${horarios}
                <a class="boton" data-id-barbero="${barbero.idBarbero}"
                   href="index.html?barbero=${barbero.idBarbero}#reservar">Agendar con ${escapar(nombreCorto)}</a>
            </article>`;
    }

    let temporizador;
    function mostrarAviso(mensaje) {
        const aviso = document.getElementById("aviso");
        if (!aviso) {
            return;
        }
        aviso.textContent = mensaje;
        clearTimeout(temporizador);
        temporizador = setTimeout(() => { aviso.textContent = ""; }, 4000);
    }

    // Botones comunes a todas las páginas
    document.addEventListener("click", (evento) => {
        if (evento.target.closest('[data-accion="apartar"]')) {
            mostrarAviso(PENDIENTE_APARTADO);
            return;
        }

        // Elegir un horario marca ese botón y lo agrega al enlace "Agendar con..."
        const horario = evento.target.closest(".horario");
        if (horario) {
            const tarjeta = horario.closest(".barbero");
            const elegido = horario.getAttribute("aria-pressed") !== "true";
            tarjeta.querySelectorAll(".horario").forEach((h) => h.setAttribute("aria-pressed", "false"));
            horario.setAttribute("aria-pressed", String(elegido));

            const enlace = tarjeta.querySelector("[data-id-barbero]");
            const hora = elegido ? `&hora=${horario.dataset.hora}` : "";
            enlace.href = `index.html?barbero=${enlace.dataset.idBarbero}${hora}#reservar`;
        }
    });

    window.Componentes = { escapar, precio, hora12, tarjetaItem, tarjetaBarbero, mostrarAviso };
})();
