package com.siav_pisip.siav_web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.siav_pisip.siav_web.model.dto.request.SolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;
import com.siav_pisip.siav_web.service.ISolicitudService;

@Service
public class SolicitudServiceImpl implements ISolicitudService {

	private final WebClient webclient;

	public SolicitudServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<SolicitudResponseDto> listarSolicitudes() {
		return webclient.get().uri("/solicitud").retrieve().bodyToFlux(SolicitudResponseDto.class).collectList()
				.block();
	}

	@Override
	public void guardarSolicitud(SolicitudRequestDto nuevaSolicitud) {
		webclient.post().uri("/solicitud").bodyValue(nuevaSolicitud).retrieve().toBodilessEntity().block();
	}

}