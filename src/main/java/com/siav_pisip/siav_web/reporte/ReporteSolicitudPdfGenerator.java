package com.siav_pisip.siav_web.reporte;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.core.io.ClassPathResource;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.siav_pisip.siav_web.model.dto.response.SolicitudResponseDto;

public final class ReporteSolicitudPdfGenerator {

	private static final String[] ENCABEZADOS = { "ID", "Colaborador", "Fecha Inicio", "Fecha Fin", "Días", "Estado",
			"Aprobador" };
	private static final String RUTA_LOGO = "static/assets/images/logo/logo-siav.png";
	private static final Color AZUL_MARCA = new Color(54, 92, 245);
	private static final Color GRIS_TEXTO = new Color(100, 100, 100);
	private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	private ReporteSolicitudPdfGenerator() {
	}

	public static byte[] generar(List<SolicitudResponseDto> solicitudes, LocalDate fechaDesde, LocalDate fechaHasta,
			String nombreColaborador) {
		Document documento = new Document(PageSize.A4, 36, 36, 36, 36);
		ByteArrayOutputStream salida = new ByteArrayOutputStream();
		try {
			PdfWriter.getInstance(documento, salida);
			documento.open();

			documento.add(construirEncabezado());
			documento.add(construirTitulo());
			documento.add(construirLineaMeta(fechaDesde, fechaHasta, nombreColaborador));
			documento.add(construirTabla(solicitudes));
			documento.add(construirResumen(solicitudes));
			documento.add(construirPie());

			documento.close();
		} catch (DocumentException e) {
			throw new IllegalStateException("No se pudo generar el reporte PDF", e);
		}
		return salida.toByteArray();
	}

	private static PdfPTable construirEncabezado() throws DocumentException {
		PdfPTable encabezado = new PdfPTable(2);
		encabezado.setWidthPercentage(100);
		encabezado.setWidths(new float[] { 30f, 70f });

		PdfPCell celdaLogo = new PdfPCell();
		celdaLogo.setBorder(Rectangle.NO_BORDER);
		celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cargarLogo().ifPresent(celdaLogo::addElement);
		encabezado.addCell(celdaLogo);

		Font fuenteEmpresa = new Font(Font.HELVETICA, 13, Font.BOLD, AZUL_MARCA);
		Font fuenteSubtitulo = new Font(Font.HELVETICA, 9, Font.NORMAL, GRIS_TEXTO);
		Paragraph datosEmpresa = new Paragraph();
		datosEmpresa.setAlignment(Element.ALIGN_RIGHT);
		datosEmpresa.add(new Chunk("INNOBIX S.A.", fuenteEmpresa));
		datosEmpresa.add(Chunk.NEWLINE);
		datosEmpresa.add(new Chunk("SIAV - Sistema de Administración de Vacaciones", fuenteSubtitulo));
		datosEmpresa.add(Chunk.NEWLINE);
		datosEmpresa.add(new Chunk("Av. República del Salvador N35-82, Quito, Ecuador", fuenteSubtitulo));

		PdfPCell celdaEmpresa = new PdfPCell();
		celdaEmpresa.setBorder(Rectangle.NO_BORDER);
		celdaEmpresa.setVerticalAlignment(Element.ALIGN_MIDDLE);
		celdaEmpresa.addElement(datosEmpresa);
		encabezado.addCell(celdaEmpresa);

		return encabezado;
	}

	private static java.util.Optional<Image> cargarLogo() {
		try (InputStream entrada = new ClassPathResource(RUTA_LOGO).getInputStream()) {
			Image logo = Image.getInstance(entrada.readAllBytes());
			logo.scaleToFit(110, 45);
			return java.util.Optional.of(logo);
		} catch (IOException | RuntimeException e) {
			return java.util.Optional.empty();
		}
	}

	private static Paragraph construirTitulo() {
		Font fuenteTitulo = new Font(Font.HELVETICA, 15, Font.BOLD);
		Chunk textoTitulo = new Chunk("REPORTE DE SOLICITUDES DE VACACIONES", fuenteTitulo);
		textoTitulo.setUnderline(0.8f, -2f);

		Paragraph titulo = new Paragraph(textoTitulo);
		titulo.setAlignment(Element.ALIGN_CENTER);
		titulo.setSpacingBefore(20);
		titulo.setSpacingAfter(18);
		return titulo;
	}

	private static PdfPTable construirLineaMeta(LocalDate fechaDesde, LocalDate fechaHasta,
			String nombreColaborador) {
		Font fuenteEtiqueta = new Font(Font.HELVETICA, 9, Font.BOLD);
		Font fuenteValor = new Font(Font.HELVETICA, 9, Font.NORMAL);

		String rango = (fechaDesde == null && fechaHasta == null) ? "Todo el histórico"
				: (fechaDesde == null ? "Hasta " : fechaDesde.format(FORMATO_FECHA) + " — ")
						+ (fechaHasta == null ? "" : fechaHasta.format(FORMATO_FECHA));
		String colaborador = (nombreColaborador == null || nombreColaborador.isBlank()) ? "Todos"
				: nombreColaborador;

		PdfPTable meta = new PdfPTable(2);
		meta.setWidthPercentage(100);
		meta.setSpacingAfter(14);

		Phrase izquierda = new Phrase();
		izquierda.add(new Chunk("FECHA DE EMISIÓN: ", fuenteEtiqueta));
		izquierda.add(new Chunk(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
				fuenteValor));
		PdfPCell celdaIzquierda = new PdfPCell(izquierda);
		celdaIzquierda.setBorder(Rectangle.BOX);
		celdaIzquierda.setBorderColor(Color.LIGHT_GRAY);
		celdaIzquierda.setPadding(8);
		meta.addCell(celdaIzquierda);

		Phrase derecha = new Phrase();
		derecha.add(new Chunk("RANGO: ", fuenteEtiqueta));
		derecha.add(new Chunk(rango + "   ", fuenteValor));
		derecha.add(new Chunk("COLABORADOR: ", fuenteEtiqueta));
		derecha.add(new Chunk(colaborador, fuenteValor));
		PdfPCell celdaDerecha = new PdfPCell(derecha);
		celdaDerecha.setBorder(Rectangle.BOX);
		celdaDerecha.setBorderColor(Color.LIGHT_GRAY);
		celdaDerecha.setPadding(8);
		meta.addCell(celdaDerecha);

		return meta;
	}

