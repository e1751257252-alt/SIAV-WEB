package com.siav_pisip.siav_web.model.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class HistorialEstadosResponseDto {

	private Long idHistorial;
	private Long idSolicitud;
	private Long idEstadoAnterior;
	private String nombreEstadoAnterior;
	private Long idEstadoNuevo;
	private String nombreEstadoNuevo;
	private Long idUsuarioResponsable;
	private String nombresUsuarioResponsable;
	private LocalDateTime fechaCambio;
	private String observaciones;
	private Boolean activo;
}