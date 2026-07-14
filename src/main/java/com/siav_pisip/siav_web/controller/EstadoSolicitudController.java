package com.siav_pisip.siav_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estadoSolicitud")
public class EstadoSolicitudController {

	@GetMapping
	public String leerpagina() {
		return "estadoSolicitud/listarEstadoSolicitud";
	}

	@GetMapping("/crearEstadoSolicitud")
	public String leerpaginacrear() {
		return "estadoSolicitud/crearEstadoSolicitud";
	}

	@GetMapping("/editarEstadoSolicitud")
	public String leerpaginaeditar() {
		return "estadoSolicitud/editarEstadoSolicitud";
	}

}
