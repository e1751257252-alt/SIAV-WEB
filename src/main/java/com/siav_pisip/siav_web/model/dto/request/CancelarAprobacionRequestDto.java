package com.siav_pisip.siav_web.model.dto.request;

import lombok.Data;

@Data
public class CancelarAprobacionRequestDto {

	private Long idUsuarioEjecutor;
	private String motivo;

}
