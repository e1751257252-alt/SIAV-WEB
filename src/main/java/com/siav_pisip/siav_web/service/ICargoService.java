package com.siav_pisip.siav_web.service;

import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.CargoRequestDto;
import com.siav_pisip.siav_web.model.dto.response.CargoResponseDto;

public interface ICargoService {

	List<CargoResponseDto> listarCargos();

	CargoResponseDto buscarPorId(Long idCargo);

	void guardarCargo(CargoRequestDto nuevoCargo);

	void desactivarCargo(Long idCargo);

}
