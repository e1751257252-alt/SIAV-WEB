package com.siav_pisip.siav_web.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import com.siav_pisip.siav_web.model.dto.response.RolResponseDto;
import com.siav_pisip.siav_web.service.IRolService;

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

}