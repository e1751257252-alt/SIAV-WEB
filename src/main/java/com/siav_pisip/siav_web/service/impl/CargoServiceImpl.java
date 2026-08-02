package com.siav_pisip.siav_web.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.siav_pisip.siav_web.model.dto.request.CargoRequestDto;
import com.siav_pisip.siav_web.model.dto.response.CargoResponseDto;
import com.siav_pisip.siav_web.service.ICargoService;
import com.siav_pisip.siav_web.service.OperacionNoDisponibleException;

@Service
public class CargoServiceImpl implements ICargoService {
	private final WebClient webclient;

	public CargoServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<CargoResponseDto> listarCargos() {
		return webclient.get().uri("/cargo").retrieve().bodyToFlux(CargoResponseDto.class).collectList().block();
	}

	@Override
	public CargoResponseDto buscarPorId(Long idCargo) {
		return webclient.get().uri("/cargo/{id}", idCargo).retrieve().bodyToMono(CargoResponseDto.class).block();
	}

	@Override
	public void guardarCargo(CargoRequestDto nuevoCargo) {
		try {
			webclient.post().uri("/cargo").bodyValue(nuevoCargo).retrieve().toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new OperacionNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

	@Override
	public void desactivarCargo(Long idCargo) {
		try {
			webclient.delete().uri("/cargo/{id}", idCargo).retrieve().toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new OperacionNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

}
