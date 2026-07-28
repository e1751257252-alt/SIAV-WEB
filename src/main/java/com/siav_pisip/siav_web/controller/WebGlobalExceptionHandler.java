package com.siav_pisip.siav_web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.siav_pisip.siav_web.controller")
public class WebGlobalExceptionHandler {

	@ExceptionHandler(BindException.class)
	public String datosInvalidos(BindException ex, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String referer = request.getHeader("Referer");
		redirectAttributes.addFlashAttribute("errorMensaje",
				"Uno de los valores ingresados no es válido. Verifica los datos e intenta de nuevo.");
		return "redirect:" + (referer != null && !referer.isBlank() ? referer : "/index");
	}

	@ExceptionHandler(WebClientResponseException.class)
	public String errorBackend(WebClientResponseException ex, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String referer = request.getHeader("Referer");
		redirectAttributes.addFlashAttribute("errorMensaje", mensajeLimpio(ex));
		return "redirect:" + (referer != null && !referer.isBlank() ? referer : "/index");
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView errorGenerico(Exception ex) {
		ModelAndView mav = new ModelAndView("error/error");
		mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return mav;
	}

	private String mensajeLimpio(WebClientResponseException ex) {
		String cuerpo = ex.getResponseBodyAsString();
		if (cuerpo == null || cuerpo.isBlank() || cuerpo.trim().startsWith("{") || cuerpo.trim().startsWith("<")) {
			return mensajePorEstado(ex.getStatusCode().value());
		}
		return cuerpo;
	}

	private String mensajePorEstado(int status) {
		switch (status) {
			case 400:
				return "Uno de los valores ingresados no es válido. Verifica los datos e intenta de nuevo.";
			case 401:
				return "Credenciales inválidas.";
			case 403:
				return "No tienes permiso para realizar esta acción.";
			case 404:
				return "El recurso solicitado no existe.";
			case 409:
				return "La operación no se pudo completar por el estado actual del recurso.";
			default:
				return "Ocurrió un error al procesar la solicitud. Intenta nuevamente.";
		}
	}

}
