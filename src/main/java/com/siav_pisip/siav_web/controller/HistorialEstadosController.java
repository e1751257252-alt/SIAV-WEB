package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.HistorialEstadosRequestDto;
import com.siav_pisip.siav_web.model.dto.response.HistorialEstadosResponseDto;
import com.siav_pisip.siav_web.service.IHistorialEstadosService;

@Controller
@RequestMapping("/historialEstados")
public class HistorialEstadosController {

	@Autowired
	private IHistorialEstadosService servicioHistorialEstados;

	@GetMapping
	public String leerpagina() {
		List<HistorialEstadosResponseDto> historialBD = servicioHistorialEstados.listarHistorialEstados();
		System.out.println(historialBD);
		return "historialEstados/listarHistorialEstados";
	}

	@GetMapping("/crearHistorialEstados")
	public String leerpaginacrear(Model model) {
		model.addAttribute("historial", new HistorialEstadosRequestDto());
		return "historialEstados/crearHistorialEstados";
	}

	@PostMapping("/guardar")
	public String guardarHistorialEstados(@ModelAttribute HistorialEstadosRequestDto historial) {
		servicioHistorialEstados.guardarHistorialEstados(historial);
		return "redirect:/historialEstados";
	}

	@GetMapping("/editarHistorialEstados")
	public String leerpaginaeditar() {
		return "historialEstados/editarHistorialEstados";
	}

}