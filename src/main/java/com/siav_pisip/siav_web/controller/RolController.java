package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.siav_pisip.siav_web.model.dto.response.RolResponseDto;
import com.siav_pisip.siav_web.service.IRolService;

@Controller
@RequestMapping("/rol")
public class RolController {

	@Autowired
	private IRolService servicioRol;

	@GetMapping
	public String leerpagina() {
		List<RolResponseDto> rolesBD = servicioRol.listarRoles();
		System.out.println(rolesBD);
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