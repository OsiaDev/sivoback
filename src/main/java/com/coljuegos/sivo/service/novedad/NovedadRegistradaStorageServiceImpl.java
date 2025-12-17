package com.coljuegos.sivo.service.novedad;

import com.coljuegos.sivo.data.dto.acta.NovedadRegistradaDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiNovedadRegistradaEntity;
import com.coljuegos.sivo.data.repository.NovedadRegistradaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class NovedadRegistradaStorageServiceImpl implements NovedadRegistradaStorageService {

    private final NovedadRegistradaRepository novedadRegistradaRepository;

    @Override
    @Transactional
    public List<SiiNovedadRegistradaEntity> guardarNovedades(
            Collection<NovedadRegistradaDTO> novedades,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa) {

        if (novedades == null || novedades.isEmpty()) {
            log.debug("No hay novedades para guardar en acta {}", numActa);
            return new ArrayList<>();
        }

        log.info("Guardando {} novedades para acta {}", novedades.size(), numActa);

        List<SiiNovedadRegistradaEntity> novedadesGuardadas = new ArrayList<>();
        int contador = 0;

        for (NovedadRegistradaDTO novedadDTO : novedades) {
            try {
                contador++;
                log.debug("Procesando novedad {}/{} para acta {}",
                        contador, novedades.size(), numActa);

                SiiNovedadRegistradaEntity novedadGuardada = guardarNovedad(
                        novedadDTO,
                        autoComisorio,
                        numActa);

                novedadesGuardadas.add(novedadGuardada);

            } catch (Exception e) {
                log.error("Error al guardar novedad {}/{} (serial: {}) del acta {}: {}",
                        contador, novedades.size(), novedadDTO.getSerial(),
                        numActa, e.getMessage(), e);
                // Continuar con las demás novedades
            }
        }

        log.info("Se guardaron exitosamente {}/{} novedades para acta {}",
                novedadesGuardadas.size(), novedades.size(), numActa);

        return novedadesGuardadas;
    }

    @Override
    @Transactional
    public SiiNovedadRegistradaEntity guardarNovedad(
            NovedadRegistradaDTO novedadDTO,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa) {

        if (novedadDTO == null) {
            throw new IllegalArgumentException("NovedadRegistradaDTO no puede ser null");
        }

        if (novedadDTO.getSerial() == null || novedadDTO.getSerial().trim().isEmpty()) {
            throw new IllegalArgumentException("Serial de la novedad no puede estar vacío");
        }

        log.debug("Procesando novedad con serial: {}", novedadDTO.getSerial());

        SiiNovedadRegistradaEntity novedadEntity = new SiiNovedadRegistradaEntity();

        // Mapear campos obligatorios
        novedadEntity.setSiiAutoComisorio(autoComisorio);
        novedadEntity.setNorNumActa(numActa);
        novedadEntity.setNorSerial(novedadDTO.getSerial());

        // Mapear campos opcionales
        novedadEntity.setNorMarca(novedadDTO.getMarca());
        novedadEntity.setNorCodigoApuesta(novedadDTO.getCodigoApuesta());

        // Convertir Boolean a Integer (Oracle: 1=true, 0=false, null=no especificado)
        if (novedadDTO.getTienePlaca() != null) {
            novedadEntity.setNorTienePlaca(novedadDTO.getTienePlaca() ? 1 : 0);
        }

        novedadEntity.setNorOperando(novedadDTO.getOperando());
        novedadEntity.setNorValorCredito(novedadDTO.getValorCredito());
        novedadEntity.setNorCoinInMet(novedadDTO.getCoinInMet());
        novedadEntity.setNorCoinOutMet(novedadDTO.getCoinOutMet());
        novedadEntity.setNorJackpotMet(novedadDTO.getJackpotMet());
        novedadEntity.setNorCoinInSclm(novedadDTO.getCoinInSclm());
        novedadEntity.setNorCoinOutSclm(novedadDTO.getCoinOutSclm());
        novedadEntity.setNorJackpotSclm(novedadDTO.getJackpotSclm());
        novedadEntity.setNorObservaciones(novedadDTO.getObservaciones());

        SiiNovedadRegistradaEntity novedadGuardada =
                this.novedadRegistradaRepository.save(novedadEntity);

        log.info("Novedad guardada: código={}, acta={}, serial={}, tienePlaca={}",
                novedadGuardada.getNorCodigo(), numActa,
                novedadGuardada.getNorSerial(), novedadGuardada.getNorTienePlaca());

        return novedadGuardada;
    }

    @Override
    @Transactional
    public int eliminarNovedadesDeActa(Integer numActa) {
        try {
            log.info("Eliminando novedades existentes del acta {}", numActa);

            Collection<SiiNovedadRegistradaEntity> novedadesExistentes =
                    this.novedadRegistradaRepository.findByNumActa(numActa);

            if (novedadesExistentes.isEmpty()) {
                log.debug("No hay novedades existentes para eliminar del acta {}", numActa);
                return 0;
            }

            int eliminadas = 0;

            for (SiiNovedadRegistradaEntity novedad : novedadesExistentes) {
                try {
                    this.novedadRegistradaRepository.delete(novedad);
                    eliminadas++;
                } catch (Exception e) {
                    log.error("Error al eliminar novedad {}: {}",
                            novedad.getNorCodigo(), e.getMessage(), e);
                }
            }

            log.info("Se eliminaron {} novedades del acta {}", eliminadas, numActa);
            return eliminadas;

        } catch (Exception e) {
            log.error("Error al eliminar novedades del acta {}: {}", numActa, e.getMessage(), e);
            return 0;
        }
    }

}