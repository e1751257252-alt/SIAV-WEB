package com.siav_pisip.siav_web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;
import com.siav_pisip.siav_web.service.IEstadoSolicitudService;

@Service
public class EstadoSolicitudServiceImpl implements IEstadoSolicitudService {

	private final WebClient webclient;

	public EstadoSolicitudServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<EstadoSolicitudResponseDto> listarEstadosSolicitud() {

		return webclient.get().uri("/estado-solicitud").retrieve().bodyToFlux(EstadoSolicitudResponseDto.class)
				.collectList().block();
	}

}