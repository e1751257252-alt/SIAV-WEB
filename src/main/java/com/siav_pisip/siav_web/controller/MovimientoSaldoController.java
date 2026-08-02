package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.response.MovimientoSaldoResponseDto;
import com.siav_pisip.siav_web.service.IMovimientoSaldoService;

/**
 * Los movimientos de saldo ya no se llenan a mano: SolicitudUseCaseImpl los
 * registra automáticamente en el backend cada vez que una solicitud se
 * aprueba, rechaza, cancela o reprograma. Esta pantalla queda solo como
 * historial/auditoría de esos movimientos automáticos.
 */
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

	@PostMapping("/desactivar/{idMovimiento}")
	public String desactivarMovimientoSaldo(@PathVariable Long idMovimiento) {
		servicioMovimientoSaldo.desactivarMovimientoSaldo(idMovimiento);
		return "redirect:/movimientoSaldo";
	}

}
