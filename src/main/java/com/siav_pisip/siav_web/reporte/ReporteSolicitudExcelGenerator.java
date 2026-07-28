package com.siav_pisip.siav_web.reporte;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;

import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;

public final class ReporteSolicitudExcelGenerator {

	private static final String[] ENCABEZADOS = { "ID", "Colaborador", "Fecha Inicio", "Fecha Fin", "Días", "Motivo",
			"Estado", "Aprobador", "Fecha Resolución", "Observaciones" };
	private static final int ULTIMA_COLUMNA = ENCABEZADOS.length - 1;
	private static final String RUTA_LOGO = "static/assets/images/logo/logo-siav.png";
	private static final Color AZUL_MARCA = new Color(54, 92, 245);
	private static final Color GRIS_TEXTO = new Color(100, 100, 100);
	private static final Color GRIS_CLARO = new Color(245, 247, 255);
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private ReporteSolicitudExcelGenerator() {
	}

	public static byte[] generar(List<SolicitudResponseDto> solicitudes, LocalDate fechaDesde, LocalDate fechaHasta,
			String nombreColaborador) {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			XSSFSheet hoja = workbook.createSheet("Solicitudes");

			int fila = 0;
			fila = escribirEncabezadoEmpresa(workbook, hoja, fila);
			fila = escribirTitulo(workbook, hoja, fila);
			fila = escribirLineaMeta(workbook, hoja, fila, fechaDesde, fechaHasta, nombreColaborador);
			fila++;
			fila = escribirTabla(workbook, hoja, fila, solicitudes);
			fila++;
			fila = escribirResumen(workbook, hoja, fila, solicitudes);
			escribirPie(workbook, hoja, fila + 1);

			for (int i = 0; i <= ULTIMA_COLUMNA; i++) {
				hoja.autoSizeColumn(i);
			}
			if (hoja.getColumnWidth(0) < 3500) {
				hoja.setColumnWidth(0, 3500);
			}

			ByteArrayOutputStream salida = new ByteArrayOutputStream();
			workbook.write(salida);
			return salida.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException("No se pudo generar el reporte Excel", e);
		}
	}

	private static int escribirEncabezadoEmpresa(XSSFWorkbook workbook, XSSFSheet hoja, int filaInicial) {
		XSSFFont fuenteEmpresa = workbook.createFont();
		fuenteEmpresa.setBold(true);
		fuenteEmpresa.setFontHeightInPoints((short) 14);
		fuenteEmpresa.setColor(new XSSFColor(AZUL_MARCA, null));

		XSSFFont fuenteSubtitulo = workbook.createFont();
		fuenteSubtitulo.setFontHeightInPoints((short) 10);
		fuenteSubtitulo.setColor(new XSSFColor(GRIS_TEXTO, null));

		crearFilaMerge(hoja, filaInicial, "INNOBIX S.A.", estilo(workbook, fuenteEmpresa, HorizontalAlignment.LEFT));
		crearFilaMerge(hoja, filaInicial + 1, "SIAV - Sistema de Administración de Vacaciones",
				estilo(workbook, fuenteSubtitulo, HorizontalAlignment.LEFT));
		crearFilaMerge(hoja, filaInicial + 2, "Av. República del Salvador N35-82, Quito, Ecuador",
				estilo(workbook, fuenteSubtitulo, HorizontalAlignment.LEFT));
		hoja.getRow(filaInicial).setHeightInPoints(20);

		cargarLogo(workbook).ifPresent(bytesLogo -> {
			int idImagen = workbook.addPicture(bytesLogo, Workbook.PICTURE_TYPE_PNG);
			XSSFDrawing dibujo = hoja.createDrawingPatriarch();
			XSSFClientAnchor ancla = new XSSFClientAnchor();
			ancla.setCol1(ULTIMA_COLUMNA - 1);
			ancla.setRow1(filaInicial);
			ancla.setCol2(ULTIMA_COLUMNA + 1);
			ancla.setRow2(filaInicial + 3);
			dibujo.createPicture(ancla, idImagen).resize(0.9);
		});

		return filaInicial + 3;
	}

	private static int escribirTitulo(XSSFWorkbook workbook, XSSFSheet hoja, int fila) {
		XSSFFont fuenteTitulo = workbook.createFont();
		fuenteTitulo.setBold(true);
		fuenteTitulo.setFontHeightInPoints((short) 13);
		fuenteTitulo.setUnderline(XSSFFont.U_SINGLE);

		crearFilaMerge(hoja, fila, "REPORTE DE SOLICITUDES DE VACACIONES",
				estilo(workbook, fuenteTitulo, HorizontalAlignment.CENTER));
		hoja.getRow(fila).setHeightInPoints(22);
		return fila + 1;
	}

	private static int escribirLineaMeta(XSSFWorkbook workbook, XSSFSheet hoja, int fila, LocalDate fechaDesde,
			LocalDate fechaHasta, String nombreColaborador) {
		String rango = (fechaDesde == null && fechaHasta == null) ? "Todo el histórico"
				: (fechaDesde == null ? "Hasta " : fechaDesde.format(FORMATO_FECHA) + " — ")
						+ (fechaHasta == null ? "" : fechaHasta.format(FORMATO_FECHA));
		String colaborador = (nombreColaborador == null || nombreColaborador.isBlank()) ? "Todos" : nombreColaborador;
		String emision = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

		XSSFFont fuenteMeta = workbook.createFont();
		fuenteMeta.setFontHeightInPoints((short) 10);
		XSSFCellStyle estiloMeta = estilo(workbook, fuenteMeta, HorizontalAlignment.LEFT);
		estiloMeta.setBorderTop(BorderStyle.THIN);
		estiloMeta.setBorderBottom(BorderStyle.THIN);
		estiloMeta.setBorderLeft(BorderStyle.THIN);
		estiloMeta.setBorderRight(BorderStyle.THIN);

		Row filaMeta = hoja.createRow(fila);
		filaMeta.setHeightInPoints(18);
		int mitad = ULTIMA_COLUMNA / 2;

		Cell celdaIzquierda = filaMeta.createCell(0);
		celdaIzquierda.setCellValue("FECHA DE EMISIÓN: " + emision);
		celdaIzquierda.setCellStyle(estiloMeta);
		hoja.addMergedRegion(new CellRangeAddress(fila, fila, 0, mitad));
		for (int i = 1; i <= mitad; i++) {
			filaMeta.createCell(i).setCellStyle(estiloMeta);
		}

		Cell celdaDerecha = filaMeta.createCell(mitad + 1);
		celdaDerecha.setCellValue("RANGO: " + rango + "     COLABORADOR: " + colaborador);
		celdaDerecha.setCellStyle(estiloMeta);
		hoja.addMergedRegion(new CellRangeAddress(fila, fila, mitad + 1, ULTIMA_COLUMNA));
		for (int i = mitad + 2; i <= ULTIMA_COLUMNA; i++) {
			filaMeta.createCell(i).setCellStyle(estiloMeta);
		}

		return fila + 1;
	}

	private static int escribirTabla(XSSFWorkbook workbook, XSSFSheet hoja, int fila,
			List<SolicitudResponseDto> solicitudes) {
		XSSFFont fuenteEncabezado = workbook.createFont();
		fuenteEncabezado.setBold(true);
		fuenteEncabezado.setColor(new XSSFColor(Color.WHITE, null));

		XSSFCellStyle estiloEnc = workbook.createCellStyle();
		estiloEnc.setFont(fuenteEncabezado);
		estiloEnc.setFillForegroundColor(new XSSFColor(AZUL_MARCA, null));
		estiloEnc.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		estiloEnc.setAlignment(HorizontalAlignment.CENTER);
		estiloEnc.setVerticalAlignment(VerticalAlignment.CENTER);

		Row filaEncabezado = hoja.createRow(fila);
		filaEncabezado.setHeightInPoints(18);
		for (int i = 0; i < ENCABEZADOS.length; i++) {
			Cell celda = filaEncabezado.createCell(i);
			celda.setCellValue(ENCABEZADOS[i]);
			celda.setCellStyle(estiloEnc);
		}

		CellStyle estiloFilaClara = workbook.createCellStyle();
		XSSFCellStyle estiloFilaOscura = workbook.createCellStyle();
		estiloFilaOscura.setFillForegroundColor(new XSSFColor(GRIS_CLARO, null));
		estiloFilaOscura.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		int numeroFila = fila + 1;
		boolean filaClara = true;
		for (SolicitudResponseDto solicitud : solicitudes) {
			Row filaDatos = hoja.createRow(numeroFila++);
			CellStyle estiloActual = filaClara ? estiloFilaClara : estiloFilaOscura;
			crearCelda(filaDatos, 0, String.valueOf(solicitud.getIdSolicitud()), estiloActual);
			crearCelda(filaDatos, 1, solicitud.getNombresUsuario() + " " + solicitud.getApellidosUsuario(),
					estiloActual);
			crearCelda(filaDatos, 2, String.valueOf(solicitud.getFechaInicio()), estiloActual);
			crearCelda(filaDatos, 3, String.valueOf(solicitud.getFechaFin()), estiloActual);
			crearCelda(filaDatos, 4,
					String.valueOf(solicitud.getDiasSolicitados() == null ? 0 : solicitud.getDiasSolicitados()),
					estiloActual);
			crearCelda(filaDatos, 5, solicitud.getMotivo() == null ? "" : solicitud.getMotivo(), estiloActual);
			crearCelda(filaDatos, 6, solicitud.getNombreEstado(), estiloActual);
			crearCelda(filaDatos, 7,
					solicitud.getNombresAprobador() == null ? "" : solicitud.getNombresAprobador(), estiloActual);
			crearCelda(filaDatos, 8,
					solicitud.getFechaResolucion() == null ? "" : String.valueOf(solicitud.getFechaResolucion()),
					estiloActual);
			crearCelda(filaDatos, 9,
					solicitud.getObservacionesAprobador() == null ? "" : solicitud.getObservacionesAprobador(),
					estiloActual);
			filaClara = !filaClara;
		}

		if (solicitudes.isEmpty()) {
			Row filaVacia = hoja.createRow(numeroFila++);
			Cell celdaVacia = filaVacia.createCell(0);
			celdaVacia.setCellValue("No hay solicitudes que coincidan con los filtros aplicados.");
			hoja.addMergedRegion(new CellRangeAddress(filaVacia.getRowNum(), filaVacia.getRowNum(), 0,
					ULTIMA_COLUMNA));
		}

		return numeroFila;
	}

	private static int escribirResumen(XSSFWorkbook workbook, XSSFSheet hoja, int fila,
			List<SolicitudResponseDto> solicitudes) {
		long aprobadas = solicitudes.stream().filter(s -> "Aprobada".equalsIgnoreCase(s.getNombreEstado())).count();
		long rechazadas = solicitudes.stream().filter(s -> "Rechazada".equalsIgnoreCase(s.getNombreEstado())).count();

		XSSFFont fuenteResumen = workbook.createFont();
		fuenteResumen.setBold(true);
		fuenteResumen.setFontHeightInPoints((short) 10);

		XSSFCellStyle estiloResumen = estilo(workbook, fuenteResumen,
				HorizontalAlignment.LEFT);
		estiloResumen.setFillForegroundColor(new XSSFColor(GRIS_CLARO, null));
		estiloResumen.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		crearFilaMerge(hoja, fila,
				"TOTAL: " + solicitudes.size() + " solicitudes     APROBADAS: " + aprobadas + "     RECHAZADAS: "
						+ rechazadas,
				estiloResumen);
		hoja.getRow(fila).setHeightInPoints(18);
		return fila + 1;
	}

	private static void escribirPie(XSSFWorkbook workbook, XSSFSheet hoja, int fila) {
		XSSFFont fuentePie = workbook.createFont();
		fuentePie.setItalic(true);
		fuentePie.setFontHeightInPoints((short) 8);
		fuentePie.setColor(new XSSFColor(GRIS_TEXTO, null));

		crearFilaMerge(hoja, fila, "Documento generado automáticamente por SIAV.",
				estilo(workbook, fuentePie, HorizontalAlignment.LEFT));
	}

	private static XSSFCellStyle estilo(XSSFWorkbook workbook, XSSFFont fuente,
			HorizontalAlignment alineacion) {
		XSSFCellStyle estilo = workbook.createCellStyle();
		estilo.setFont(fuente);
		estilo.setAlignment(alineacion);
		estilo.setVerticalAlignment(VerticalAlignment.CENTER);
		return estilo;
	}

	private static void crearFilaMerge(XSSFSheet hoja, int numeroFila, String texto, CellStyle estilo) {
		Row fila = hoja.getRow(numeroFila);
		if (fila == null) {
			fila = hoja.createRow(numeroFila);
		}
		Cell celda = fila.createCell(0);
		celda.setCellValue(texto);
		celda.setCellStyle(estilo);
		for (int i = 1; i <= ULTIMA_COLUMNA; i++) {
			fila.createCell(i).setCellStyle(estilo);
		}
		hoja.addMergedRegion(new CellRangeAddress(numeroFila, numeroFila, 0, ULTIMA_COLUMNA));
	}

	private static void crearCelda(Row fila, int columna, String texto, CellStyle estilo) {
		Cell celda = fila.createCell(columna);
		celda.setCellValue(texto);
		celda.setCellStyle(estilo);
	}

	private static Optional<byte[]> cargarLogo(XSSFWorkbook workbook) {
		try (InputStream entrada = new ClassPathResource(RUTA_LOGO).getInputStream()) {
			return Optional.of(entrada.readAllBytes());
		} catch (IOException e) {
			return Optional.empty();
		}
	}

}
