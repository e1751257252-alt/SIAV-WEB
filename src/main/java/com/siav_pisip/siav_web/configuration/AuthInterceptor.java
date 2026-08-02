package com.siav_pisip.siav_web.configuration;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {

	private static final Map<String, List<String>> RUTAS_RESTRINGIDAS = Map.ofEntries(
			Map.entry("/solicitud/miEquipo", List.of("JEFE", "ADMINISTRADOR")),
			Map.entry("/rol", List.of("ADMINISTRADOR")),
			Map.entry("/usuario", List.of("ADMINISTRADOR")),
			Map.entry("/cargo", List.of("ADMINISTRADOR")),
			Map.entry("/estadoSolicitud", List.of("ADMINISTRADOR")),
			Map.entry("/saldoVacaciones", List.of("ADMINISTRADOR")),
			Map.entry("/movimientoSaldo", List.of("ADMINISTRADOR")),
			Map.entry("/historialEstados", List.of("ADMINISTRADOR")),
			Map.entry("/notificacion/crearNotificacion", List.of("ADMINISTRADOR")),
			Map.entry("/notificacion/editarNotificacion", List.of("ADMINISTRADOR")),
			Map.entry("/notificacion/guardar", List.of("ADMINISTRADOR")),
			Map.entry("/notificacion/desactivar", List.of("ADMINISTRADOR")));

	// Rutas accesibles aunque el usuario todavía tenga pendiente el cambio de
	// contraseña obligatorio (la propia pantalla de cambio, y el logout).
	private static final List<String> RUTAS_PERMITIDAS_CON_CAMBIO_PENDIENTE = List.of("/perfil/cambiarPassword",
			"/logout");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// Toda página que pasa por este interceptor está detrás de sesión: se
		// marca como no cacheable para que el botón "atrás" del navegador no
		// la muestre desde caché después de cerrar sesión (siempre revalida
		// contra el servidor, que redirige a /login si ya no hay sesión).
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		HttpSession session = request.getSession(false);
		UsuarioResponseDto usuarioLogueado = session == null ? null
				: (UsuarioResponseDto) session.getAttribute("usuarioLogueado");

		if (usuarioLogueado == null) {
			response.sendRedirect("/login");
			return false;
		}

		String uri = request.getRequestURI();

		if (Boolean.TRUE.equals(usuarioLogueado.getDebeCambiarPassword())
				&& RUTAS_PERMITIDAS_CON_CAMBIO_PENDIENTE.stream().noneMatch(uri::startsWith)) {
			response.sendRedirect("/perfil/cambiarPassword?primerIngreso=true");
			return false;
		}

		String rutaRestringida = rutaRestringidaMasEspecifica(uri);
		if (rutaRestringida != null && !rolPermitido(rutaRestringida, usuarioLogueado.getNombreRol())) {
			response.sendRedirect("/index?accesoDenegado=true");
			return false;
		}

		return true;
	}

	private String rutaRestringidaMasEspecifica(String uri) {
		return RUTAS_RESTRINGIDAS.keySet().stream().filter(uri::startsWith)
				.max((a, b) -> Integer.compare(a.length(), b.length())).orElse(null);
	}

	private boolean rolPermitido(String ruta, String nombreRol) {
		if (nombreRol == null) {
			return false;
		}
		return RUTAS_RESTRINGIDAS.get(ruta).stream().anyMatch(nombreRol::equalsIgnoreCase);
	}

}
