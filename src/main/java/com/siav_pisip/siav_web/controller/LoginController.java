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

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {

	@Autowired
	private IUsuarioService servicioUsuario;

	@GetMapping
	public String leerpagina() {
		return "login";
	}

	@PostMapping
	public String iniciarSesion(@RequestParam String username, @RequestParam String password, Model model,
			HttpSession session) {
		UsuarioResponseDto usuario = servicioUsuario.autenticar(username, password);
		if (usuario == null) {
			model.addAttribute("error", "Usuario o contraseña incorrectos");
			return "login";
		}
		session.setAttribute("usuarioLogueado", usuario);
		return "redirect:/index";
	}

}
