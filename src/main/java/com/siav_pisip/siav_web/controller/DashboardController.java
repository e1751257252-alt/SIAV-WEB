package com.siav_pisip.siav_web.controller;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.response.SaldoVacacionesResponseDto;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;
import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;
import com.siav_pisip.siav_web.service.ISaldoVacacionesService;
import com.siav_pisip.siav_web.service.ISolicitudService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/index")
public class DashboardController {

	private static final String ESTADO_PENDIENTE = "Pendiente";
	private static final String ESTADO_APROBADA = "Aprobada";
	private static final String ESTADO_RECHAZADA = "Rechazada";
	private static final String ROL_JEFE = "Jefe";
	private static final String ROL_ADMINISTRADOR = "Administrador";

	@Autowired
	private ISolicitudService servicioSolicitud;

	@Autowired
	private ISaldoVacacionesService servicioSaldoVacaciones;

	@GetMapping
	public String leerpagina(Model model, HttpSession session) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		boolean esAdministrador = ROL_ADMINISTRADOR.equalsIgnoreCase(usuarioLogueado.getNombreRol());

		List<SolicitudResponseDto> solicitudesAlcance = filtrarSolicitudesPorRol(usuarioLogueado, esAdministrador);

		List<SolicitudResponseDto> ultimasSolicitudes = solicitudesAlcance.stream()
				.sorted(Comparator.comparing(SolicitudResponseDto::getFechaSolicitud,
						Comparator.nullsLast(Comparator.reverseOrder())))
				.limit(5)
				.toList();

		model.addAttribute("pendientes", contarPorEstado(solicitudesAlcance, ESTADO_PENDIENTE));
		model.addAttribute("aprobadas", contarPorEstado(solicitudesAlcance, ESTADO_APROBADA));
		model.addAttribute("rechazadas", contarPorEstado(solicitudesAlcance, ESTADO_RECHAZADA));
		model.addAttribute("ultimasSolicitudes", ultimasSolicitudes);
		model.addAttribute("esAdministrador", esAdministrador);

		if (!esAdministrador) {
			model.addAttribute("saldoDisponible", buscarSaldoDisponible(usuarioLogueado));
		}

		return "index";
	}

	private List<SolicitudResponseDto> filtrarSolicitudesPorRol(UsuarioResponseDto usuarioLogueado,
			boolean esAdministrador) {
		List<Long> archivadosPorMi = servicioSolicitud.listarIdsArchivados(usuarioLogueado.getIdUsuario());
		List<SolicitudResponseDto> todas = servicioSolicitud.listarSolicitudes().stream()
				.filter(s -> !Boolean.FALSE.equals(s.getActivo()))
				.filter(s -> !archivadosPorMi.contains(s.getIdSolicitud())).toList();
		if (esAdministrador) {
			return todas;
		}
		if (ROL_JEFE.equalsIgnoreCase(usuarioLogueado.getNombreRol())) {
			return todas.stream().filter(s -> usuarioLogueado.getIdUsuario().equals(s.getIdAprobador())).toList();
		}
		return todas.stream().filter(s -> usuarioLogueado.getIdUsuario().equals(s.getIdUsuario())).toList();
	}

	private long contarPorEstado(List<SolicitudResponseDto> solicitudes, String estado) {
		return solicitudes.stream().filter(s -> estado.equalsIgnoreCase(s.getNombreEstado())).count();
	}

	private BigDecimal buscarSaldoDisponible(UsuarioResponseDto usuarioLogueado) {
		int anioActual = Year.now().getValue();
		return servicioSaldoVacaciones.listarSaldosVacaciones().stream()
				.filter(s -> usuarioLogueado.getIdUsuario().equals(s.getIdUsuario()) && anioActual == s.getAnio())
				.map(SaldoVacacionesResponseDto::getDiasDisponibles)
				.findFirst()
				.orElse(BigDecimal.ZERO);
	}

}
