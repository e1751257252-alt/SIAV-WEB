package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.response.HistorialEstadosResponseDto;
import com.siav_pisip.siav_web.service.IHistorialEstadosService;

/**
 * El historial de estados ya no se llena a mano: SolicitudUseCaseImpl
 * registra automáticamente una entrada cada vez que una solicitud cambia de
 * estado (creación, aprobación, rechazo, cancelación o reprogramación). Esta
 * pantalla queda solo como consulta/auditoría, por eso no expone crear/editar.
 */
@Controller
@RequestMapping("/historialEstados")
public class HistorialEstadosController {

	@Autowired
	private IHistorialEstadosService servicioHistorialEstados;

	@GetMapping
	public String leerpagina(Model model) {
		List<HistorialEstadosResponseDto> historialBD = servicioHistorialEstados.listarHistorialEstados();
		model.addAttribute("listahistorial", historialBD);
		return "historialEstados/listarHistorialEstados";
	}

	@PostMapping("/desactivar/{idHistorial}")
	public String desactivarHistorialEstados(@PathVariable Long idHistorial) {
		servicioHistorialEstados.desactivarHistorialEstados(idHistorial);
		return "redirect:/historialEstados";
	}

}
