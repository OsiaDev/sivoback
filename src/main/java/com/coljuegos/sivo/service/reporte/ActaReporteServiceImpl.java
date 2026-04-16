package com.coljuegos.sivo.service.reporte;

import com.coljuegos.sivo.config.properties.ActaReporteProperties;
import com.coljuegos.sivo.data.dto.reporte.ActaReporteContextDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
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

        Path rutaJrxml = Paths.get(basePath, "jasperReports",
                reporteProperties.getNombreArchivo() + ".jasper");

        log.debug("[REPORTE] Cargando plantilla desde: {}", rutaJrxml.toAbsolutePath());

        if (!Files.exists(rutaJrxml)) {
            log.error("[REPORTE] No se encontró la plantilla Jasper en: {}", rutaJrxml.toAbsolutePath());
            return null;
        }

        try (InputStream jrxmlStream = new FileInputStream(rutaJrxml.toFile())) {

            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jrxmlStream);

            Map<String, Object> parametros = construirParametros(context);

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

    /**
     * Construye el mapa de parámetros Jasper con exactamente los mismos nombres de clave
     * que usa {@code ResultadoVisitaMB.imprimir()} en el sistema legacy.
     */
    private Map<String, Object> construirParametros(ActaReporteContextDTO ctx) {
        Map<String, Object> p = new HashMap<>();

        // Rutas de recursos — igual que el legacy
        Path dirJasper = Paths.get(basePath, "jasperReports");
        p.put("path", dirJasper.toAbsolutePath());
        p.put("pathImagen", basePath + FileSystems.getDefault().getSeparator());

        Path rutaImagen1 = Paths.get(basePath, "jasperReports", "imagenes",
                reporteProperties.getImagenUno() + ".png");
        Path rutaImagen2 = Paths.get(basePath, "jasperReports", "imagenes",
                reporteProperties.getImagenDos() + ".png");
        Path rutaCheck = Paths.get(basePath, "jasperReports", "imagenes",
                reporteProperties.getCheck() + ".png");
        Path rutaNoCheck = Paths.get(basePath, "jasperReports", "imagenes",
                reporteProperties.getNoCheck() + ".png");

        Path rutaInventario = Paths.get(basePath, "jasperReports",
                reporteProperties.getInventario() + ".jasper");
        Path rutaNovedad = Paths.get(basePath, "jasperReports",
                reporteProperties.getNovedad() + ".jasper");

        p.put("pathInventario", rutaInventario.toAbsolutePath());
        p.put("pathNovedad", rutaNovedad.toAbsolutePath());

        p.put("pathImagenJasper1", rutaImagen1.toAbsolutePath());
        p.put("pathImagenJasper2", rutaImagen2.toAbsolutePath());

        p.put("pathCheck", rutaCheck.toAbsolutePath());
        p.put("pathNoCheck", rutaNoCheck.toAbsolutePath());

        // ── Datos generales del acta ────────────────────────────────────────────
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

        // ── Datos de la visita ──────────────────────────────────────────────────
        // Legacy descompone la fecha en hora, día, mes y año por separado
        if (ctx.getFechaHoraVisita() != null) {
            int hora = ctx.getFechaHoraVisita().getHour();
            int min = ctx.getFechaHoraVisita().getMinute();
            p.put("horaVisita", hora + ":" + String.format("%02d", min));
            p.put("diaVisita", ctx.getFechaHoraVisita().getDayOfMonth());
            p.put("mesVisita", obtenerNombreMes(ctx.getFechaHoraVisita().getMonthValue()));
            p.put("anoVisita", ctx.getFechaHoraVisita().getYear());
        }
        p.put("nombrePresente", ctx.getNombrePresente());
        p.put("identificacionPresente", ctx.getIdentificacionPresente());
        p.put("municipioPresente", ctx.getMunicipioPresente());
        p.put("cargoPresente", ctx.getCargoPresente());

        // ── Fecha del auto comisorio ────────────────────────────────────────────
        if (ctx.getFechaAuto() != null) {
            p.put("fechaAuto", ctx.getFechaAuto()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        if (ctx.getFechaFinVisita() != null) {
            p.put("fechaFinVisita", ctx.getFechaFinVisita()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        p.put("latitud", ctx.getLatitud());
        p.put("longitud", ctx.getLongitud());

        p.put("observacionColjuegos", ctx.getObservacionColjuegos());
        p.put("observacionOperador", ctx.getObservacionOperador());

        // Responsables
        p.put("cuentaProgramaJuegoResp", ctx.getCuentaProgramaJuegoResp());
        p.put("cuentaTestIdentRiesgos", ctx.getCuentaTestIdentRiesgos());
        p.put("existenPiezasPublicitarias", ctx.getExistenPiezasPublicitarias());

        // ── Verificación contractual ────────────────────────────────────────────
        p.put("avisoAutorizacion", ctx.getAvisoAutorizacion());
        p.put("direccionCorresponde", ctx.getDireccionCorresponde());
        p.put("direccionCorrecta", ctx.getOtraDireccion());          // "Otra dirección"
        p.put("nombreCorresponde", ctx.getNombreEstCorresponde());
        p.put("nombreCorrecto", ctx.getOtroNombre());                // "Otro nombre"
        p.put("actividadDiferente", ctx.getActividadesDiferentes());

        // Legacy: si tipoActividad == "Otros" se muestra especificacionOtros
        String actividad = ctx.getTipoActividad();
        if ("Otros".equalsIgnoreCase(actividad)) {
            actividad = ctx.getEspecificacionOtros();
        }
        p.put("actividad", actividad);
        p.put("cuentaRegistros", ctx.getRegistrosMantenimiento());

        // ── Verificación SIPLAFT ────────────────────────────────────────────────
        p.put("formatoIdentificacion", ctx.getFormatoIdentificacion());
        p.put("monto", ctx.getMontoIdentificacion());
        p.put("formatoReporteInterno", ctx.getFormatoReporteInterno());
        p.put("senalesAlerta", ctx.getSenalesAlerta());
        p.put("codigoConducta", ctx.getConoceCodigoConducta());

        // ── Firmas ──────────────────────────────────────────────────────────────
        // Nombres de parámetros Jasper idénticos al legacy ResultadoVisitaMB.imprimir()
        p.put("nombreFiscalizador", ctx.getNombreFiscalizador());
        p.put("ccFiscalizador", ctx.getCcFiscalizador());
        p.put("cargoFiscalizador", ctx.getCargoFiscalizador());
        p.put("firmaFiscalizador", ctx.getFirmaFiscalizadorPath());        // path en disco

        p.put("nombreAcompanante", ctx.getNombreAcompanante());
        p.put("ccAcompanante", ctx.getCcAcompanante());
        p.put("cargoAcompanante", ctx.getCargoAcompanante());
        p.put("firmaAcompanante", ctx.getFirmaAcompanantePath());          // path en disco

        p.put("nombreFirmaOperador", ctx.getNombreFirmaOperador());
        p.put("ccFirmaOperador", ctx.getCcFirmaOperador());
        p.put("rolFirmaOperador", ctx.getRolFirmaOperador());
        p.put("firmaOperador", ctx.getFirmaOperadorPath());                // path en disco

        // ── Sub-reportes ────────────────────────────────────────────────────────
        p.put("listaInventario", new JRBeanCollectionDataSource(
                ctx.getListaInventarios() != null ? ctx.getListaInventarios() : Collections.emptyList()));
        p.put("listaNovedad", new JRBeanCollectionDataSource(
                ctx.getListaNovedades() != null ? ctx.getListaNovedades() : Collections.emptyList()));

        // ── Contadores de Inventario ────────────────────────────────────────────
        p.put("numeroInventariosRegistrados", ctx.getRegistrados());
        p.put("numeroInventariosApagados", ctx.getNumeroInventariosApagados());
        p.put("numeroInventariosNoEncontrados", ctx.getNumeroInventariosNoEncontrados());
        p.put("numeroNovedadesSinPlaca", ctx.getNumeroNovedadesSinPlaca());
        p.put("numeroMaquinasSerialDiferente", ctx.getNumeroMaquinasSerialDiferente());
        p.put("numeroCodigoApuestaDiferente", ctx.getNumeroCodigoApuestaDiferente());
        p.put("numeroNovedadesApagadas", ctx.getNumeroNovedadesApagadas());
        p.put("numeroNovedadesOperando", ctx.getNumeroNovedadesOperando());
        Integer totalInventarioEncontrado =
                nvl(ctx.getRegistrados())
                        - nvl(ctx.getNumeroInventariosNoEncontrados())
                        + nvl(ctx.getNumeroNovedadesSinPlaca()) + nvl(ctx.getNumeroNovedadesOperando());
        p.put("totalInventarioEncontrado", totalInventarioEncontrado);

        return p;
    }

    private int nvl(Integer value) {
        return value == null ? 0 : value;
    }

    private String obtenerNombreMes(int numeroMes) {
        return Month.of(numeroMes).getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
    }

}