package com.siav_pisip.siav_web.configuration;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Bloquea el acceso a cualquier página que no sea /login (o los assets
 * estáticos) si no hay una sesión con usuario autenticado. Antes de este
 * fix, cualquiera podía navegar directo a /index o a cualquier módulo sin
 * pasar por el login.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession(false);
		boolean autenticado = session != null && session.getAttribute("usuarioLogueado") != null;
		if (autenticado) {
			return true;
		}
		response.sendRedirect("/login");
		return false;
	}

}
