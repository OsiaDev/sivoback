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

    // Verificación Bingo
    private String bingoCartonesModulos;
    private String bingoSistemaTecnologico;
    private String bingoSistemaInterconectado;
    private String bingoEventosEspeciales;
    private String bingoTipoBalotera;
    private String bingoValorCarton;

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
    private List<?> listaInventariosBingo;

    // Contadores de inventario
    private Integer registrados;
    private Integer numeroInventariosApagados;
    private Integer numeroInventariosNoEncontrados;
    private Integer numeroNovedadesSinPlaca;
    private Integer numeroMaquinasSerialDiferente;
    private Integer numeroCodigoApuestaDiferente;
    private Integer numeroNovedadesApagadas;
    private Integer numeroNovedadesOperando;

    // Contadores de inventario Bingos
    private Integer registradosBingos;
    private Integer numeroInventariosApagadosBingos;
    private Integer numeroInventariosNoEncontradosBingos;
    private Integer numeroNovedadesSinPlacaBingos;
    private Integer numeroMaquinasSerialDiferenteBingos;
    private Integer numeroCodigoApuestaDiferenteBingos;
    private Integer numeroNovedadesApagadasBingos;
    private Integer numeroNovedadesOperandoBingos;

    // Contadores de inventario Mesas
    private Integer registradosMesas;
    private Integer numeroInventariosApagadosMesas;
    private Integer numeroInventariosNoEncontradosMesas;
    private Integer numeroNovedadesSinPlacaMesas;
    private Integer numeroMaquinasSerialDiferenteMesas;
    private Integer numeroCodigoApuestaDiferenteMesas;
    private Integer numeroNovedadesApagadasMesas;
    private Integer numeroNovedadesOperandoMesas;

    // Contadores de inventario Otros
    private Integer registradosOtros;
    private Integer numeroInventariosApagadosOtros;
    private Integer numeroInventariosNoEncontradosOtros;
    private Integer numeroNovedadesSinPlacaOtros;
    private Integer numeroMaquinasSerialDiferenteOtros;
    private Integer numeroCodigoApuestaDiferenteOtros;
    private Integer numeroNovedadesApagadasOtros;
    private Integer numeroNovedadesOperandoOtros;

}