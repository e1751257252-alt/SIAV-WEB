package com.siav_pisip.siav_web.model.dto.request;

import lombok.Data;

@Data
public class HistorialEstadosRequestDto {

	private Long idHistorial;
	private Long idSolicitud;
	private Long idEstadoAnterior;
	private Long idEstadoNuevo;
	private Long idUsuarioResponsable;
	private String observaciones;
}