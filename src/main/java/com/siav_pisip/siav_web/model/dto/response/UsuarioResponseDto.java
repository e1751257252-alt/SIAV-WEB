package com.siav_pisip.siav_web.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioResponseDto {

	private Long idUsuario;
	private String cedula;
	private String nombres;
	private String apellidos;
	private String email;
	private Long idRol;
	private String nombreRol;
	private Long idJefe;
	private String nombreJefe;
	private String cargo;
	private LocalDate fechaIngreso;
	private Boolean activo;
	private LocalDateTime fechaCreacion;
}