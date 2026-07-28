package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.RolRequestDto;
import com.siav_pisip.siav_web.model.dto.response.RolResponseDto;

public interface IRolService {

	List<RolResponseDto> listarRoles();

	RolResponseDto buscarPorId(Long idRol);

	void guardarRol(RolRequestDto nuevoRol);

	void desactivarRol(Long idRol);

}
