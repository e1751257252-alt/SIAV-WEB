package com.siav_pisip.siav_web.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.siav_pisip.siav_web.model.dto.request.UsuarioRequestDto;
import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;
import com.siav_pisip.siav_web.service.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	private final WebClient webclient;

	public UsuarioServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<UsuarioResponseDto> listarUsuarios() {
		return webclient.get().uri("/usuario").retrieve().bodyToFlux(UsuarioResponseDto.class).collectList().block();
	}

	@Override
	public void guardarUsuario(UsuarioRequestDto nuevoUsuario) {
		webclient.post().uri("/usuario").bodyValue(nuevoUsuario).retrieve().toBodilessEntity().block();
	}

}