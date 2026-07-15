package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;

public interface IEstadoSolicitudService {

	List<EstadoSolicitudResponseDto> listarEstadosSolicitud();
}