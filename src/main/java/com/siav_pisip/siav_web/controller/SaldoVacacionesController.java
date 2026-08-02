package com.siav_pisip.siav_web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.siav_pisip.siav_web.model.dto.response.SaldoVacacionesResponseDto;
import com.siav_pisip.siav_web.service.ISaldoVacacionesService;

/**
 * El saldo de vacaciones ya no se llena a mano: se crea automáticamente en
 * el backend cuando se registra un usuario nuevo (RF: saldo automático). Esta
 * pantalla queda solo como consulta/auditoría, por eso no expone crear/editar.
 */
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

	@PostMapping("/desactivar/{idSaldo}")
	public String desactivarSaldoVacaciones(@PathVariable Long idSaldo) {
		servicioSaldoVacaciones.desactivarSaldoVacaciones(idSaldo);
		return "redirect:/saldoVacaciones";
	}

}
