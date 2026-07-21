package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.SolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;

public interface ISolicitudService {

	List<SolicitudResponseDto> listarSolicitudes();

	void guardarSolicitud(SolicitudRequestDto nuevaSolicitud);
}