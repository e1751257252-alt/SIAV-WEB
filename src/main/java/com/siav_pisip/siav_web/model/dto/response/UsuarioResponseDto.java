package com.siav_pisip.siav_web.model.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UsuarioResponseDto {

	private Long idUsuario;
	private String cedula;
	private String tipoDocumento;
	private String nombres;
	private String apellidos;
	private String email;
	private Boolean debeCambiarPassword;
	private Long idRol;
	private String nombreRol;
	private Long idJefe;
	private String nombreJefe;
	private Long idCargo;
	private String nombreCargo;
	private LocalDate fechaIngreso;
	private Boolean activo;
	private LocalDateTime fechaCreacion;
}
