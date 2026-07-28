package com.siav_pisip.siav_web.controller;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.siav_pisip.siav_web.model.dto.request.SolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;
import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;
import com.siav_pisip.siav_web.reporte.ReporteSolicitudExcelGenerator;
import com.siav_pisip.siav_web.reporte.ReporteSolicitudPdfGenerator;
import com.siav_pisip.siav_web.service.IEstadoSolicitudService;
import com.siav_pisip.siav_web.service.ISolicitudService;
import com.siav_pisip.siav_web.service.IUsuarioService;
import com.siav_pisip.siav_web.service.SolicitudNoDisponibleException;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/solicitud")
public class SolicitudController {

	private static final String ESTADO_PENDIENTE = "Pendiente";
	private static final String ESTADO_APROBADA = "Aprobada";
	private static final String ESTADO_RECHAZADA = "Rechazada";
	private static final String ESTADO_CANCELADA = "Cancelada";
	private static final String ROL_ADMINISTRADOR = "Administrador";
	private static final String ROL_JEFE = "Jefe";

	@Autowired
	private ISolicitudService servicioSolicitud;

	@Autowired
	private IEstadoSolicitudService servicioEstadoSolicitud;

	@Autowired
	private IUsuarioService servicioUsuario;

	@GetMapping
	public String leerpagina(@RequestParam(required = false, defaultValue = "false") boolean verEliminadas,
			Model model, HttpSession session) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		boolean esAdministrador = esAdministrador(usuarioLogueado);
		boolean esJefe = esJefe(usuarioLogueado);
		List<SolicitudResponseDto> todas = servicioSolicitud.listarSolicitudes();
		asignarNumeroPorColaborador(todas);

		List<SolicitudResponseDto> visibles = filtrarPorAlcance(todas, usuarioLogueado, esAdministrador, esJefe);
		List<Long> archivadosPorMi = servicioSolicitud.listarIdsArchivados(usuarioLogueado.getIdUsuario());
		List<SolicitudResponseDto> solicitudesBD = visibles.stream()
				.filter(s -> verEliminadas || (!Boolean.FALSE.equals(s.getActivo())
						&& !archivadosPorMi.contains(s.getIdSolicitud())))
				.toList();

