package com.siav_pisip.siav_web.model.dto.request;

import lombok.Data;

@Data
public class NotificacionRequestDto {

	private Long idNotificacion;
	private Long idUsuarioDestino;
	private Long idSolicitud;
	private String tipoNotificacion;
	private String mensaje;
	private Boolean leido;
}