package com.siav_pisip.siav_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/saldoVacaciones")
public class SaldoVacacionesController {

	@GetMapping
	public String leerpagina() {
		return "saldoVacaciones/listarSaldoVacaciones";
	}

	@GetMapping("/crearSaldoVacaciones")
	public String leerpaginacrear() {
		return "saldoVacaciones/crearSaldoVacaciones";
	}

	@GetMapping("/editarSaldoVacaciones")
	public String leerpaginaeditar() {
		return "saldoVacaciones/editarSaldoVacaciones";
	}

}