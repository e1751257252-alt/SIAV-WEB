package com.siav_pisip.siav_web.model.dto.request;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SolicitudRequestDto {

	private Long idSolicitud;
	private Long idUsuario;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaInicio;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fechaFin;
	private Integer diasSolicitados;
	private String motivo;
	private Long idEstado;
	private Long idAprobador;
	private String observacionesAprobador;
}