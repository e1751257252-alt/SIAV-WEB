package com.siav_pisip.siav_web.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SolicitudResponseDto {

	private Long idSolicitud;
	private Long idUsuario;
	private String nombresUsuario;
	private String apellidosUsuario;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Integer diasSolicitados;
	private String motivo;
	private Long idEstado;
	private String nombreEstado;
	private LocalDateTime fechaSolicitud;
	private Long idAprobador;
	private String nombresAprobador;
	private LocalDateTime fechaResolucion;
	private String observacionesAprobador;
}