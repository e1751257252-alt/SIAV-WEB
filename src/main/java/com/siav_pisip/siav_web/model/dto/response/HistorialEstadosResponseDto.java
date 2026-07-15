package com.siav_pisip.siav_web.model.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class HistorialEstadosResponseDto {

	private Long idHistorial;
	private Long idSolicitud;
	private EstadoSolicitudResponseDto estadoAnterior;
	private EstadoSolicitudResponseDto estadoNuevo;
	private Long idUsuarioResponsable;
	private String nombresUsuarioResponsable;
	private LocalDateTime fechaCambio;
	private String observaciones;
}