		model.addAttribute("listasolicitudes", solicitudesBD);
		model.addAttribute("esAdministrador", esAdministrador);
		model.addAttribute("mostrarFiltroColaborador", esAdministrador || esJefe);
		model.addAttribute("listausuarios", opcionesFiltroColaborador(usuarioLogueado));
		model.addAttribute("verEliminadas", verEliminadas);
		model.addAttribute("idsArchivadosPorMi", archivadosPorMi);
		return "solicitud/listarSolicitud";
	}

	private List<SolicitudResponseDto> filtrarPorAlcance(List<SolicitudResponseDto> todas,
			UsuarioResponseDto usuarioLogueado, boolean esAdministrador, boolean esJefe) {
		if (esAdministrador) {
			return todas;
		}
		if (esJefe) {
			List<Long> equipoIds = opcionesFiltroColaborador(usuarioLogueado).stream()
					.map(UsuarioResponseDto::getIdUsuario).toList();
			return todas.stream()
					.filter(s -> usuarioLogueado.getIdUsuario().equals(s.getIdUsuario())
							|| (equipoIds.contains(s.getIdUsuario())
									&& (ESTADO_APROBADA.equalsIgnoreCase(s.getNombreEstado())
											|| ESTADO_RECHAZADA.equalsIgnoreCase(s.getNombreEstado())
											|| ESTADO_CANCELADA.equalsIgnoreCase(s.getNombreEstado()))))
					.toList();
		}
		return todas.stream().filter(s -> usuarioLogueado.getIdUsuario().equals(s.getIdUsuario())).toList();
	}

	private void asignarNumeroPorColaborador(List<SolicitudResponseDto> todas) {
		Map<Long, List<SolicitudResponseDto>> porUsuario = todas.stream()
				.collect(Collectors.groupingBy(SolicitudResponseDto::getIdUsuario));
		porUsuario.values().forEach(lista -> {
			lista.sort(Comparator.comparing(SolicitudResponseDto::getFechaSolicitud,
					Comparator.nullsLast(Comparator.naturalOrder())));
			int contador = 1;
			for (SolicitudResponseDto s : lista) {
				s.setNumeroSolicitud(contador++);
			}
		});
	}

	@GetMapping("/exportar/excel")
	public ResponseEntity<byte[]> exportarExcel(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
			@RequestParam(required = false) Long idUsuario, HttpSession session) {
		Long idUsuarioFiltro = idUsuarioEfectivo(idUsuario, session);
		List<SolicitudResponseDto> datos = filtrarParaReporte(fechaDesde, fechaHasta, idUsuario, session);
		byte[] archivo = ReporteSolicitudExcelGenerator.generar(datos, fechaDesde, fechaHasta,
				nombreColaborador(idUsuarioFiltro));
		return descargar(archivo, "reporte_solicitudes.xlsx",
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
	}

	@GetMapping("/exportar/pdf")
	public ResponseEntity<byte[]> exportarPdf(
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
			@RequestParam(required = false) Long idUsuario, HttpSession session) {
		Long idUsuarioFiltro = idUsuarioEfectivo(idUsuario, session);
		List<SolicitudResponseDto> datos = filtrarParaReporte(fechaDesde, fechaHasta, idUsuario, session);
		byte[] archivo = ReporteSolicitudPdfGenerator.generar(datos, fechaDesde, fechaHasta,
				nombreColaborador(idUsuarioFiltro));
		return descargar(archivo, "reporte_solicitudes.pdf", MediaType.APPLICATION_PDF);
	}

	private boolean esAdministrador(UsuarioResponseDto usuarioLogueado) {
		return ROL_ADMINISTRADOR.equalsIgnoreCase(usuarioLogueado.getNombreRol());
	}

	private boolean esJefe(UsuarioResponseDto usuarioLogueado) {
		return ROL_JEFE.equalsIgnoreCase(usuarioLogueado.getNombreRol());
	}

	private List<UsuarioResponseDto> opcionesFiltroColaborador(UsuarioResponseDto usuarioLogueado) {
		if (esAdministrador(usuarioLogueado)) {
			return servicioUsuario.listarUsuarios();
		}
		if (esJefe(usuarioLogueado)) {
			return servicioUsuario.listarUsuarios().stream()
					.filter(u -> usuarioLogueado.getIdUsuario().equals(u.getIdJefe())).toList();
		}
		return List.of();
	}

	private Long idUsuarioEfectivo(Long idUsuarioSolicitado, HttpSession session) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		if (esAdministrador(usuarioLogueado)) {
			return idUsuarioSolicitado;
		}
		if (esJefe(usuarioLogueado)) {
			boolean esDeMiEquipo = idUsuarioSolicitado != null && opcionesFiltroColaborador(usuarioLogueado).stream()
					.anyMatch(u -> idUsuarioSolicitado.equals(u.getIdUsuario()));
			return esDeMiEquipo ? idUsuarioSolicitado : null;
		}
		return usuarioLogueado.getIdUsuario();
	}

	private List<SolicitudResponseDto> filtrarParaReporte(LocalDate fechaDesde, LocalDate fechaHasta,
			Long idUsuarioSolicitado, HttpSession session) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		Long idUsuarioFiltro = idUsuarioEfectivo(idUsuarioSolicitado, session);
		List<Long> idsPermitidos = esJefe(usuarioLogueado)
				? opcionesFiltroColaborador(usuarioLogueado).stream().map(UsuarioResponseDto::getIdUsuario).toList()
				: null;

		return servicioSolicitud.listarSolicitudes().stream()
				.filter(s -> ESTADO_APROBADA.equalsIgnoreCase(s.getNombreEstado())
						|| ESTADO_RECHAZADA.equalsIgnoreCase(s.getNombreEstado()))
				.filter(s -> fechaDesde == null || !s.getFechaInicio().isBefore(fechaDesde))
				.filter(s -> fechaHasta == null || !s.getFechaFin().isAfter(fechaHasta))
				.filter(s -> idUsuarioFiltro == null || idUsuarioFiltro.equals(s.getIdUsuario()))
				.filter(s -> idsPermitidos == null || idsPermitidos.contains(s.getIdUsuario()))
				.toList();
	}

	private String nombreColaborador(Long idUsuario) {
		if (idUsuario == null) {
			return null;
		}
		return servicioUsuario.listarUsuarios().stream().filter(u -> idUsuario.equals(u.getIdUsuario())).findFirst()
				.map(u -> u.getNombres() + " " + u.getApellidos()).orElse(null);
	}

	private ResponseEntity<byte[]> descargar(byte[] contenido, String nombreArchivo, MediaType tipo) {
		return ResponseEntity.ok().contentType(tipo)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nombreArchivo + "\"")
				.body(contenido);
	}

	@GetMapping("/crearSolicitud")
	public String leerpaginacrear(Model model, HttpSession session) {
		model.addAttribute("solicitud", new SolicitudRequestDto());
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		model.addAttribute("nombreJefe", usuarioLogueado.getNombreJefe());
		return "solicitud/crearSolicitud";
	}

	@PostMapping("/guardar")
	public String guardarSolicitud(@ModelAttribute SolicitudRequestDto solicitud, HttpSession session,
			RedirectAttributes redirectAttributes) {
		boolean esNueva = solicitud.getIdSolicitud() == null;
		if (esNueva) {
			UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
			solicitud.setIdUsuario(usuarioLogueado.getIdUsuario());
			solicitud.setIdAprobador(usuarioLogueado.getIdJefe());
			Long idPendiente = servicioEstadoSolicitud.listarEstadosSolicitud().stream()
					.filter(e -> ESTADO_PENDIENTE.equalsIgnoreCase(e.getNombre()))
					.map(EstadoSolicitudResponseDto::getIdEstado).findFirst().orElse(null);
			solicitud.setIdEstado(idPendiente);
		}
		try {
			servicioSolicitud.guardarSolicitud(solicitud);
		} catch (SolicitudNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
			return esNueva ? "redirect:/solicitud/crearSolicitud" : "redirect:/solicitud/editarSolicitud/" + solicitud.getIdSolicitud();
		}
		return "redirect:/solicitud";
	}

	@GetMapping("/editarSolicitud/{idSolicitud}")
	public String leerpaginaeditar(@PathVariable Long idSolicitud, Model model,
			RedirectAttributes redirectAttributes) {
		SolicitudResponseDto existente = servicioSolicitud.buscarPorId(idSolicitud);
		if (!ESTADO_PENDIENTE.equalsIgnoreCase(existente.getNombreEstado())) {
			redirectAttributes.addFlashAttribute("errorMensaje",
					"No se puede editar: la solicitud ya fue resuelta (" + existente.getNombreEstado() + ").");
			return "redirect:/solicitud";
		}
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

	@PostMapping("/archivar/{idSolicitud}")
	public String archivarSolicitud(@PathVariable Long idSolicitud, HttpSession session) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		servicioSolicitud.archivarSolicitud(idSolicitud, usuarioLogueado.getIdUsuario());
		return "redirect:/solicitud";
	}

	@GetMapping("/miEquipo")
	public String leerpaginaMiEquipo(Model model, HttpSession session) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		boolean esAdministrador = esAdministrador(usuarioLogueado);
		List<Long> archivadosPorMi = servicioSolicitud.listarIdsArchivados(usuarioLogueado.getIdUsuario());
		List<SolicitudResponseDto> solicitudesDeMiEquipo = servicioSolicitud.listarSolicitudes().stream()
				.filter(s -> !Boolean.FALSE.equals(s.getActivo()))
				.filter(s -> !archivadosPorMi.contains(s.getIdSolicitud()))
				.filter(s -> esAdministrador || usuarioLogueado.getIdUsuario().equals(s.getIdAprobador())).toList();
		List<SolicitudResponseDto> pendientesDeMiEquipo = solicitudesDeMiEquipo.stream()
				.filter(s -> ESTADO_PENDIENTE.equalsIgnoreCase(s.getNombreEstado())).toList();
		List<SolicitudResponseDto> aprobadasDeMiEquipo = solicitudesDeMiEquipo.stream()
				.filter(s -> ESTADO_APROBADA.equalsIgnoreCase(s.getNombreEstado())).toList();
		model.addAttribute("listapendientes", pendientesDeMiEquipo);
		model.addAttribute("listaaprobadas", aprobadasDeMiEquipo);
		return "solicitud/miEquipo";
	}

	@PostMapping("/aprobar/{idSolicitud}")
	public String aprobarSolicitud(@PathVariable Long idSolicitud, @RequestParam(required = false) String observaciones,
			HttpSession session, RedirectAttributes redirectAttributes) {
		return resolverSolicitud(idSolicitud, observaciones, session, redirectAttributes, true);
	}

	@PostMapping("/rechazar/{idSolicitud}")
	public String rechazarSolicitud(@PathVariable Long idSolicitud,
			@RequestParam(required = false) String observaciones, HttpSession session,
			RedirectAttributes redirectAttributes) {
		return resolverSolicitud(idSolicitud, observaciones, session, redirectAttributes, false);
	}

	private String resolverSolicitud(Long idSolicitud, String observaciones, HttpSession session,
			RedirectAttributes redirectAttributes, boolean aprobar) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		try {
			if (aprobar) {
				servicioSolicitud.aprobarSolicitud(idSolicitud, usuarioLogueado.getIdUsuario(), observaciones);
			} else {
				servicioSolicitud.rechazarSolicitud(idSolicitud, usuarioLogueado.getIdUsuario(), observaciones);
			}
		} catch (SolicitudNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
		}
		return "redirect:/solicitud/miEquipo";
	}

	@GetMapping("/reprogramar/{idSolicitud}")
	public String leerpaginaReprogramar(@PathVariable Long idSolicitud,
			@RequestParam(required = false, defaultValue = "lista") String origen, Model model,
			HttpSession session, RedirectAttributes redirectAttributes) {
		SolicitudResponseDto existente = servicioSolicitud.buscarPorId(idSolicitud);
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		if (!ESTADO_APROBADA.equalsIgnoreCase(existente.getNombreEstado())) {
			redirectAttributes.addFlashAttribute("errorMensaje",
					"Solo se pueden reprogramar solicitudes Aprobadas.");
			return "redirect:" + urlOrigen(origen);
		}
		if (!usuarioLogueado.getIdUsuario().equals(existente.getIdUsuario())) {
			return "redirect:/index?accesoDenegado=true";
		}
		model.addAttribute("solicitud", existente);
		model.addAttribute("origen", origen);
		return "solicitud/reprogramarSolicitud";
	}

	@PostMapping("/reprogramar/{idSolicitud}")
	public String reprogramarSolicitud(@PathVariable Long idSolicitud,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
			@RequestParam Integer diasSolicitados, @RequestParam(required = false) String motivo,
			@RequestParam(required = false, defaultValue = "lista") String origen, HttpSession session,
			RedirectAttributes redirectAttributes) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		try {
			servicioSolicitud.reprogramarSolicitud(idSolicitud, usuarioLogueado.getIdUsuario(), fechaInicio,
					fechaFin, diasSolicitados, motivo);
		} catch (SolicitudNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
			return "redirect:/solicitud/reprogramar/" + idSolicitud + "?origen=" + origen;
		}
		return "redirect:" + urlOrigen(origen);
	}

	@PostMapping("/cancelarAprobacion/{idSolicitud}")
	public String cancelarAprobacionSolicitud(@PathVariable Long idSolicitud,
			@RequestParam(required = false) String observaciones,
			@RequestParam(required = false, defaultValue = "lista") String origen, HttpSession session,
			RedirectAttributes redirectAttributes) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		try {
			servicioSolicitud.cancelarAprobacionSolicitud(idSolicitud, usuarioLogueado.getIdUsuario(), observaciones);
		} catch (SolicitudNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
		}
		return "redirect:" + urlOrigen(origen);
	}

	private String urlOrigen(String origen) {
		return "equipo".equals(origen) ? "/solicitud/miEquipo" : "/solicitud";
	}

}