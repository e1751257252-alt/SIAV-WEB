package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.SaldoVacacionesRequestDto;
import com.siav_pisip.siav_web.model.dto.response.SaldoVacacionesResponseDto;
import com.siav_pisip.siav_web.service.ISaldoVacacionesService;

@Controller
@RequestMapping("/saldoVacaciones")
public class SaldoVacacionesController {

	@Autowired
	private ISaldoVacacionesService servicioSaldoVacaciones;

	@GetMapping
	public String leerpagina(Model model) {
		List<SaldoVacacionesResponseDto> saldosBD = servicioSaldoVacaciones.listarSaldosVacaciones();
		model.addAttribute("listasaldos", saldosBD);
		return "saldoVacaciones/listarSaldoVacaciones";
	}

	@GetMapping("/crearSaldoVacaciones")
	public String leerpaginacrear(Model model) {
		model.addAttribute("saldo", new SaldoVacacionesRequestDto());
		return "saldoVacaciones/crearSaldoVacaciones";
	}

	@PostMapping("/guardar")
	public String guardarSaldoVacaciones(@ModelAttribute SaldoVacacionesRequestDto saldo) {
		servicioSaldoVacaciones.guardarSaldoVacaciones(saldo);
		return "redirect:/saldoVacaciones";
	}

	@GetMapping("/editarSaldoVacaciones")
	public String leerpaginaeditar() {
		return "saldoVacaciones/editarSaldoVacaciones";
	}

}