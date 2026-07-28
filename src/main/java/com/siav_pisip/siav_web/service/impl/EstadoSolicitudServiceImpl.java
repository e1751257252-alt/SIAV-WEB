package com.siav_pisip.siav_web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.siav_pisip.siav_web.model.dto.request.EstadoSolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;
import com.siav_pisip.siav_web.service.IEstadoSolicitudService;
import com.siav_pisip.siav_web.service.OperacionNoDisponibleException;

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

	@Override
	public EstadoSolicitudResponseDto buscarPorId(Long idEstado) {
		return webclient.get().uri("/estado-solicitud/{id}", idEstado).retrieve()
				.bodyToMono(EstadoSolicitudResponseDto.class).block();
	}

	@Override
	public void guardarEstadoSolicitud(EstadoSolicitudRequestDto nuevoEstado) {
		try {
			webclient.post().uri("/estado-solicitud").bodyValue(nuevoEstado).retrieve().toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new OperacionNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

	@Override
	public void desactivarEstadoSolicitud(Long idEstado) {
		try {
			webclient.delete().uri("/estado-solicitud/{id}", idEstado).retrieve().toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new OperacionNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

}