package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.HistorialEstadosRequestDto;
import com.siav_pisip.siav_web.model.dto.response.HistorialEstadosResponseDto;

public interface IHistorialEstadosService {

	List<HistorialEstadosResponseDto> listarHistorialEstados();

	void guardarHistorialEstados(HistorialEstadosRequestDto nuevoHistorial);

	HistorialEstadosResponseDto buscarPorId(Long idHistorial);

	void desactivarHistorialEstados(Long idHistorial);
}