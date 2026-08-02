package com.siav_pisip.siav_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;
import com.siav_pisip.siav_web.service.IUsuarioService;

import jakarta.servlet.http.HttpSession;

/**
 * Permite a cualquier usuario autenticado cambiar su propia contraseña.
 * También es la pantalla a la que AuthInterceptor redirige obligatoriamente
 * cuando el usuario todavía no cambió la contraseña que se le asignó al
 * crearlo (primer inicio de sesión) o que un administrador le reseteó.
 */
@Controller
@RequestMapping("/perfil")
public class PerfilController {

	@Autowired
	private IUsuarioService servicioUsuario;

	@GetMapping("/cambiarPassword")
	public String leerpagina(@RequestParam(required = false, defaultValue = "false") boolean primerIngreso,
			Model model) {
		model.addAttribute("primerIngreso", primerIngreso);
		return "perfil/cambiarPassword";
	}

	@PostMapping("/cambiarPassword")
	public String cambiarPassword(@RequestParam String passwordActual, @RequestParam String passwordNueva,
			@RequestParam String confirmarPassword,
			@RequestParam(required = false, defaultValue = "false") boolean primerIngreso, Model model,
			HttpSession session) {
		UsuarioResponseDto usuarioLogueado = (UsuarioResponseDto) session.getAttribute("usuarioLogueado");
		model.addAttribute("primerIngreso", primerIngreso);
		if (!passwordNueva.equals(confirmarPassword)) {
			model.addAttribute("error", "La confirmación no coincide con la nueva contraseña.");
			return "perfil/cambiarPassword";
		}
		try {
			UsuarioResponseDto actualizado = servicioUsuario.cambiarPassword(usuarioLogueado.getIdUsuario(),
					passwordActual, passwordNueva);
			session.setAttribute("usuarioLogueado", actualizado);
		} catch (WebClientResponseException ex) {
			model.addAttribute("error", "No se pudo cambiar la contraseña: " + mensaje(ex));
			return "perfil/cambiarPassword";
		}
		return "redirect:/index";
	}

	private String mensaje(WebClientResponseException ex) {
		String cuerpo = ex.getResponseBodyAsString();
		if (cuerpo == null || cuerpo.isBlank()) {
			return "verifica los datos e intenta de nuevo.";
		}
		return cuerpo;
	}

}
