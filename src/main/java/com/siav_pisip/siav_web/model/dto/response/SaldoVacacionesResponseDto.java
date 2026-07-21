package com.siav_pisip.siav_web.model.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SaldoVacacionesResponseDto {

	private Long idSaldo;
	private Long idUsuario;
	private String nombresUsuario;
	private String apellidosUsuario;
	private Integer anio;
	private BigDecimal diasAsignados;
	private BigDecimal diasDisponibles;
	private LocalDateTime fechaActualizacion;
}