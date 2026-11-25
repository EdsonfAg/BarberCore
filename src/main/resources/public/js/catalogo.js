/*
 * Páginas de Servicios y Productos: dibujan las tarjetas del catálogo.
 * Cada página solo tiene algunos de estos contenedores; los demás se ignoran.
 */
(async function () {
    "use strict";

    const { tarjetaItem } = window.Componentes;
    const catalogo = await window.Datos.obtenerCatalogo();

    function llenar(id, items) {
        const contenedor = document.getElementById(id);
        if (contenedor) {
            contenedor.innerHTML = items.length
                ? items.map(tarjetaItem).join("")
                : '<p class="vacio">No encontramos productos con esa búsqueda.</p>';
        }
        return contenedor;
    }

    // ---------- Servicios y paquetes ----------
    const servicios = catalogo.filter((item) => item.tipo === "SERVICIO_CORTE");
    llenar("lista-servicios", servicios.filter((s) => !s.paquete));
    llenar("lista-paquetes", servicios.filter((s) => s.paquete));

    // ---------- Productos con buscador ----------
    const productos = catalogo.filter((item) => item.tipo === "ARTICULO_RETAIL");
    if (llenar("lista-productos", productos)) {
        const sinAcentos = (texto) => texto.normalize("NFD").replace(/[̀-ͯ]/g, "").toLowerCase();

        document.getElementById("buscar-producto").addEventListener("input", (evento) => {
            const busqueda = sinAcentos(evento.target.value.trim());
            llenar("lista-productos", productos.filter((p) =>
                sinAcentos(`${p.nombre} ${p.descripcion}`).includes(busqueda)));
        });
    }
})();
