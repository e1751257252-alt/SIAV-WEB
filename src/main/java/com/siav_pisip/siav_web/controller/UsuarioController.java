package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.UsuarioRequestDto;
import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;
import com.siav_pisip.siav_web.service.IUsuarioService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private IUsuarioService servicioUsuario;

	@GetMapping
	public String leerpagina(Model model) {
		List<UsuarioResponseDto> usuariosBD = servicioUsuario.listarUsuarios();
		model.addAttribute("listausuarios", usuariosBD);
		return "usuario/listarUsuario";
	}

	@GetMapping("/crearUsuario")
	public String leerpaginacrear(Model model) {
		model.addAttribute("usuario", new UsuarioRequestDto());
		return "usuario/crearUsuario";
	}

	@PostMapping("/guardar")
	public String guardarUsuario(@ModelAttribute UsuarioRequestDto usuario) {
		servicioUsuario.guardarUsuario(usuario);
		return "redirect:/usuario";
	}

	@GetMapping("/editarUsuario")
	public String leerpaginaeditar() {
		return "usuario/editarUsuario";
	}

}