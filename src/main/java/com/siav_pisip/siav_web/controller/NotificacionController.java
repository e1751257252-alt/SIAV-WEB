package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.response.NotificacionResponseDto;
import com.siav_pisip.siav_web.service.INotificacionService;

@Controller
@RequestMapping("/notificacion")
public class NotificacionController {
	
	@Autowired
	private INotificacionService servicioNotificacion;

	@GetMapping
	public String leerpagina() {
		List<NotificacionResponseDto> notificacionesBD = servicioNotificacion.listarNotificaciones();
		System.out.println(notificacionesBD);
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