package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.request.MovimientoSaldoRequestDto;
import com.siav_pisip.siav_web.model.dto.response.MovimientoSaldoResponseDto;
import com.siav_pisip.siav_web.service.IMovimientoSaldoService;

@Controller
@RequestMapping("/movimientoSaldo")
public class MovimientoSaldoController {

	@Autowired
	private IMovimientoSaldoService servicioMovimientoSaldo;

	@GetMapping
	public String leerpagina(Model model) {
		List<MovimientoSaldoResponseDto> movimientosBD = servicioMovimientoSaldo.listarMovimientosSaldo();
		model.addAttribute("listamovimientos", movimientosBD);
		return "movimientoSaldo/listarMovimientoSaldo";
	}

	@GetMapping("/crearMovimientoSaldo")
	public String leerpaginacrear(Model model) {
		model.addAttribute("movimiento", new MovimientoSaldoRequestDto());
		return "movimientoSaldo/crearMovimientoSaldo";
	}

	@PostMapping("/guardar")
	public String guardarMovimientoSaldo(@ModelAttribute MovimientoSaldoRequestDto movimiento) {
		servicioMovimientoSaldo.guardarMovimientoSaldo(movimiento);
		return "redirect:/movimientoSaldo";
	}

	@GetMapping("/editarMovimientoSaldo")
	public String leerpaginaeditar() {
		return "movimientoSaldo/editarMovimientoSaldo";
	}

}