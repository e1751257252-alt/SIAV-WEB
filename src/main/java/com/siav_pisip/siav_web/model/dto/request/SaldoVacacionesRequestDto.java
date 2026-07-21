package com.siav_pisip.siav_web.model.dto.request;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SaldoVacacionesRequestDto {

	private Long idSaldo;
	private Long idUsuario;
	private Integer anio;
	private BigDecimal diasAsignados;
	private BigDecimal diasDisponibles;
}