	private static PdfPTable construirTabla(List<SolicitudResponseDto> solicitudes) {
		PdfPTable tabla = new PdfPTable(ENCABEZADOS.length);
		tabla.setWidthPercentage(100);
		tabla.setSpacingAfter(14);

		Font fuenteEncabezado = new Font(Font.HELVETICA, 9, Font.BOLD, Color.WHITE);
		for (String encabezado : ENCABEZADOS) {
			PdfPCell celda = new PdfPCell(new Phrase(encabezado, fuenteEncabezado));
			celda.setBackgroundColor(AZUL_MARCA);
			celda.setPadding(6);
			celda.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabla.addCell(celda);
		}

		Font fuenteCelda = new Font(Font.HELVETICA, 9);
		boolean filaClara = true;
		for (SolicitudResponseDto solicitud : solicitudes) {
			Color fondo = filaClara ? Color.WHITE : new Color(245, 247, 255);
			agregarCelda(tabla, String.valueOf(solicitud.getIdSolicitud()), fuenteCelda, fondo);
			agregarCelda(tabla, solicitud.getNombresUsuario() + " " + solicitud.getApellidosUsuario(), fuenteCelda,
					fondo);
			agregarCelda(tabla, String.valueOf(solicitud.getFechaInicio()), fuenteCelda, fondo);
			agregarCelda(tabla, String.valueOf(solicitud.getFechaFin()), fuenteCelda, fondo);
			agregarCelda(tabla,
					String.valueOf(solicitud.getDiasSolicitados() == null ? 0 : solicitud.getDiasSolicitados()),
					fuenteCelda, fondo);
			agregarCelda(tabla, solicitud.getNombreEstado(), fuenteCelda, fondo);
			agregarCelda(tabla, solicitud.getNombresAprobador() == null ? "-" : solicitud.getNombresAprobador(),
					fuenteCelda, fondo);
			filaClara = !filaClara;
		}

		if (solicitudes.isEmpty()) {
			PdfPCell vacio = new PdfPCell(
					new Phrase("No hay solicitudes que coincidan con los filtros aplicados.", fuenteCelda));
			vacio.setColspan(ENCABEZADOS.length);
			vacio.setPadding(10);
			vacio.setHorizontalAlignment(Element.ALIGN_CENTER);
			tabla.addCell(vacio);
		}

		return tabla;
	}

	private static void agregarCelda(PdfPTable tabla, String texto, Font fuente, Color fondo) {
		PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
		celda.setBackgroundColor(fondo);
		celda.setPadding(5);
		tabla.addCell(celda);
	}

	private static PdfPTable construirResumen(List<SolicitudResponseDto> solicitudes) {
		long aprobadas = solicitudes.stream().filter(s -> "Aprobada".equalsIgnoreCase(s.getNombreEstado())).count();
		long rechazadas = solicitudes.stream().filter(s -> "Rechazada".equalsIgnoreCase(s.getNombreEstado()))
				.count();

		Font fuenteEtiqueta = new Font(Font.HELVETICA, 9, Font.BOLD);
		Font fuenteValor = new Font(Font.HELVETICA, 9, Font.NORMAL);

		Phrase resumen = new Phrase();
		resumen.add(new Chunk("TOTAL: ", fuenteEtiqueta));
		resumen.add(new Chunk(solicitudes.size() + " solicitudes   ", fuenteValor));
		resumen.add(new Chunk("APROBADAS: ", fuenteEtiqueta));
		resumen.add(new Chunk(aprobadas + "   ", fuenteValor));
		resumen.add(new Chunk("RECHAZADAS: ", fuenteEtiqueta));
		resumen.add(new Chunk(String.valueOf(rechazadas), fuenteValor));

		PdfPTable contenedor = new PdfPTable(1);
		contenedor.setWidthPercentage(100);
		contenedor.setSpacingAfter(30);
		PdfPCell celda = new PdfPCell(resumen);
		celda.setBackgroundColor(new Color(245, 247, 255));
		celda.setBorderColor(Color.LIGHT_GRAY);
		celda.setPadding(10);
		contenedor.addCell(celda);
		return contenedor;
	}

	private static PdfPTable construirPie() {
		PdfPTable pie = new PdfPTable(1);
		pie.setWidthPercentage(100);

		PdfPCell linea = new PdfPCell();
		linea.setBorder(Rectangle.TOP);
		linea.setBorderColor(Color.LIGHT_GRAY);
		linea.setFixedHeight(1f);
		pie.addCell(linea);

		Font fuentePie = new Font(Font.HELVETICA, 8, Font.ITALIC, GRIS_TEXTO);
		PdfPCell texto = new PdfPCell(new Phrase("Documento generado automáticamente por SIAV.", fuentePie));
		texto.setBorder(Rectangle.NO_BORDER);
		texto.setPaddingTop(6);
		pie.addCell(texto);

		return pie;
	}

}
