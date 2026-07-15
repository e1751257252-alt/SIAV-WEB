package com.siav_pisip.siav_web.model.dto.request;

import com.siav_pisip.siav_web.model.dto.response.EstadoSolicitudResponseDto;
import lombok.Data;

@Data
public class HistorialEstadosRequestDto {

	private Long idHistorial;
	private Long idSolicitud;
	private EstadoSolicitudResponseDto estadoAnterior;
	private EstadoSolicitudResponseDto estadoNuevo;
	private Long idUsuarioResponsable;
	private String observaciones;
}