package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.UsuarioRequestDto;
import com.siav_pisip.siav_web.model.dto.response.UsuarioResponseDto;

public interface IUsuarioService {

	List<UsuarioResponseDto> listarUsuarios();

	UsuarioResponseDto buscarPorId(Long idUsuario);

	void guardarUsuario(UsuarioRequestDto nuevoUsuario);

	void desactivarUsuario(Long idUsuario);

	UsuarioResponseDto autenticar(String email, String password);
}