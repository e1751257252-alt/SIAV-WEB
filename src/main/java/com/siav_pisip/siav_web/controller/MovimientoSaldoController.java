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

import com.siav_pisip.siav_web.model.dto.request.MovimientoSaldoRequestDto;
import com.siav_pisip.siav_web.model.dto.response.MovimientoSaldoResponseDto;
import com.siav_pisip.siav_web.service.IMovimientoSaldoService;
import com.siav_pisip.siav_web.service.ISaldoVacacionesService;
import com.siav_pisip.siav_web.service.ISolicitudService;

@Controller
@RequestMapping("/movimientoSaldo")
public class MovimientoSaldoController {

	@Autowired
	private IMovimientoSaldoService servicioMovimientoSaldo;

	@Autowired
	private ISaldoVacacionesService servicioSaldoVacaciones;

	@Autowired
	private ISolicitudService servicioSolicitud;

	@GetMapping
	public String leerpagina(Model model) {
		List<MovimientoSaldoResponseDto> movimientosBD = servicioMovimientoSaldo.listarMovimientosSaldo();
		model.addAttribute("listamovimientos", movimientosBD);
		return "movimientoSaldo/listarMovimientoSaldo";
	}

	@GetMapping("/crearMovimientoSaldo")
	public String leerpaginacrear(Model model) {
		model.addAttribute("movimiento", new MovimientoSaldoRequestDto());
		model.addAttribute("listasaldos", servicioSaldoVacaciones.listarSaldosVacaciones());
		model.addAttribute("listasolicitudes", servicioSolicitud.listarSolicitudes());
		return "movimientoSaldo/crearMovimientoSaldo";
	}

	@PostMapping("/guardar")
	public String guardarMovimientoSaldo(@ModelAttribute MovimientoSaldoRequestDto movimiento) {
		servicioMovimientoSaldo.guardarMovimientoSaldo(movimiento);
		return "redirect:/movimientoSaldo";
	}

	@GetMapping("/editarMovimientoSaldo/{idMovimiento}")
	public String leerpaginaeditar(@PathVariable Long idMovimiento, Model model) {
		MovimientoSaldoResponseDto existente = servicioMovimientoSaldo.buscarPorId(idMovimiento);
		MovimientoSaldoRequestDto movimiento = new MovimientoSaldoRequestDto();
		movimiento.setIdMovimiento(existente.getIdMovimiento());
		movimiento.setIdSaldo(existente.getIdSaldo());
		movimiento.setIdSolicitud(existente.getIdSolicitud());
		movimiento.setTipoMovimiento(existente.getTipoMovimiento());
		movimiento.setDias(existente.getDias());
		movimiento.setDescripcion(existente.getDescripcion());
		model.addAttribute("movimiento", movimiento);
		model.addAttribute("listasaldos", servicioSaldoVacaciones.listarSaldosVacaciones());
		model.addAttribute("listasolicitudes", servicioSolicitud.listarSolicitudes());
		return "movimientoSaldo/editarMovimientoSaldo";
	}

	@PostMapping("/desactivar/{idMovimiento}")
	public String desactivarMovimientoSaldo(@PathVariable Long idMovimiento) {
		servicioMovimientoSaldo.desactivarMovimientoSaldo(idMovimiento);
		return "redirect:/movimientoSaldo";
	}

}