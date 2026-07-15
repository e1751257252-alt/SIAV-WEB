package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.response.MovimientoSaldoResponseDto;

public interface IMovimientoSaldoService {

	List<MovimientoSaldoResponseDto> listarMovimientosSaldo();
}