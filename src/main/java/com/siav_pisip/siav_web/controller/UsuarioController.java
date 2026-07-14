package com.siav_pisip.siav_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@GetMapping
	public String leerpagina() {
		return "usuario/listarUsuario";
	}

	@GetMapping("/crearUsuario")
	public String leerpaginacrear() {
		return "usuario/crearUsuario";
	}

	@GetMapping("/editarUsuario")
	public String leerpaginaeditar() {
		return "usuario/editarUsuario";
	}

}
