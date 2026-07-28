package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.siav_pisip.siav_web.model.dto.request.RolRequestDto;
import com.siav_pisip.siav_web.model.dto.response.RolResponseDto;
import com.siav_pisip.siav_web.service.IRolService;
import com.siav_pisip.siav_web.service.OperacionNoDisponibleException;

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
	public String guardarRol(@ModelAttribute RolRequestDto rol, RedirectAttributes redirectAttributes) {
		boolean esNuevo = rol.getIdRol() == null;
		try {
			servicioRol.guardarRol(rol);
		} catch (OperacionNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
			return esNuevo ? "redirect:/rol/crearRol" : "redirect:/rol/editarRol/" + rol.getIdRol();
		}
		return "redirect:/rol";
	}

	@GetMapping("/editarRol/{idRol}")
	public String leerpaginaeditar(@PathVariable Long idRol, Model model) {
		RolResponseDto existente = servicioRol.buscarPorId(idRol);
		RolRequestDto rol = new RolRequestDto();
		rol.setIdRol(existente.getIdRol());
		rol.setNombreRol(existente.getNombreRol());
		rol.setDescripcion(existente.getDescripcion());
		model.addAttribute("rol", rol);
		return "rol/editarRol";
	}

	@PostMapping("/desactivar/{idRol}")
	public String desactivarRol(@PathVariable Long idRol, RedirectAttributes redirectAttributes) {
		try {
			servicioRol.desactivarRol(idRol);
		} catch (OperacionNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
		}
		return "redirect:/rol";
	}

}