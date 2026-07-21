package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.SaldoVacacionesRequestDto;
import com.siav_pisip.siav_web.model.dto.response.SaldoVacacionesResponseDto;

public interface ISaldoVacacionesService {

	List<SaldoVacacionesResponseDto> listarSaldosVacaciones();

	void guardarSaldoVacaciones(SaldoVacacionesRequestDto nuevoSaldo);
}