package com.siav_pisip.siav_web.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.siav_pisip.siav_web.model.dto.request.RolRequestDto;
import com.siav_pisip.siav_web.model.dto.response.RolResponseDto;
import com.siav_pisip.siav_web.service.IRolService;
import com.siav_pisip.siav_web.service.OperacionNoDisponibleException;

@Service
public class RolServiceImpl implements IRolService {
	private final WebClient webclient;

	public RolServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<RolResponseDto> listarRoles() {

		return webclient.get().uri("/rol").retrieve().bodyToFlux(RolResponseDto.class).collectList().block();
	}

	@Override
	public RolResponseDto buscarPorId(Long idRol) {
		return webclient.get().uri("/rol/{id}", idRol).retrieve().bodyToMono(RolResponseDto.class).block();
	}

	@Override
	public void guardarRol(RolRequestDto nuevoRol) {
		try {
			webclient.post().uri("/rol").bodyValue(nuevoRol).retrieve().toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new OperacionNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

	@Override
	public void desactivarRol(Long idRol) {
		try {
			webclient.delete().uri("/rol/{id}", idRol).retrieve().toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new OperacionNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

}