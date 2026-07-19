package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.NotificacionRequestDto;
import com.siav_pisip.siav_web.model.dto.response.NotificacionResponseDto;

public interface INotificacionService {

	List<NotificacionResponseDto> listarNotificaciones();

	void guardarNotificacion(NotificacionRequestDto nuevaNotificacion);
}