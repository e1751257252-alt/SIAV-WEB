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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.siav_pisip.siav_web.model.dto.request.EstadoSolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;
import com.siav_pisip.siav_web.service.IEstadoSolicitudService;
import com.siav_pisip.siav_web.service.OperacionNoDisponibleException;

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
	public String guardarEstadoSolicitud(@ModelAttribute EstadoSolicitudRequestDto estado,
			RedirectAttributes redirectAttributes) {
		boolean esNuevo = estado.getIdEstado() == null;
		try {
			servicioEstadoSolicitud.guardarEstadoSolicitud(estado);
		} catch (OperacionNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
			return esNuevo ? "redirect:/estadoSolicitud/crearEstadoSolicitud"
					: "redirect:/estadoSolicitud/editarEstadoSolicitud/" + estado.getIdEstado();
		}
		return "redirect:/estadoSolicitud";
	}

	@GetMapping("/editarEstadoSolicitud/{idEstado}")
	public String leerpaginaeditar(@PathVariable Long idEstado, Model model) {
		EstadoSolicitudResponseDto existente = servicioEstadoSolicitud.buscarPorId(idEstado);
		EstadoSolicitudRequestDto estado = new EstadoSolicitudRequestDto();
		estado.setIdEstado(existente.getIdEstado());
		estado.setNombre(existente.getNombre());
		model.addAttribute("estado", estado);
		return "estadoSolicitud/editarEstadoSolicitud";
	}

	@PostMapping("/desactivar/{idEstado}")
	public String desactivarEstadoSolicitud(@PathVariable Long idEstado, RedirectAttributes redirectAttributes) {
		try {
			servicioEstadoSolicitud.desactivarEstadoSolicitud(idEstado);
		} catch (OperacionNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
		}
		return "redirect:/estadoSolicitud";
	}

}
