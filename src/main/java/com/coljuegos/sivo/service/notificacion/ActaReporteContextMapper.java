package com.coljuegos.sivo.service.notificacion;

import com.coljuegos.sivo.data.dto.reporte.ActaReporteContextDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ActaReporteContextMapper {

    public ActaReporteContextDTO mapear(
            SiiAutoComisorioEntity auto,
            SiiActaVisitaEntity acta,
            SiiVerificacionContractualEntity contractual,
            SiiVerificacionSiplaftEntity siplaft,
            SiiVerificacionJuegoResponsableEntity juegoResp,
            SiiFirmaActaEntity firma,
            SiiResumenInventarioEntity resumen,
            List<SiiInventarioRegistradoEntity> inventarios,
            List<SiiNovedadRegistradaEntity> novedades) {

        long apagados = inventarios == null ? 0 : inventarios.stream()
                .filter(inv -> "APAGADO".equalsIgnoreCase(inv.getInrEstado()))
                .count();

        long noEncontrados = inventarios == null ? 0 : inventarios.stream()
                .filter(inv -> "NO_ENCONTRADO".equalsIgnoreCase(inv.getInrEstado()))
                .count();
                
        long serialDiferente = inventarios == null ? 0 : inventarios.stream()
                .filter(inv -> inv.getInrSerialDiferente() != null && !inv.getInrSerialDiferente().trim().isEmpty())
                .count();

        long sinPlaca = novedades == null ? 0 : novedades.stream()
                .filter(nov -> Integer.valueOf(0).equals(nov.getNorTienePlaca()))
                .count();
                
        long codigoApuestaDiferente = inventarios == null ? 0 : inventarios.stream()
                .filter(inv -> Integer.valueOf(1).equals(inv.getInrCodApuestaDiferente()))
                .count();

        long novedadesApagadas = novedades == null ? 0 : novedades.stream()
                .filter(nov -> "Apagado".equalsIgnoreCase(nov.getNorOperando()))
                .count();

        long novedadesOperando = novedades == null ? 0 : novedades.stream()
                .filter(nov -> "Operando".equalsIgnoreCase(nov.getNorOperando()))
                .count();

        Integer registrados = inventarios == null ? 0 : inventarios.size();

        return ActaReporteContextDTO.builder()
                .numActa(auto.getAucNumero())
                .aucCodigo(auto.getAucCodigo())
                .actaNumero("AH" + auto.getAucNumero())
                .autoNumero("AC" + auto.getAucNumero())
                .fechaInventario(auto.getAucFechaVisitaRealizada() != null ? 
                        auto.getAucFechaVisitaRealizada().toLocalDate().toString() : "")
                
                // Operador / Contrato
                .nombreOperador(auto.getSiiContrato() != null && auto.getSiiContrato().getSiiOperadorEntity() != null ? 
                        auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona().getPerJurNombreLargo() : null)
                .nitOperador(auto.getSiiContrato() != null && auto.getSiiContrato().getSiiOperadorEntity() != null ? 
                        auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona().getPerNumIdentificacion() : null)
                .numeroContrato(auto.getSiiContrato() != null ? auto.getSiiContrato().getConNumero() : null)
                .fechaFinContrato(auto.getSiiContrato() != null ? 
                        auto.getSiiContrato().getConFechaFinDefin() : null)

                // Establecimiento
                .nombreEstablecimiento(auto.getSiiEstablecimiento() != null ? auto.getSiiEstablecimiento().getEstNombre() : null)
                .direccionEstablecimiento(auto.getSiiEstablecimiento() != null ? auto.getSiiEstablecimiento().getEstDireccion() : null)
                .municipioEstablecimiento(auto.getSiiEstablecimiento() != null && auto.getSiiEstablecimiento().getSiiUbicacion() != null ? 
                        auto.getSiiEstablecimiento().getSiiUbicacion().getUbiNombre() : null)
                .departamentoEstablecimiento(auto.getSiiEstablecimiento() != null && auto.getSiiEstablecimiento().getSiiUbicacion() != null && auto.getSiiEstablecimiento().getSiiUbicacion().getSiiUbicacionPadre() != null ? 
                        auto.getSiiEstablecimiento().getSiiUbicacion().getSiiUbicacionPadre().getUbiNombre() : null)

                // Visita
                .fechaHoraVisita(acta != null ? acta.getAviFechaRegistro() : null)
                .nombrePresente(acta != null ? acta.getAviNombrePresente() : null)
                .identificacionPresente(acta != null ? acta.getAviIdentificacionPresente() : null)
                .municipioPresente(acta != null ? acta.getAviMunicipio() : null)
                .cargoPresente(acta != null ? acta.getAviCargoPresente() : null)
                
                // Fecha Auto
                .fechaAuto(auto.getAucFechaVisita() != null ? auto.getAucFechaVisita() : null)

                // Contractual
                .avisoAutorizacion(contractual != null ? contractual.getVcoAvisoAutorizacion() : null)
                .direccionCorresponde(contractual != null ? contractual.getVcoDireccionCorresponde() : null)
                .otraDireccion(contractual != null ? contractual.getVcoOtraDireccion() : null)
                .nombreEstCorresponde(contractual != null ? contractual.getVcoNombreEstCorresponde() : null)
                .otroNombre(contractual != null ? contractual.getVcoOtroNombre() : null)
                .actividadesDiferentes(contractual != null ? contractual.getVcoActividadesDiferentes() : null)
                .tipoActividad(contractual != null ? contractual.getVcoTipoActividad() : null)
                .especificacionOtros(contractual != null ? contractual.getVcoEspecificacionOtros() : null)
                .registrosMantenimiento(contractual != null ? (contractual.getVcoRegistrosMantenimiento() != null ? contractual.getVcoRegistrosMantenimiento().toString() : null) : null)

                // SIPLAFT
                .formatoIdentificacion(siplaft != null ? siplaft.getVsiFormatoIdentificacion() : null)
                .montoIdentificacion(siplaft != null ? (siplaft.getVsiMontoIdentificacion() != null ? siplaft.getVsiMontoIdentificacion().toString() : null) : null)
                .formatoReporteInterno(siplaft != null ? siplaft.getVsiFormatoReporteInterno() : null)
                .senalesAlerta(siplaft != null ? siplaft.getVsiSenalesAlerta() : null)
                .conoceCodigoConducta(siplaft != null ? siplaft.getVsiConoceCodigoConducta() : null)

                // Responsable
                .cuentaProgramaJuegoResp(juegoResp != null ? juegoResp.getVjrCuentaProgramaJuegoResp() : null)
                .cuentaTestIdentRiesgos(juegoResp != null ? juegoResp.getVjrCuentaTestIdentRiesgos() : null)
                .existenPiezasPublicitarias(juegoResp != null ? juegoResp.getVjrExistenPiezasPublicitarias() : null)

                // Responsable
                .observacionColjuegos(resumen != null ? resumen.getRsiNotasResumen() : null)
                .observacionOperador(resumen != null ? resumen.getRsiObservacionesOperador() : null)

                // Locacion
                .latitud(resumen != null ? (resumen.getRsiLatitud() != null ? resumen.getRsiLatitud().toString() : null) : null)
                .longitud(resumen != null ? (resumen.getRsiLongitud() != null ? resumen.getRsiLongitud().toString() : null) : null)

                .fechaFinVisita(acta != null ? acta.getAviFechaRegistro() : null)

                // Firmas
                .nombreFiscalizador(firma != null ? firma.getFiaNombreFiscPrincipal() : null)
                .ccFiscalizador(firma != null ? (firma.getFiaCcFiscPrincipal() != null ? firma.getFiaCcFiscPrincipal().toString() : null) : null)
                .cargoFiscalizador(firma != null ? firma.getFiaCargoFiscPrincipal() : null)
                .firmaFiscalizadorPath(firma != null ? firma.getFiaPathFirmaFiscPrincipal() : null)
                
                .nombreAcompanante(firma != null ? firma.getFiaNombreFiscSecundario() : null)
                .ccAcompanante(firma != null ? (firma.getFiaCcFiscSecundario() != null ? firma.getFiaCcFiscSecundario().toString() : null) : null)
                .cargoAcompanante(firma != null ? firma.getFiaCargoFiscSecundario() : null)
                .firmaAcompanantePath(firma != null ? firma.getFiaPathFirmaFiscSecundario() : null)

                .nombreFirmaOperador(firma != null ? firma.getFiaNombreOperador() : null)
                .ccFirmaOperador(firma != null ? (firma.getFiaCcOperador() != null ? firma.getFiaCcOperador().toString() : null) : null)
                .rolFirmaOperador(firma != null ? firma.getFiaCargoOperador() : null)
                .firmaOperadorPath(firma != null ? firma.getFiaPathFirmaOperador() : null)

                .listaInventarios(inventarios)
                .listaNovedades(novedades)
                .registrados(registrados)
                .numeroInventariosApagados((int) apagados)
                .numeroInventariosNoEncontrados((int) noEncontrados)
                .numeroNovedadesSinPlaca((int) sinPlaca)
                .numeroMaquinasSerialDiferente((int) serialDiferente)
                .numeroCodigoApuestaDiferente((int) codigoApuestaDiferente)
                .numeroNovedadesApagadas((int) novedadesApagadas)
                .numeroNovedadesOperando((int) novedadesOperando)
                .build();
    }
}
