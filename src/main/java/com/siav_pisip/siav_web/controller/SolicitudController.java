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

import com.siav_pisip.siav_web.model.dto.request.SolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;
import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;
import com.siav_pisip.siav_web.service.IEstadoSolicitudService;
import com.siav_pisip.siav_web.service.ISolicitudService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/solicitud")
public class SolicitudController {

	private static final String ESTADO_PENDIENTE = "Pendiente";
	private static final String ESTADO_APROBADA = "Aprobada";
	private static final String ESTADO_RECHAZADA = "Rechazada";

	@Autowired
	private ISolicitudService servicioSolicitud;

	@Autowired
	private IEstadoSolicitudService servicioEstadoSolicitud;

	@GetMapping
	public String leerpagina(Model model) {
		List<SolicitudResponseDto> solicitudesBD = servicioSolicitud.listarSolicitudes();
		model.addAttribute("listasolicitudes", solicitudesBD);
		return "solicitud/listarSolicitud";
	}

	@GetMapping("/crearSolicitud")
	public String leerpaginacrear(Model model, HttpSession session) {
		model.addAttribute("solicitud", new SolicitudRequestDto());
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		model.addAttribute("nombreJefe", usuarioLogueado.getNombreJefe());
		return "solicitud/crearSolicitud";
	}

	@PostMapping("/guardar")
	public String guardarSolicitud(@ModelAttribute SolicitudRequestDto solicitud, HttpSession session) {
		if (solicitud.getIdSolicitud() == null) {
			// Solicitud nueva: quien la crea es siempre el usuario logueado, el
			// aprobador es su jefe (no lo elige el propio usuario) y arranca en
			// Pendiente.
			UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
			solicitud.setIdUsuario(usuarioLogueado.getIdUsuario());
			solicitud.setIdAprobador(usuarioLogueado.getIdJefe());
			Long idPendiente = servicioEstadoSolicitud.listarEstadosSolicitud().stream()
					.filter(e -> ESTADO_PENDIENTE.equalsIgnoreCase(e.getNombre()))
					.map(EstadoSolicitudResponseDto::getIdEstado).findFirst().orElse(null);
			solicitud.setIdEstado(idPendiente);
		}
		servicioSolicitud.guardarSolicitud(solicitud);
		return "redirect:/solicitud";
	}

	@GetMapping("/editarSolicitud/{idSolicitud}")
	public String leerpaginaeditar(@PathVariable Long idSolicitud, Model model) {
		SolicitudResponseDto existente = servicioSolicitud.buscarPorId(idSolicitud);
		SolicitudRequestDto solicitud = new SolicitudRequestDto();
		solicitud.setIdSolicitud(existente.getIdSolicitud());
		solicitud.setIdUsuario(existente.getIdUsuario());
		solicitud.setFechaInicio(existente.getFechaInicio());
		solicitud.setFechaFin(existente.getFechaFin());
		solicitud.setDiasSolicitados(existente.getDiasSolicitados());
		solicitud.setMotivo(existente.getMotivo());
		solicitud.setIdEstado(existente.getIdEstado());
		solicitud.setIdAprobador(existente.getIdAprobador());
		solicitud.setObservacionesAprobador(existente.getObservacionesAprobador());
		model.addAttribute("solicitud", solicitud);
		model.addAttribute("nombreJefe", existente.getNombresAprobador());
		return "solicitud/editarSolicitud";
	}

	@PostMapping("/desactivar/{idSolicitud}")
	public String desactivarSolicitud(@PathVariable Long idSolicitud) {
		servicioSolicitud.desactivarSolicitud(idSolicitud);
		return "redirect:/solicitud";
	}

	@GetMapping("/miEquipo")
	public String leerpaginaMiEquipo(Model model, HttpSession session) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		List<SolicitudResponseDto> pendientesDeMiEquipo = servicioSolicitud.listarSolicitudes().stream()
				.filter(s -> usuarioLogueado.getIdUsuario().equals(s.getIdAprobador()))
				.filter(s -> ESTADO_PENDIENTE.equalsIgnoreCase(s.getNombreEstado())).toList();
		model.addAttribute("listapendientes", pendientesDeMiEquipo);
		return "solicitud/miEquipo";
	}

	@PostMapping("/aprobar/{idSolicitud}")
	public String aprobarSolicitud(@PathVariable Long idSolicitud) {
		resolverSolicitud(idSolicitud, ESTADO_APROBADA);
		return "redirect:/solicitud/miEquipo";
	}

	@PostMapping("/rechazar/{idSolicitud}")
	public String rechazarSolicitud(@PathVariable Long idSolicitud) {
		resolverSolicitud(idSolicitud, ESTADO_RECHAZADA);
		return "redirect:/solicitud/miEquipo";
	}

	private void resolverSolicitud(Long idSolicitud, String nombreEstadoNuevo) {
		SolicitudResponseDto existente = servicioSolicitud.buscarPorId(idSolicitud);
		Long idEstadoNuevo = servicioEstadoSolicitud.listarEstadosSolicitud().stream()
				.filter(e -> nombreEstadoNuevo.equalsIgnoreCase(e.getNombre())).map(EstadoSolicitudResponseDto::getIdEstado)
				.findFirst().orElse(null);

		SolicitudRequestDto solicitud = new SolicitudRequestDto();
		solicitud.setIdSolicitud(existente.getIdSolicitud());
		solicitud.setIdUsuario(existente.getIdUsuario());
		solicitud.setFechaInicio(existente.getFechaInicio());
		solicitud.setFechaFin(existente.getFechaFin());
		solicitud.setDiasSolicitados(existente.getDiasSolicitados());
		solicitud.setMotivo(existente.getMotivo());
		solicitud.setIdEstado(idEstadoNuevo);
		solicitud.setIdAprobador(existente.getIdAprobador());
		solicitud.setObservacionesAprobador(existente.getObservacionesAprobador());
		servicioSolicitud.guardarSolicitud(solicitud);
	}

}