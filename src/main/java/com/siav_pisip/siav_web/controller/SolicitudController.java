package com.siav_pisip.siav_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/solicitud")
public class SolicitudController {

	@GetMapping
	public String leerpagina() {
		return "solicitud/listarSolicitud";
	}

	@GetMapping("/crearSolicitud")
	public String leerpaginacrear() {
		return "solicitud/crearSolicitud";
	}

	@GetMapping("/editarSolicitud")
	public String leerpaginaeditar() {
		return "solicitud/editarSolicitud";
	}

}
