/*
 * Menú superior y pie de página compartidos.
 * Cada página solo necesita <header id="encabezado"> y <footer id="pie">.
 */
(function () {
    "use strict";

    const paginas = [
        { archivo: "servicios.html", texto: "Servicios" },
        { archivo: "barberos.html", texto: "Nuestros Barberos" },
        { archivo: "productos.html", texto: "Productos" },
        { archivo: "ubicacion.html", texto: "Ubicación" }
    ];

    const paginaActual = location.pathname.split("/").pop() || "index.html";

    const logo = `
        <a class="logo" href="index.html">
            <img src="../img/logo.svg" alt="" width="30" height="32">
            <span>BarberCore</span>
        </a>`;

    function enlace(pagina) {
        const actual = pagina.archivo === paginaActual ? ' aria-current="page"' : "";
        return `<li><a href="${pagina.archivo}"${actual}>${pagina.texto}</a></li>`;
    }

    const encabezado = document.getElementById("encabezado");
    encabezado.className = "menu";
    encabezado.innerHTML = `
        <div class="menu-contenido">
            ${logo}
            <button type="button" class="menu-boton" aria-expanded="false" aria-controls="navegacion">Menú</button>
            <nav class="menu-navegacion" id="navegacion" aria-label="Principal">
                <ul class="menu-enlaces">${paginas.map(enlace).join("")}</ul>
                <div class="menu-acciones">
                    <a href="login.html">Iniciar Sesión</a>
                    <a class="boton" href="index.html#reservar">Agendar Cita</a>
                </div>
            </nav>
        </div>`;

    const botonMenu = encabezado.querySelector(".menu-boton");
    const navegacion = encabezado.querySelector(".menu-navegacion");

    function abrirMenu(abierto) {
        navegacion.classList.toggle("abierto", abierto);
        botonMenu.setAttribute("aria-expanded", String(abierto));
    }

    botonMenu.addEventListener("click", () => abrirMenu(!navegacion.classList.contains("abierto")));
    navegacion.addEventListener("click", (evento) => {
        if (evento.target.closest("a")) {
            abrirMenu(false);
        }
    });

    const pie = document.getElementById("pie");
    pie.className = "pie";
    pie.innerHTML = `
        <div class="contenedor pie-contenido">
            <div>
                ${logo}
                <p class="pie-derechos">© 2026 BarberCore. Todos los derechos reservados</p>
            </div>
            <div>
                <h4>Horarios y Ubicación</h4>
                <ul>
                    <li>Av. Hidalgo #402, Col. Centro</li>
                    <li>Lun - Vie: 10:00 AM – 8:00 PM</li>
                    <li>Sábados: 9:00 AM – 7:00 PM</li>
                    <li>Domingos: Cerrado</li>
                </ul>
            </div>
            <div>
                <h4>Navegación</h4>
                <ul>
                    <li><a href="index.html">Inicio</a></li>
                    ${paginas.map(enlace).join("")}
                </ul>
            </div>
            <div>
                <h4>Redes Sociales</h4>
                <ul>
                    <li><a href="#">Twitter</a></li>
                    <li><a href="#">Facebook</a></li>
                    <li><a href="#">Instagram</a></li>
                </ul>
            </div>
            <div>
                <h4>Legal</h4>
                <ul>
                    <li><a href="#">Políticas de Apartado (5 días)</a></li>
                    <li><a href="#">Aviso de Privacidad</a></li>
                    <li><a href="#">Términos de Servicio</a></li>
                </ul>
            </div>
        </div>`;
})();
