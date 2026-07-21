package com.siav_pisip.siav_web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.siav_pisip.siav_web.model.dto.request.SaldoVacacionesRequestDto;
import com.siav_pisip.siav_web.model.dto.response.SaldoVacacionesResponseDto;
import com.siav_pisip.siav_web.service.ISaldoVacacionesService;

@Service
public class SaldoVacacionesServiceImpl implements ISaldoVacacionesService {

	private final WebClient webclient;

	public SaldoVacacionesServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<SaldoVacacionesResponseDto> listarSaldosVacaciones() {
		return webclient.get().uri("/saldo-vacaciones").retrieve().bodyToFlux(SaldoVacacionesResponseDto.class)
				.collectList().block();
	}

	@Override
	public void guardarSaldoVacaciones(SaldoVacacionesRequestDto nuevoSaldo) {
		webclient.post().uri("/saldo-vacaciones").bodyValue(nuevoSaldo).retrieve().toBodilessEntity().block();
	}

}