package com.siav_pisip.siav_web.model.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ReprogramarSolicitudRequestDto {

	private Long idUsuarioEjecutor;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Integer diasSolicitados;
	private String motivo;

}
