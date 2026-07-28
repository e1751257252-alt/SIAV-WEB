(function () {
	function mostrarAviso(el, max) {
		var avisoId = 'aviso-limite-' + (el.id || Math.random().toString(36).slice(2));
		el.dataset.avisoId = el.dataset.avisoId || avisoId;
		var aviso = document.getElementById(el.dataset.avisoId);
		if (el.value.length >= max) {
			if (!aviso) {
				aviso = document.createElement('small');
				aviso.id = el.dataset.avisoId;
				aviso.className = 'text-danger d-block mt-1';
				el.insertAdjacentElement('afterend', aviso);
			}
			aviso.textContent = 'Límite de caracteres: ' + max;
		} else if (aviso) {
			aviso.remove();
		}
	}

	function aplicarSoloNumeros(el) {
		if (el.hasAttribute('data-solo-numeros')) {
			var limpio = el.value.replace(/[^0-9]/g, '');
			if (limpio !== el.value) {
				el.value = limpio;
			}
		}
	}

	function aplicarLimite(el) {
		var max = parseInt(el.getAttribute('maxlength') || el.getAttribute('data-maxlen'), 10);
		if (!max) {
			return;
		}
		if (el.value.length > max) {
			el.value = el.value.slice(0, max);
		}
		mostrarAviso(el, max);
	}

	document.addEventListener('input', function (event) {
		var el = event.target;
		if (el.tagName !== 'INPUT' && el.tagName !== 'TEXTAREA') {
			return;
		}
		aplicarSoloNumeros(el);
		if (el.hasAttribute('maxlength') || el.hasAttribute('data-maxlen')) {
			aplicarLimite(el);
		}
	});
})();
