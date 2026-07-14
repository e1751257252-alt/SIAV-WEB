package com.siav_pisip.siav_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/movimientoSaldo")
public class MovimientoSaldoController {

	@GetMapping
	public String leerpagina() {
		return "movimientoSaldo/listarMovimientoSaldo";
	}

	@GetMapping("/crearMovimientoSaldo")
	public String leerpaginacrear() {
		return "movimientoSaldo/crearMovimientoSaldo";
	}

	@GetMapping("/editarMovimientoSaldo")
	public String leerpaginaeditar() {
		return "movimientoSaldo/editarMovimientoSaldo";
	}

}