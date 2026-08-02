package com.siav_pisip.siav_web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;
import com.siav_pisip.siav_web.service.IUsuarioService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

	@Autowired
	private IUsuarioService servicioUsuario;

	@GetMapping("/login")
	public String leerpagina() {
		return "login";
	}

	@PostMapping("/login")
	public String iniciarSesion(@RequestParam String username, @RequestParam String password, Model model,
			HttpServletRequest request) {
		UsuarioResponseDto usuario = servicioUsuario.autenticar(username, password);
		if (usuario == null) {
			model.addAttribute("error", "Usuario o contraseña incorrectos");
			return "login";
		}
		// Sesión nueva de verdad: si el navegador conservaba una sesión anterior
		// (por ejemplo, de otro usuario en el mismo equipo) se invalida antes de
		// crear la nueva, para no arrastrar datos de otra sesión.
		HttpSession sesionAnterior = request.getSession(false);
		if (sesionAnterior != null) {
			sesionAnterior.invalidate();
		}
		HttpSession sesion = request.getSession(true);
		sesion.setAttribute("usuarioLogueado", usuario);
		if (Boolean.TRUE.equals(usuario.getDebeCambiarPassword())) {
			return "redirect:/perfil/cambiarPassword?primerIngreso=true";
		}
		return "redirect:/index";
	}

	// GET intencionalmente: el enlace "Cerrar Sesión" del menú es un <a>, no un
	// formulario. Invalida la sesión en el servidor (antes solo redirigía a
	// /login sin cerrarla), así que ni retrocediendo con el navegador ni
	// escribiendo la URL de una página protegida se puede volver a entrar.
	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}

}
