package com.siav_pisip.siav_web.model.dto.request;

import lombok.Data;

@Data
public class MovimientoSaldoRequestDto {

	private Long idMovimiento;
	private Long idSaldo;
	private Long idSolicitud;
	private String tipoMovimiento;
	private Integer dias;
	private String descripcion;
}