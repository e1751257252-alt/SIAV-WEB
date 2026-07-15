package com.siav_pisip.siav_web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.siav_pisip.siav_web.model.dto.response.HistorialEstadosResponseDto;
import com.siav_pisip.siav_web.service.IHistorialEstadosService;

@Service
public class HistorialEstadosServiceImpl implements IHistorialEstadosService {

	private final WebClient webclient;

	public HistorialEstadosServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<HistorialEstadosResponseDto> listarHistorialEstados() {

		return webclient.get().uri("/historial-estados").retrieve().bodyToFlux(HistorialEstadosResponseDto.class)
				.collectList().block();
	}

}