package com.siav_pisip.siav_web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.siav_pisip.siav_web.model.dto.response.MovimientoSaldoResponseDto;
import com.siav_pisip.siav_web.service.IMovimientoSaldoService;

@Service
public class MovimientoSaldoServiceImpl implements IMovimientoSaldoService {

	private final WebClient webclient;

	public MovimientoSaldoServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<MovimientoSaldoResponseDto> listarMovimientosSaldo() {

		return webclient.get().uri("/movimiento-saldo").retrieve().bodyToFlux(MovimientoSaldoResponseDto.class)
				.collectList().block();
	}

}