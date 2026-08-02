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

import com.siav_pisip.siav_web.model.dto.request.CargoRequestDto;
import com.siav_pisip.siav_web.model.dto.response.CargoResponseDto;
import com.siav_pisip.siav_web.service.ICargoService;
import com.siav_pisip.siav_web.service.OperacionNoDisponibleException;

@Controller
@RequestMapping("/cargo")
public class CargoController {

	@Autowired
	private ICargoService servicioCargo;

	@GetMapping
	public String leerpagina(Model model) {
		List<CargoResponseDto> cargosBD = servicioCargo.listarCargos();
		model.addAttribute("listacargos", cargosBD);
		return "cargo/listarCargo";
	}

	@GetMapping("/crearCargo")
	public String leerpaginacrear(Model model) {
		model.addAttribute("cargo", new CargoRequestDto());
		return "cargo/crearCargo";
	}

	@PostMapping("/guardar")
	public String guardarCargo(@ModelAttribute CargoRequestDto cargo, RedirectAttributes redirectAttributes) {
		boolean esNuevo = cargo.getIdCargo() == null;
		try {
			servicioCargo.guardarCargo(cargo);
		} catch (OperacionNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
			return esNuevo ? "redirect:/cargo/crearCargo" : "redirect:/cargo/editarCargo/" + cargo.getIdCargo();
		}
		return "redirect:/cargo";
	}

	@GetMapping("/editarCargo/{idCargo}")
	public String leerpaginaeditar(@PathVariable Long idCargo, Model model) {
		CargoResponseDto existente = servicioCargo.buscarPorId(idCargo);
		CargoRequestDto cargo = new CargoRequestDto();
		cargo.setIdCargo(existente.getIdCargo());
		cargo.setNombreCargo(existente.getNombreCargo());
		cargo.setDescripcion(existente.getDescripcion());
		model.addAttribute("cargo", cargo);
		return "cargo/editarCargo";
	}

	@PostMapping("/desactivar/{idCargo}")
	public String desactivarCargo(@PathVariable Long idCargo, RedirectAttributes redirectAttributes) {
		try {
			servicioCargo.desactivarCargo(idCargo);
		} catch (OperacionNoDisponibleException ex) {
			redirectAttributes.addFlashAttribute("errorMensaje", ex.getMessage());
		}
		return "redirect:/cargo";
	}

}
