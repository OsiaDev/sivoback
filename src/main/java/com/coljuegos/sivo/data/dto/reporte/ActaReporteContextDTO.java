package com.coljuegos.sivo.data.dto.reporte;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ActaReporteContextDTO {

    // Datos del acta
    private Integer numActa;
    private Long aucCodigo;
    private String actaNumero;
    private String autoNumero;
    private String fechaInventario;

    // Datos del operador / contrato
    private String nombreOperador;
    private String nitOperador;
    private String numeroContrato;
    private LocalDate fechaFinContrato;

    // Datos del establecimiento
    private String nombreEstablecimiento;
    private String direccionEstablecimiento;
    private String municipioEstablecimiento;
    private String departamentoEstablecimiento;

    // Datos de la visita (ActaVisita)
    private LocalDateTime fechaHoraVisita;
    private String nombrePresente;
    private String identificacionPresente;
    private String municipioPresente;
    private String cargoPresente;

    // Fecha del auto comisorio
    private LocalDate fechaAuto;

    // Verificación contractual
    private String avisoAutorizacion;
    private String direccionCorresponde;
    private String otraDireccion;
    private String nombreEstCorresponde;
    private String otroNombre;
    private String actividadesDiferentes;
    private String tipoActividad;
    private String especificacionOtros;
    private String registrosMantenimiento;

    // Verificación SIPLAFT
    private String formatoIdentificacion;
    private String montoIdentificacion;
    private String formatoReporteInterno;
    private String senalesAlerta;
    private String conoceCodigoConducta;

    // Juegos Responsables
    private String cuentaProgramaJuegoResp;
    private String cuentaTestIdentRiesgos;
    private String existenPiezasPublicitarias;

    // Observaciones
    private String observacionOperador;
    private String observacionColjuegos;

    private LocalDateTime fechaFinVisita;

    // Locacion
    private String latitud;
    private String longitud;

    // Firma del acta
    private String nombreFiscalizador;
    private String ccFiscalizador;
    private String cargoFiscalizador;
    private String nombreAcompanante;
    private String ccAcompanante;
    private String cargoAcompanante;
    private String nombreFirmaOperador;
    private String ccFirmaOperador;
    private String rolFirmaOperador;
    private String firmaFiscalizadorPath;
    private String firmaAcompanantePath;
    private String firmaOperadorPath;

    // Listas para sub-reportes (JRBeanCollectionDataSource)
    private List<?> listaInventarios;
    private List<?> listaNovedades;

    // Contadores de inventario
    private Integer registrados;
    private Integer numeroInventariosApagados;
    private Integer numeroInventariosNoEncontrados;
    private Integer numeroNovedadesSinPlaca;
    private Integer numeroMaquinasSerialDiferente;
    private Integer numeroCodigoApuestaDiferente;
    private Integer numeroNovedadesApagadas;
    private Integer numeroNovedadesOperando;

}