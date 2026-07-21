package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.UsuarioRequestDto;
import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;

public interface IUsuarioService {

	List<UsuarioResponseDto> listarUsuarios();

	void guardarUsuario(UsuarioRequestDto nuevoUsuario);
}