package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.RolRequestDto;
import com.siav_pisip.siav_web.model.dto.response.RolResponseDto;
import com.siav_pisip.siav_web.service.IRolService;

@Controller
@RequestMapping("/rol")
public class RolController {

	@Autowired
	private IRolService servicioRol;

	@GetMapping
	public String leerpagina(Model model) {
		List<RolResponseDto> rolesBD = servicioRol.listarRoles();
		model.addAttribute("listaroles", rolesBD);
		return "rol/listarRol";
	}

	@GetMapping("/crearRol")
	public String leerpaginacrear(Model model) {
		model.addAttribute("rol", new RolRequestDto());
		return "rol/crearRol";
	}

	@PostMapping("/guardar")
	public String guardarRol(@ModelAttribute RolRequestDto rol) {
		servicioRol.guardarRol(rol);
		return "redirect:/rol";
	}

	@GetMapping("/editarRol")
	public String leerpaginaeditar() {
		return "rol/editarRol";
	}

}