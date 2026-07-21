package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.SolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;
import com.siav_pisip.siav_web.service.ISolicitudService;

@Controller
@RequestMapping("/solicitud")
public class SolicitudController {

	@Autowired
	private ISolicitudService servicioSolicitud;

	@GetMapping
	public String leerpagina(Model model) {
		List<SolicitudResponseDto> solicitudesBD = servicioSolicitud.listarSolicitudes();
		model.addAttribute("listasolicitudes", solicitudesBD);
		return "solicitud/listarSolicitud";
	}

	@GetMapping("/crearSolicitud")
	public String leerpaginacrear(Model model) {
		model.addAttribute("solicitud", new SolicitudRequestDto());
		return "solicitud/crearSolicitud";
	}

	@PostMapping("/guardar")
	public String guardarSolicitud(@ModelAttribute SolicitudRequestDto solicitud) {
		servicioSolicitud.guardarSolicitud(solicitud);
		return "redirect:/solicitud";
	}

	@GetMapping("/editarSolicitud")
	public String leerpaginaeditar() {
		return "solicitud/editarSolicitud";
	}

}