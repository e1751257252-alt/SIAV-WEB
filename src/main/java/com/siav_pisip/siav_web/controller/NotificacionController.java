package com.siav_pisip.siav_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/notificacion")
public class NotificacionController {

	@GetMapping
	public String leerpagina() {
		return "notificacion/listarNotificacion";
	}

	@GetMapping("/crearNotificacion")
	public String leerpaginacrear() {
		return "notificacion/crearNotificacion";
	}

	@GetMapping("/editarNotificacion")
	public String leerpaginaeditar() {
		return "notificacion/editarNotificacion";
	}

}