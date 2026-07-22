package com.siav_pisip.siav_web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.siav_pisip.siav_web.model.dto.request.NotificacionRequestDto;
import com.siav_pisip.siav_web.model.dto.response.NotificacionResponseDto;
import com.siav_pisip.siav_web.service.INotificacionService;

@Service
public class NotificacionServiceImpl implements INotificacionService {

	private final WebClient webclient;

	public NotificacionServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<NotificacionResponseDto> listarNotificaciones() {
		return webclient.get().uri("/notificacion").retrieve().bodyToFlux(NotificacionResponseDto.class).collectList()
				.block();
	}

	@Override
	public void guardarNotificacion(NotificacionRequestDto nuevaNotificacion) {
		webclient.post().uri("/notificacion").bodyValue(nuevaNotificacion).retrieve().toBodilessEntity().block();
	}

	@Override
	public NotificacionResponseDto buscarPorId(Long idNotificacion) {
		return webclient.get().uri("/notificacion/{id}", idNotificacion).retrieve()
				.bodyToMono(NotificacionResponseDto.class).block();
	}

	@Override
	public void desactivarNotificacion(Long idNotificacion) {
		webclient.delete().uri("/notificacion/{id}", idNotificacion).retrieve().toBodilessEntity().block();
	}

}