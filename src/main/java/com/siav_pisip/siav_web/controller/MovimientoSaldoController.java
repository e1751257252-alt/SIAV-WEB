package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.response.MovimientoSaldoResponseDto;
import com.siav_pisip.siav_web.service.IMovimientoSaldoService;

@Controller
@RequestMapping("/movimientoSaldo")
public class MovimientoSaldoController {
	
	@Autowired
	private IMovimientoSaldoService servicioMovimientoSaldo;

	@GetMapping
	public String leerpagina() {
		List<MovimientoSaldoResponseDto> movimientosBD = servicioMovimientoSaldo.listarMovimientosSaldo();
		System.out.println(movimientosBD);
		return "movimientoSaldo/listarMovimientoSaldo";
	}

	@GetMapping("/crearMovimientoSaldo")
	public String leerpaginacrear() {
		return "movimientoSaldo/crearMovimientoSaldo";
	}

	@GetMapping("/editarMovimientoSaldo")
	public String leerpaginaeditar() {
		return "movimientoSaldo/editarMovimientoSaldo";
	}

}