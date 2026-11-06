/* Buscador: se oculta al hacer una acción (clic fuera, elegir un resultado,
   Escape, volver con el botón atrás) y solo se abre al escribir o enfocar
   cuando el campo tiene texto. */
(function () {
    function panel() { return document.querySelector('.buscador-wrap'); }
    function campo() { return document.querySelector('.buscador-input'); }

    function abrir() {
        var p = panel(), i = campo();
        if (!p || !i || !i.value.trim()) { return; }
        p.classList.remove('busqueda-cerrada');
    }

    function cerrar(limpiar) {
        var p = panel(), i = campo();
        if (p) { p.classList.add('busqueda-cerrada'); }
        if (limpiar && i) { i.value = ''; }
    }

    window.mostrarResultadosVinilo = abrir;

    document.addEventListener('click', function (e) {
        var t = e.target;
        if (!t.closest) { return; }

        if (!t.closest('.buscador-wrap')) {
            cerrar(true);                    // clic fuera
        } else if (t.closest('.resultado')) {
            setTimeout(function () { cerrar(false); }, 0);   // eligió un resultado
        }
    });

    document.addEventListener('focusin', function (e) {
        if (e.target.matches && e.target.matches('.buscador-input')) { abrir(); }
    });

    document.addEventListener('input', function (e) {
        if (e.target.matches && e.target.matches('.buscador-input')) { abrir(); }
    });

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            cerrar(true);
            var i = campo();
            if (i) { i.blur(); }
        }
    });

    // Al volver con el botón "atrás" no debe aparecer abierto
    window.addEventListener('pageshow', function () { cerrar(true); });
})();
