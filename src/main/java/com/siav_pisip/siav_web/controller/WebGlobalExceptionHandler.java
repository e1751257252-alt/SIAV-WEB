package com.siav_pisip.siav_web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
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
		String mensaje = ex.getResponseBodyAsString();
		redirectAttributes.addFlashAttribute("errorMensaje",
				mensaje != null && !mensaje.isBlank() ? mensaje : "El servidor rechazó la operación.");
		return "redirect:" + (referer != null && !referer.isBlank() ? referer : "/index");
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView errorGenerico(Exception ex) {
		ModelAndView mav = new ModelAndView("error/error");
		mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
		mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		return mav;
	}

}
