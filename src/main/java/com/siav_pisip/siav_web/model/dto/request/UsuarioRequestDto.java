package com.siav_pisip.siav_web.model.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioRequestDto {

	private Long idUsuario;
	private String cedula;
	private String nombres;
	private String apellidos;
	private String email;
	private String passwordHash;
	private Long idRol;
	private Long idJefe;
	private String cargo;
	private LocalDate fechaIngreso;
	private Boolean activo;
}