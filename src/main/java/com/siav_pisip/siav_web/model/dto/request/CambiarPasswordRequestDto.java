package com.siav_pisip.siav_web.model.dto.request;

import lombok.Data;

@Data
public class CambiarPasswordRequestDto {

	private String passwordActual;
	private String passwordNueva;
}
