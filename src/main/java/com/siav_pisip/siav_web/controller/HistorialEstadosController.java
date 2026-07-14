package com.siav_pisip.siav_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/historialEstados")
public class HistorialEstadosController {

	@GetMapping
	public String leerpagina() {
		return "historialEstados/listarHistorialEstados";
	}

	@GetMapping("/crearHistorialEstados")
	public String leerpaginacrear() {
		return "historialEstados/crearHistorialEstados";
	}

	@GetMapping("/editarHistorialEstados")
	public String leerpaginaeditar() {
		return "historialEstados/editarHistorialEstados";
	}

}