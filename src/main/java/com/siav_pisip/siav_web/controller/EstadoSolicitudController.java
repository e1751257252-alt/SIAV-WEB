package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.EstadoSolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;
import com.siav_pisip.siav_web.service.IEstadoSolicitudService;

@Controller
@RequestMapping("/estadoSolicitud")
public class EstadoSolicitudController {

	@Autowired
	private IEstadoSolicitudService servicioEstadoSolicitud;

	@GetMapping
	public String leerpagina(Model model) {
		List<EstadoSolicitudResponseDto> estadosBD = servicioEstadoSolicitud.listarEstadosSolicitud();
		model.addAttribute("listaestados", estadosBD);
		return "estadoSolicitud/listarEstadoSolicitud";
	}

	@GetMapping("/crearEstadoSolicitud")
	public String leerpaginacrear(Model model) {
		model.addAttribute("estado", new EstadoSolicitudRequestDto());
		return "estadoSolicitud/crearEstadoSolicitud";
	}

	@PostMapping("/guardar")
	public String guardarEstadoSolicitud(@ModelAttribute EstadoSolicitudRequestDto estado) {
		servicioEstadoSolicitud.guardarEstadoSolicitud(estado);
		return "redirect:/estadoSolicitud";
	}

	@GetMapping("/editarEstadoSolicitud")
	public String leerpaginaeditar() {
		return "estadoSolicitud/editarEstadoSolicitud";
	}

}
