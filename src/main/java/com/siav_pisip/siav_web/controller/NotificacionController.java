package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.NotificacionRequestDto;
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
	public String leerpaginacrear(Model model) {
		model.addAttribute("notificacion", new NotificacionRequestDto());
		return "notificacion/crearNotificacion";
	}

	@PostMapping("/guardar")
	public String guardarNotificacion(@ModelAttribute NotificacionRequestDto notificacion) {
		servicioNotificacion.guardarNotificacion(notificacion);
		return "redirect:/notificacion";
	}

	@GetMapping("/editarNotificacion")
	public String leerpaginaeditar() {
		return "notificacion/editarNotificacion";
	}

}