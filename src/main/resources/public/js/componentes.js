/*
 * Piezas reutilizables: tarjetas de catálogo, tarjetas de barbero y avisos.
 * Las usarán también las páginas de Servicios, Productos y Barberos.
 */
(function () {
    "use strict";

    function escapar(texto) {
        const div = document.createElement("div");
        div.textContent = String(texto);
        return div.innerHTML;
    }

    function precio(valor) {
        return "$" + Number(valor).toLocaleString("es-MX");
    }

    function tarjetaItem(item) {
        const esServicio = item.tipo === "SERVICIO_CORTE";
        const duracion = esServicio ? `${item.duracionMin} min` : "";
        const textoBoton = esServicio ? "Reservar" : "Apartar en sucursal";
        return `
            <article class="tarjeta">
                <div class="tarjeta-imagen" role="img" aria-label="Imagen de ${escapar(item.nombre)}"></div>
                <div class="tarjeta-cuerpo">
                    <h3>${escapar(item.nombre)}</h3>
                    <p class="tarjeta-duracion">${duracion}</p>
                    <p class="tarjeta-descripcion">${escapar(item.descripcion)}</p>
                    <p class="tarjeta-precio">${precio(item.precio)}</p>
                    <button type="button" class="boton" data-accion="${esServicio ? "reservar" : "apartar"}"
                            data-id-item="${item.idItem}">${textoBoton}</button>
                </div>
            </article>`;
    }

    function tarjetaBarbero(barbero) {
        const nombreCorto = barbero.nombre.split(" ")[0];
        return `
            <article class="barbero">
                <div class="barbero-foto" role="img" aria-label="Foto de ${escapar(barbero.nombre)}"></div>
                <h3>${escapar(barbero.nombre)}</h3>
                <p>${escapar(barbero.especialidad)}</p>
                <button type="button" class="boton" data-accion="agendar-barbero"
                        data-id-barbero="${barbero.idBarbero}">Agendar con ${escapar(nombreCorto)}</button>
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

    window.Componentes = { escapar, precio, tarjetaItem, tarjetaBarbero, mostrarAviso };
})();
