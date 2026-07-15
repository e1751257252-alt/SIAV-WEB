package com.siav_pisip.siav_web.model.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NotificacionResponseDto {

	private Long idNotificacion;
	private Long idUsuarioDestino;
	private String nombresUsuarioDestino;
	private Long idSolicitud;
	private String tipoNotificacion;
	private String mensaje;
	private Boolean leido;
	private LocalDateTime fechaLectura;
	private LocalDateTime fechaCreacion;
}