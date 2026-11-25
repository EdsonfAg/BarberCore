/*
 * Formularios de Iniciar Sesión, Registro y Restablecer Contraseña.
 * Revisan los campos en el navegador; todavía no envían nada al sistema.
 */
(function () {
    "use strict";

    const formulario = document.querySelector("[data-formulario]");
    if (!formulario) {
        return;
    }

    const error = formulario.querySelector(".error");
    const PENDIENTE = "Las cuentas estarán disponibles cuando la página se conecte al sistema.";

    function primerProblema() {
        for (const campo of formulario.elements) {
            // El navegador no marca "tooShort" si el valor no se escribió a mano
            // (por ejemplo, autocompletado), así que la longitud se revisa aquí.
            if (campo.minLength > 0 && campo.value && campo.value.length < campo.minLength) {
                campo.focus();
                return `La contraseña debe tener al menos ${campo.minLength} caracteres.`;
            }
            if (campo.validity && !campo.validity.valid) {
                campo.focus();
                if (campo.type === "checkbox") {
                    return "Debes aceptar los Términos y Condiciones.";
                }
                if (campo.validity.valueMissing) {
                    return "Completa todos los campos.";
                }
                if (campo.validity.typeMismatch) {
                    return "Escribe un correo electrónico válido.";
                }
                if (campo.validity.tooShort) {
                    return `La contraseña debe tener al menos ${campo.minLength} caracteres.`;
                }
                return "Revisa los datos del formulario.";
            }
        }

        const { password, confirmarPassword } = formulario.elements;
        if (confirmarPassword && password.value !== confirmarPassword.value) {
            confirmarPassword.focus();
            return "Las contraseñas no coinciden.";
        }
        return "";
    }

    formulario.addEventListener("submit", (evento) => {
        evento.preventDefault();
        error.textContent = primerProblema();
        if (!error.textContent) {
            window.Componentes.mostrarAviso(PENDIENTE);
        }
    });
})();
