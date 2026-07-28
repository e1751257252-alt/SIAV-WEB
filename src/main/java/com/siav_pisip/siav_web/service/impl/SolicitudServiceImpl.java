package com.siav_pisip.siav_web.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.siav_pisip.siav_web.model.dto.request.ArchivarSolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.request.CancelarAprobacionRequestDto;
import com.siav_pisip.siav_web.model.dto.request.ReprogramarSolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.request.ResolverSolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.request.SolicitudRequestDto;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;
import com.siav_pisip.siav_web.service.ISolicitudService;
import com.siav_pisip.siav_web.service.SolicitudNoDisponibleException;

@Service
public class SolicitudServiceImpl implements ISolicitudService {

	private final WebClient webclient;

	public SolicitudServiceImpl(WebClient webclient) {
		this.webclient = webclient;
	}

	@Override
	public List<SolicitudResponseDto> listarSolicitudes() {
		return webclient.get().uri("/solicitud").retrieve().bodyToFlux(SolicitudResponseDto.class).collectList()
				.block();
	}

	@Override
	public SolicitudResponseDto buscarPorId(Long idSolicitud) {
		return webclient.get().uri("/solicitud/{id}", idSolicitud).retrieve().bodyToMono(SolicitudResponseDto.class)
				.block();
	}

	@Override
	public void guardarSolicitud(SolicitudRequestDto nuevaSolicitud) {
		try {
			webclient.post().uri("/solicitud").bodyValue(nuevaSolicitud).retrieve().toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new SolicitudNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

	@Override
	public void desactivarSolicitud(Long idSolicitud) {
		webclient.delete().uri("/solicitud/{id}", idSolicitud).retrieve().toBodilessEntity().block();
	}

	@Override
	public void aprobarSolicitud(Long idSolicitud, Long idAprobador, String observaciones) {
		resolver(idSolicitud, idAprobador, observaciones, "aprobar");
	}

	@Override
	public void rechazarSolicitud(Long idSolicitud, Long idAprobador, String observaciones) {
		resolver(idSolicitud, idAprobador, observaciones, "rechazar");
	}

	private void resolver(Long idSolicitud, Long idAprobador, String observaciones, String accion) {
		ResolverSolicitudRequestDto request = new ResolverSolicitudRequestDto();
		request.setIdAprobador(idAprobador);
		request.setObservaciones(observaciones);
		try {
			webclient.post().uri("/solicitud/{id}/{accion}", idSolicitud, accion).bodyValue(request).retrieve()
					.toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new SolicitudNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

	@Override
	public void reprogramarSolicitud(Long idSolicitud, Long idUsuarioEjecutor, LocalDate fechaInicio,
			LocalDate fechaFin, Integer diasSolicitados, String motivo) {
		ReprogramarSolicitudRequestDto request = new ReprogramarSolicitudRequestDto();
		request.setIdUsuarioEjecutor(idUsuarioEjecutor);
		request.setFechaInicio(fechaInicio);
		request.setFechaFin(fechaFin);
		request.setDiasSolicitados(diasSolicitados);
		request.setMotivo(motivo);
		try {
			webclient.post().uri("/solicitud/{id}/reprogramar", idSolicitud).bodyValue(request).retrieve()
					.toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new SolicitudNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

	@Override
	public void cancelarAprobacionSolicitud(Long idSolicitud, Long idUsuarioEjecutor, String motivo) {
		CancelarAprobacionRequestDto request = new CancelarAprobacionRequestDto();
		request.setIdUsuarioEjecutor(idUsuarioEjecutor);
		request.setMotivo(motivo);
		try {
			webclient.post().uri("/solicitud/{id}/cancelar-aprobacion", idSolicitud).bodyValue(request).retrieve()
					.toBodilessEntity().block();
		} catch (WebClientResponseException ex) {
			throw new SolicitudNoDisponibleException(ex.getResponseBodyAsString());
		}
	}

	@Override
	public void archivarSolicitud(Long idSolicitud, Long idUsuario) {
		ArchivarSolicitudRequestDto request = new ArchivarSolicitudRequestDto();
		request.setIdUsuario(idUsuario);
		webclient.post().uri("/solicitud/{id}/archivar", idSolicitud).bodyValue(request).retrieve()
				.toBodilessEntity().block();
	}

	@Override
	public List<Long> listarIdsArchivados(Long idUsuario) {
		return webclient.get().uri("/solicitud/archivadas/{idUsuario}", idUsuario).retrieve()
				.bodyToFlux(Long.class).collectList().block();
	}

}