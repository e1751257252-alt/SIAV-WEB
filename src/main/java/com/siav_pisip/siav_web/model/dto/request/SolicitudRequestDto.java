package com.siav_pisip.siav_web.model.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SolicitudRequestDto {

	private Long idSolicitud;
	private Long idUsuario;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Integer diasSolicitados;
	private String motivo;
	private Long idEstado;
	private Long idAprobador;
	private String observacionesAprobador;
}