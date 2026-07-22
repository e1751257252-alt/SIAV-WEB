package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.MovimientoSaldoRequestDto;
import com.siav_pisip.siav_web.model.dto.response.MovimientoSaldoResponseDto;

public interface IMovimientoSaldoService {

	List<MovimientoSaldoResponseDto> listarMovimientosSaldo();

	MovimientoSaldoResponseDto buscarPorId(Long idMovimiento);

	void guardarMovimientoSaldo(MovimientoSaldoRequestDto nuevoMovimiento);

	void desactivarMovimientoSaldo(Long idMovimiento);
}