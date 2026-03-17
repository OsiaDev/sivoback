package com.coljuegos.sivo.service.reporte;

import com.coljuegos.sivo.config.properties.ActaReporteProperties;
import com.coljuegos.sivo.data.dto.reporte.ActaReporteContextDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActaReporteServiceImpl implements ActaReporteService {

    private final ActaReporteProperties reporteProperties;

    @Value("${acta.imagenes.base-path:E:/archivoSiicol}")
    private String basePath;

    @Override
    public byte[] generarReporteActa(ActaReporteContextDTO context) {
        log.info("[REPORTE] Generando PDF en memoria para acta {}", context.getNumActa());

        // Ruta absoluta del .jrxml en el sistema de archivos
        // Resultado: E:/archivoSiicol/jasperReports/actaVisitaComercial.jrxml
        Path rutaJrxml = Paths.get(basePath, "jasperReports",
                reporteProperties.getNombreArchivo() + ".jrxml");

        log.debug("[REPORTE] Cargando plantilla desde: {}", rutaJrxml.toAbsolutePath());

        if (!Files.exists(rutaJrxml)) {
            log.error("[REPORTE] No se encontró la plantilla Jasper en: {}", rutaJrxml.toAbsolutePath());
            return null;
        }

        try (InputStream jrxmlStream = new FileInputStream(rutaJrxml.toFile())) {

            // Compilar el .jrxml en memoria
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            // Construir parámetros
            Map<String, Object> parametros = construirParametros(context);

            // Llenar el reporte (datos van por parámetros y sub-datasources)
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parametros, new JREmptyDataSource());

            // Exportar a PDF — API Jasper 5.2
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JRPdfExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
            exporter.exportReport();

            byte[] pdf = outputStream.toByteArray();
            log.info("[REPORTE] PDF generado en memoria para acta {}. Tamaño: {} bytes",
                    context.getNumActa(), pdf.length);
            return pdf;

        } catch (FileNotFoundException e) {
            log.error("[REPORTE] Plantilla no encontrada en disco para acta {}: {}",
                    context.getNumActa(), rutaJrxml.toAbsolutePath());
            return null;
        } catch (JRException e) {
            log.error("[REPORTE] Error JasperReports al generar acta {}: {}",
                    context.getNumActa(), e.getMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("[REPORTE] Error inesperado al generar acta {}: {}",
                    context.getNumActa(), e.getMessage(), e);
            return null;
        }
    }

    private Map<String, Object> construirParametros(ActaReporteContextDTO ctx) {
        Map<String, Object> p = new HashMap<>();

        // El parámetro "path" apunta al mismo directorio del .jrxml
        // (el legacy lo usaba para sub-reportes relativos)
        Path rutaJrxml = Paths.get(basePath, "jasperReports");
        p.put("path", rutaJrxml.toAbsolutePath());
        p.put("pathImagen", basePath + FileSystems.getDefault().getSeparator());

        p.put("fechaInventario", ctx.getFechaInventario());
        p.put("actaNumero", ctx.getActaNumero());
        p.put("autoNumero", ctx.getAutoNumero());
        p.put("numeroContrato", ctx.getNumeroContrato());
        p.put("nitOperador", ctx.getNitOperador());
        p.put("direccionOperador", ctx.getDireccionEstablecimiento());
        p.put("nombreEstablecimiento", ctx.getNombreEstablecimiento());
        p.put("nombreOperador", ctx.getNombreOperador());
        p.put("municipioOperador", ctx.getMunicipioEstablecimiento());
        p.put("departamentoOperador", ctx.getDepartamentoEstablecimiento());

        if (ctx.getFechaFinContrato() != null) {
            p.put("fechaFinContrato", ctx.getFechaFinContrato()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (ctx.getFechaHoraVisita() != null) {
            p.put("horaVisita", ctx.getFechaHoraVisita().getHour() + ":"
                    + String.format("%02d", ctx.getFechaHoraVisita().getMinute()));
            p.put("diaVisita", ctx.getFechaHoraVisita().getDayOfMonth());
            p.put("mesVisita", obtenerNombreMes(ctx.getFechaHoraVisita().getMonthValue()));
            p.put("anoVisita", ctx.getFechaHoraVisita().getYear());
        }
        p.put("nombrePresente", ctx.getNombrePresente());
        p.put("identificacionPresente", ctx.getIdentificacionPresente());
        p.put("municipioPresente", ctx.getMunicipioPresente());
        p.put("cargoPresente", ctx.getCargoPresente());

        if (ctx.getFechaAuto() != null) {
            p.put("fechaAuto", ctx.getFechaAuto()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        p.put("avisoAutorizacion", ctx.getAvisoAutorizacion());
        p.put("direccionCorresponde", ctx.getDireccionCorresponde());
        p.put("direccionCorrecta", ctx.getOtraDireccion());
        p.put("nombreCorresponde", ctx.getNombreEstCorresponde());
        p.put("nombreCorrecto", ctx.getOtroNombre());
        p.put("actividadDiferente", ctx.getActividadesDiferentes());

        String actividad = ctx.getTipoActividad();
        if ("Otros".equalsIgnoreCase(actividad)) {
            actividad = ctx.getEspecificacionOtros();
        }
        p.put("actividad", actividad);
        p.put("cuentaRegistros", ctx.getRegistrosMantenimiento());

        p.put("formatoIdentificacion", ctx.getFormatoIdentificacion());
        p.put("monto", ctx.getMontoIdentificacion());
        p.put("formatoReporteInterno", ctx.getFormatoReporteInterno());
        p.put("senalesAlerta", ctx.getSenalesAlerta());
        p.put("codigoConducta", ctx.getConoceCodigoConducta());

        p.put("nombreFiscalizador", ctx.getNombreFiscalizador());
        p.put("ccFiscalizador", ctx.getCcFiscalizador());
        p.put("cargoFiscalizador", ctx.getCargoFiscalizador());
        p.put("nombreAcompanante", ctx.getNombreAcompanante());
        p.put("ccAcompanante", ctx.getCcAcompanante());
        p.put("cargoAcompanante", ctx.getCargoAcompanante());
        p.put("nombreFirmaOperador", ctx.getNombreFirmaOperador());
        p.put("ccFirmaOperador", ctx.getCcFirmaOperador());
        p.put("rolFirmaOperador", ctx.getRolFirmaOperador());
        p.put("firmaFiscalizador", ctx.getFirmaFiscalizadorPath());
        p.put("firmaAcompanante", ctx.getFirmaAcompanantePath());
        p.put("firmaOperador", ctx.getFirmaOperadorPath());

        p.put("listaInventario", new JRBeanCollectionDataSource(
                ctx.getListaInventarios() != null ? ctx.getListaInventarios() : Collections.emptyList()));
        p.put("listaNovedad", new JRBeanCollectionDataSource(
                ctx.getListaNovedades() != null ? ctx.getListaNovedades() : Collections.emptyList()));

        return p;
    }

    private String obtenerNombreMes(int numeroMes) {
        return Month.of(numeroMes).getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
    }

}
