package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;
import com.siav_pisip.siav_web.service.IEstadoSolicitudService;

@Controller
@RequestMapping("/estadoSolicitud")
public class EstadoSolicitudController {

	@Autowired
	private IEstadoSolicitudService servicioEstadoSolicitud;

	@GetMapping
	public String leerpagina() {
		List<EstadoSolicitudResponseDto> estadosBD = servicioEstadoSolicitud.listarEstadosSolicitud();
		System.out.println(estadosBD);
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
