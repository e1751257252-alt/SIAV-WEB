package com.siav_pisip.siav_web.model.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MovimientoSaldoResponseDto {

	private Long idMovimiento;
	private Long idSaldo;
	private Long idSolicitud;
	private String tipoMovimiento;
	private Integer dias;
	private LocalDateTime fechaMovimiento;
	private String descripcion;
}