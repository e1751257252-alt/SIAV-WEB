package com.siav_pisip.siav_web.service;

import java.time.LocalDate;
import java.util.List;

import com.siav_pisip.siav_web.model.dto.request.SolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;

public interface ISolicitudService {

	List<SolicitudResponseDto> listarSolicitudes();

	SolicitudResponseDto buscarPorId(Long idSolicitud);

	void guardarSolicitud(SolicitudRequestDto nuevaSolicitud);

	void desactivarSolicitud(Long idSolicitud);

	void aprobarSolicitud(Long idSolicitud, Long idAprobador, String observaciones);

	void rechazarSolicitud(Long idSolicitud, Long idAprobador, String observaciones);

	void reprogramarSolicitud(Long idSolicitud, Long idUsuarioEjecutor, LocalDate fechaInicio, LocalDate fechaFin,
			Integer diasSolicitados, String motivo);

	void cancelarAprobacionSolicitud(Long idSolicitud, Long idUsuarioEjecutor, String motivo);

	void archivarSolicitud(Long idSolicitud, Long idUsuario);

	List<Long> listarIdsArchivados(Long idUsuario);
}