package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.EstadoSolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;

public interface IEstadoSolicitudService {

	List<EstadoSolicitudResponseDto> listarEstadosSolicitud();

	EstadoSolicitudResponseDto buscarPorId(Long idEstado);

	void guardarEstadoSolicitud(EstadoSolicitudRequestDto nuevoEstado);

	void desactivarEstadoSolicitud(Long idEstado);
}