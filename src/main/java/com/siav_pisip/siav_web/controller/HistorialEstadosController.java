package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.HistorialEstadosRequestDto;
import com.siav_pisip.siav_web.model.dto.response.HistorialEstadosResponseDto;
import com.siav_pisip.siav_web.service.IEstadoSolicitudService;
import com.siav_pisip.siav_web.service.IHistorialEstadosService;
import com.siav_pisip.siav_web.service.ISolicitudService;
import com.siav_pisip.siav_web.service.IUsuarioService;

@Controller
@RequestMapping("/historialEstados")
public class HistorialEstadosController {

	@Autowired
	private IHistorialEstadosService servicioHistorialEstados;

	@Autowired
	private ISolicitudService servicioSolicitud;

	@Autowired
	private IEstadoSolicitudService servicioEstadoSolicitud;

	@Autowired
	private IUsuarioService servicioUsuario;

	@GetMapping
	public String leerpagina(Model model) {
		List<HistorialEstadosResponseDto> historialBD = servicioHistorialEstados.listarHistorialEstados();
		model.addAttribute("listahistorial", historialBD);
		return "historialEstados/listarHistorialEstados";
	}

	@GetMapping("/crearHistorialEstados")
	public String leerpaginacrear(Model model) {
		model.addAttribute("historial", new HistorialEstadosRequestDto());
		model.addAttribute("listasolicitudes", servicioSolicitud.listarSolicitudes());
		model.addAttribute("listaestados", servicioEstadoSolicitud.listarEstadosSolicitud());
		model.addAttribute("listausuarios", servicioUsuario.listarUsuarios());
		return "historialEstados/crearHistorialEstados";
	}

	@PostMapping("/guardar")
	public String guardarHistorialEstados(@ModelAttribute HistorialEstadosRequestDto historial) {
		servicioHistorialEstados.guardarHistorialEstados(historial);
		return "redirect:/historialEstados";
	}

	@GetMapping("/editarHistorialEstados/{idHistorial}")
	public String leerpaginaeditar(@PathVariable Long idHistorial, Model model) {
		HistorialEstadosResponseDto existente = servicioHistorialEstados.buscarPorId(idHistorial);
		HistorialEstadosRequestDto historial = new HistorialEstadosRequestDto();
		historial.setIdHistorial(existente.getIdHistorial());
		historial.setIdSolicitud(existente.getIdSolicitud());
		historial.setEstadoAnterior(existente.getEstadoAnterior());
		historial.setEstadoNuevo(existente.getEstadoNuevo());
		historial.setIdUsuarioResponsable(existente.getIdUsuarioResponsable());
		historial.setObservaciones(existente.getObservaciones());
		model.addAttribute("historial", historial);
		model.addAttribute("listasolicitudes", servicioSolicitud.listarSolicitudes());
		model.addAttribute("listaestados", servicioEstadoSolicitud.listarEstadosSolicitud());
		model.addAttribute("listausuarios", servicioUsuario.listarUsuarios());
		return "historialEstados/editarHistorialEstados";
	}

	@PostMapping("/desactivar/{idHistorial}")
	public String desactivarHistorialEstados(@PathVariable Long idHistorial) {
		servicioHistorialEstados.desactivarHistorialEstados(idHistorial);
		return "redirect:/historialEstados";
	}

}