package com.coljuegos.sivo.service.notificacion;

import com.coljuegos.sivo.data.dto.reporte.ActaReporteContextDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.*;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

import lombok.RequiredArgsConstructor;
import com.coljuegos.sivo.data.repository.InventarioRepository;
import com.coljuegos.sivo.data.dto.InventarioProjection;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class ActaReporteContextMapper {

    private final InventarioRepository inventarioRepository;

    private enum ApuestaCodigoEnum {
        MET(new String[]{"1", "2", "3"}),
        MESAS(new String[]{"4"}),
        BINGO(new String[]{"6", "7", "8", "9", "10", "11", "12", "13"}),
        OTROS(new String[]{"5", "14"});

        private final String[] codigos;

        ApuestaCodigoEnum(String[] codigos) {
            this.codigos = codigos;
        }

        public static ApuestaCodigoEnum fromCodigo(String codigo) {
            if (codigo == null) return MET;
            for (ApuestaCodigoEnum e : values()) {
                for (String c : e.codigos) {
                    if (c.equals(codigo)) return e;
                }
            }
            return MET;
        }
    }

    private static class Counters {
        int registrados = 0;
        int apagados = 0;
        int noEncontrados = 0;
        int sinPlaca = 0;
        int serialDiferente = 0;
        int codigoApuestaDiferente = 0;
        int novedadesApagadas = 0;
        int novedadesOperando = 0;
    }

    private Counters getCounter(ApuestaCodigoEnum tipo, Counters met, Counters mesas, Counters bingo, Counters otros) {
        switch (tipo) {
            case MESAS: return mesas;
            case BINGO: return bingo;
            case OTROS: return otros;
            case MET:
            default: return met;
        }
    }

    public ActaReporteContextDTO mapear(
            SiiAutoComisorioEntity auto,
            SiiActaVisitaEntity acta,
            SiiVerificacionContractualEntity contractual,
            SiiVerificacionSiplaftEntity siplaft,
            SiiVerificacionJuegoResponsableEntity juegoResp,
            SiiFirmaActaEntity firma,
            SiiResumenInventarioEntity resumen,
            List<SiiInventarioRegistradoEntity> inventarios,
            List<SiiNovedadRegistradaEntity> novedades,
            SiiVerificacionBingoEntity verificacionBingo,
            List<SiiInventarioBingoRegistradoEntity> inventariosBingo) {

        Counters met = new Counters();
        Counters mesas = new Counters();
        Counters otros = new Counters();
        Counters bingo = new Counters();

        if (inventarios != null) {
            for (SiiInventarioRegistradoEntity inv : inventarios) {
                String codigo = (inv.getInrCodApuestaDiferenteValor() != null && !inv.getInrCodApuestaDiferenteValor().trim().isEmpty())
                        ? inv.getInrCodApuestaDiferenteValor() : inv.getInrCodigoApuesta();
                ApuestaCodigoEnum tipo = ApuestaCodigoEnum.fromCodigo(codigo);
                Counters c = getCounter(tipo, met, mesas, bingo, otros);

                c.registrados++;
                if ("APAGADO".equalsIgnoreCase(inv.getInrEstado())) c.apagados++;
                if ("NO_ENCONTRADO".equalsIgnoreCase(inv.getInrEstado())) c.noEncontrados++;
                if (inv.getInrSerialDiferente() != null && !inv.getInrSerialDiferente().trim().isEmpty()) c.serialDiferente++;
                if (Integer.valueOf(1).equals(inv.getInrCodApuestaDiferente())) c.codigoApuestaDiferente++;
            }
        }

        if (novedades != null) {
            for (SiiNovedadRegistradaEntity nov : novedades) {
                String codigo = nov.getNorCodigoApuesta();
                ApuestaCodigoEnum tipo = ApuestaCodigoEnum.fromCodigo(codigo);
                Counters c = getCounter(tipo, met, mesas, bingo, otros);

                if (Integer.valueOf(0).equals(nov.getNorTienePlaca())) c.sinPlaca++;
                if ("Apagado".equalsIgnoreCase(nov.getNorOperando())) c.novedadesApagadas++;
                if ("Operando".equalsIgnoreCase(nov.getNorOperando())) c.novedadesOperando++;
            }
        }

        if (inventariosBingo != null) {
            for (SiiInventarioBingoRegistradoEntity invB : inventariosBingo) {
                bingo.registrados++;
                if ("APAGADO".equalsIgnoreCase(invB.getIbrEstado())) bingo.apagados++;
                if ("NO_ENCONTRADO".equalsIgnoreCase(invB.getIbrEstado())) bingo.noEncontrados++;
                if (Integer.valueOf(1).equals(invB.getIbrCodApuestaDiferente())) bingo.codigoApuestaDiferente++;
            }
        }

        // 3. Sumar Novedades Operando a Registrados de su grupo respectivo.
        met.registrados += met.novedadesOperando;
        mesas.registrados += mesas.novedadesOperando;
        bingo.registrados += bingo.novedadesOperando;
        otros.registrados += otros.novedadesOperando;

        // 4. Calculo Global Excedente
        int totalInventarioOriginal = 0;
        Long conCodigo = auto.getSiiContrato() != null ? auto.getSiiContrato().getConCodigo() : null;
        Integer aucNumero = auto.getAucNumero();
        Long estCodigo = auto.getSiiEstablecimiento() != null ? auto.getSiiEstablecimiento().getEstCodigo() : null;

        if (conCodigo != null && estCodigo != null && aucNumero != null) {
            Collection<InventarioProjection> inventariosRaw = inventarioRepository.buscarInventariosPorFiltros(
                    Collections.singletonList(conCodigo),
                    Collections.singletonList(aucNumero),
                    Collections.singletonList(estCodigo)
            );
            totalInventarioOriginal = inventariosRaw.size();
        }

        int sumaNovedadesYRegistrados = met.registrados + mesas.registrados + bingo.registrados + otros.registrados;
        int excedente = sumaNovedadesYRegistrados - totalInventarioOriginal;
        long novedadesOperandoFinal = excedente > 0 ? excedente : 0;

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

                // Bingo
                .bingoCartonesModulos(verificacionBingo != null ? verificacionBingo.getVbiCartonesModulos() : null)
                .bingoSistemaTecnologico(verificacionBingo != null ? verificacionBingo.getVbiSistemaTecnologico() : null)
                .bingoSistemaInterconectado(verificacionBingo != null ? verificacionBingo.getVbiSistemaInterconectado() : null)
                .bingoEventosEspeciales(verificacionBingo != null ? verificacionBingo.getVbiEventosEspeciales() : null)
                .bingoTipoBalotera(verificacionBingo != null ? verificacionBingo.getVbiTipoBalotera() : null)
                .bingoValorCarton(verificacionBingo != null ? verificacionBingo.getVbiValorCarton() : null)

                .listaInventarios(inventarios)
                .listaNovedades(novedades)
                .listaInventariosBingo(inventariosBingo)
                
                // Contadores MET
                .registrados(met.registrados)
                .numeroInventariosApagados(met.apagados)
                .numeroInventariosNoEncontrados(met.noEncontrados)
                .numeroNovedadesSinPlaca(met.sinPlaca)
                .numeroMaquinasSerialDiferente(met.serialDiferente)
                .numeroCodigoApuestaDiferente(met.codigoApuestaDiferente)
                .numeroNovedadesApagadas(met.novedadesApagadas)
                .numeroNovedadesOperando((int) novedadesOperandoFinal)
                
                // Contadores Mesas
                .registradosMesas(mesas.registrados)
                .numeroInventariosApagadosMesas(mesas.apagados)
                .numeroInventariosNoEncontradosMesas(mesas.noEncontrados)
                .numeroNovedadesSinPlacaMesas(mesas.sinPlaca)
                .numeroMaquinasSerialDiferenteMesas(mesas.serialDiferente)
                .numeroCodigoApuestaDiferenteMesas(mesas.codigoApuestaDiferente)
                .numeroNovedadesApagadasMesas(mesas.novedadesApagadas)
                .numeroNovedadesOperandoMesas(mesas.novedadesOperando)
                
                // Contadores Bingos
                .registradosBingos(bingo.registrados)
                .numeroInventariosApagadosBingos(bingo.apagados)
                .numeroInventariosNoEncontradosBingos(bingo.noEncontrados)
                .numeroNovedadesSinPlacaBingos(bingo.sinPlaca)
                .numeroMaquinasSerialDiferenteBingos(bingo.serialDiferente)
                .numeroCodigoApuestaDiferenteBingos(bingo.codigoApuestaDiferente)
                .numeroNovedadesApagadasBingos(bingo.novedadesApagadas)
                .numeroNovedadesOperandoBingos(bingo.novedadesOperando)
                
                // Contadores Otros
                .registradosOtros(otros.registrados)
                .numeroInventariosApagadosOtros(otros.apagados)
                .numeroInventariosNoEncontradosOtros(otros.noEncontrados)
                .numeroNovedadesSinPlacaOtros(otros.sinPlaca)
                .numeroMaquinasSerialDiferenteOtros(otros.serialDiferente)
                .numeroCodigoApuestaDiferenteOtros(otros.codigoApuestaDiferente)
                .numeroNovedadesApagadasOtros(otros.novedadesApagadas)
                .numeroNovedadesOperandoOtros(otros.novedadesOperando)
                
                .build();
    }
}
