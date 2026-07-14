package com.siav_pisip.siav_web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rol")
public class RolController {

	@GetMapping
	public String leerpagina() {
		return "rol/listarRol";
	}

	@GetMapping("/crearRol")
	public String leerpaginacrear() {
		return "rol/crearRol";
	}

	@GetMapping("/editarRol")
	public String leerpaginaeditar() {
		return "rol/editarRol";
	}

}
