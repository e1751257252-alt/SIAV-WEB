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

import com.siav_pisip.siav_web.model.dto.request.NotificacionRequestDto;
import com.siav_pisip.siav_web.model.dto.response.NotificacionResponseDto;
import com.siav_pisip.siav_web.service.INotificacionService;
import com.siav_pisip.siav_web.service.ISolicitudService;
import com.siav_pisip.siav_web.service.IUsuarioService;

@Controller
@RequestMapping("/notificacion")
public class NotificacionController {

	@Autowired
	private INotificacionService servicioNotificacion;

	@Autowired
	private IUsuarioService servicioUsuario;

	@Autowired
	private ISolicitudService servicioSolicitud;

	@GetMapping
	public String leerpagina(Model model) {
		List<NotificacionResponseDto> notificacionesBD = servicioNotificacion.listarNotificaciones();
		model.addAttribute("listanotificaciones", notificacionesBD);
		return "notificacion/listarNotificacion";
	}

	@GetMapping("/crearNotificacion")
	public String leerpaginacrear(Model model) {
		model.addAttribute("notificacion", new NotificacionRequestDto());
		model.addAttribute("listausuarios", servicioUsuario.listarUsuarios());
		model.addAttribute("listasolicitudes", servicioSolicitud.listarSolicitudes());
		return "notificacion/crearNotificacion";
	}

	@PostMapping("/guardar")
	public String guardarNotificacion(@ModelAttribute NotificacionRequestDto notificacion) {
		servicioNotificacion.guardarNotificacion(notificacion);
		return "redirect:/notificacion";
	}

	@GetMapping("/editarNotificacion/{idNotificacion}")
	public String leerpaginaeditar(@PathVariable Long idNotificacion, Model model) {
		NotificacionResponseDto existente = servicioNotificacion.buscarPorId(idNotificacion);
		NotificacionRequestDto notificacion = new NotificacionRequestDto();
		notificacion.setIdNotificacion(existente.getIdNotificacion());
		notificacion.setIdUsuarioDestino(existente.getIdUsuarioDestino());
		notificacion.setIdSolicitud(existente.getIdSolicitud());
		notificacion.setTipoNotificacion(existente.getTipoNotificacion());
		notificacion.setMensaje(existente.getMensaje());
		notificacion.setLeido(existente.getLeido());
		model.addAttribute("notificacion", notificacion);
		model.addAttribute("listausuarios", servicioUsuario.listarUsuarios());
		model.addAttribute("listasolicitudes", servicioSolicitud.listarSolicitudes());
		return "notificacion/editarNotificacion";
	}

	@PostMapping("/desactivar/{idNotificacion}")
	public String desactivarNotificacion(@PathVariable Long idNotificacion) {
		servicioNotificacion.desactivarNotificacion(idNotificacion);
		return "redirect:/notificacion";
	}

}