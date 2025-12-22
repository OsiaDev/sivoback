package com.coljuegos.sivo.service.inventario;

import com.coljuegos.sivo.data.dto.acta.InventarioRegistradoDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiInventarioRegistradoEntity;
import com.coljuegos.sivo.data.repository.InventarioRegistradoRepository;
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
public class InventarioRegistradoStorageServiceImpl implements InventarioRegistradoStorageService {

    private final InventarioRegistradoRepository inventarioRegistradoRepository;

    @Override
    @Transactional
    public List<SiiInventarioRegistradoEntity> guardarInventarios(
            Collection<InventarioRegistradoDTO> inventarios,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa) {

        if (inventarios == null || inventarios.isEmpty()) {
            log.debug("No hay inventarios para guardar en acta {}", numActa);
            return new ArrayList<>();
        }

        log.info("Guardando {} inventarios para acta {}", inventarios.size(), numActa);

        List<SiiInventarioRegistradoEntity> inventariosGuardados = new ArrayList<>();
        int contador = 0;

        for (InventarioRegistradoDTO inventarioDTO : inventarios) {
            try {
                contador++;
                log.debug("Procesando inventario {}/{} para acta {}",
                        contador, inventarios.size(), numActa);

                SiiInventarioRegistradoEntity inventarioGuardado = guardarInventario(
                        inventarioDTO,
                        autoComisorio,
                        numActa);

                inventariosGuardados.add(inventarioGuardado);

            } catch (Exception e) {
                log.error("Error al guardar inventario {}/{} (serial: {}) del acta {}: {}",
                        contador, inventarios.size(), inventarioDTO.getSerial(),
                        numActa, e.getMessage(), e);
                // Continuar con los demás inventarios
            }
        }

        log.info("Se guardaron exitosamente {}/{} inventarios para acta {}",
                inventariosGuardados.size(), inventarios.size(), numActa);

        return inventariosGuardados;
    }

    @Override
    @Transactional
    public SiiInventarioRegistradoEntity guardarInventario(
            InventarioRegistradoDTO inventarioDTO,
            SiiAutoComisorioEntity autoComisorio,
            Integer numActa) {

        if (inventarioDTO == null) {
            throw new IllegalArgumentException("InventarioRegistradoDTO no puede ser null");
        }

        if (inventarioDTO.getSerial() == null || inventarioDTO.getSerial().trim().isEmpty()) {
            throw new IllegalArgumentException("Serial del inventario no puede estar vacío");
        }

        log.debug("Procesando inventario con serial: {}", inventarioDTO.getSerial());

        SiiInventarioRegistradoEntity inventarioEntity = new SiiInventarioRegistradoEntity();
        inventarioEntity.setSiiAutoComisorio(autoComisorio);
        inventarioEntity.setInrNumActa(numActa);
        inventarioEntity.setInrSerial(inventarioDTO.getSerial());

        inventarioEntity.setInrMarca(inventarioDTO.getMarca());
        inventarioEntity.setInrCodigoApuesta(inventarioDTO.getCodigoApuesta());
        inventarioEntity.setInrEstado(inventarioDTO.getEstado());

        inventarioEntity.setInrCodApuestaDiferente(
                convertBooleanToInteger(inventarioDTO.getCodigoApuestaDiferente())
        );
        inventarioEntity.setInrCodApuestaDiferenteValor(
                inventarioDTO.getCodigoApuestaDiferenteValor()
        );
        inventarioEntity.setInrSerialVerificado(
                convertBooleanToInteger(inventarioDTO.getSerialVerificado())
        );
        inventarioEntity.setInrSerialDiferente(
                inventarioDTO.getSerialDiferente()
        );
        inventarioEntity.setInrDescripcionJuego(
                convertBooleanToInteger(inventarioDTO.getDescripcionJuego())
        );
        inventarioEntity.setInrPlanPremios(
                convertBooleanToInteger(inventarioDTO.getPlanPremios())
        );
        inventarioEntity.setInrValorPremios(
                convertBooleanToInteger(inventarioDTO.getValorPremios())
        );
        inventarioEntity.setInrValorCredito(inventarioDTO.getValorCredito());
        inventarioEntity.setInrContadoresVerificado(
                convertBooleanToInteger(inventarioDTO.getContadoresVerificado())
        );
        inventarioEntity.setInrCoinInMet(inventarioDTO.getCoinInMet());
        inventarioEntity.setInrCoinOutMet(inventarioDTO.getCoinOutMet());
        inventarioEntity.setInrJackpotMet(inventarioDTO.getJackpotMet());
        inventarioEntity.setInrCoinInSclm(inventarioDTO.getCoinInSclm());
        inventarioEntity.setInrCoinOutSclm(inventarioDTO.getCoinOutSclm());
        inventarioEntity.setInrJackpotSclm(inventarioDTO.getJackpotSclm());
        inventarioEntity.setInrObservaciones(inventarioDTO.getObservaciones());

        SiiInventarioRegistradoEntity inventarioGuardado =
                this.inventarioRegistradoRepository.save(inventarioEntity);

        log.info("Inventario guardado: código={}, acta={}, serial={}",
                inventarioGuardado.getInrCodigo(), numActa, inventarioGuardado.getInrSerial());

        return inventarioGuardado;
    }

    private Integer convertBooleanToInteger(Boolean value) {
        if (value == null) {
            return null;
        }
        return value ? 1 : 0;
    }

    @Override
    @Transactional
    public int eliminarInventariosDeActa(Integer numActa) {
        try {
            log.info("Eliminando inventarios existentes del acta {}", numActa);

            Collection<SiiInventarioRegistradoEntity> inventariosExistentes =
                    this.inventarioRegistradoRepository.findByNumActa(numActa);

            if (inventariosExistentes.isEmpty()) {
                log.debug("No hay inventarios existentes para eliminar del acta {}", numActa);
                return 0;
            }

            int eliminados = 0;

            for (SiiInventarioRegistradoEntity inventario : inventariosExistentes) {
                try {
                    this.inventarioRegistradoRepository.delete(inventario);
                    eliminados++;
                } catch (Exception e) {
                    log.error("Error al eliminar inventario {}: {}",
                            inventario.getInrCodigo(), e.getMessage(), e);
                }
            }

            log.info("Se eliminaron {} inventarios del acta {}", eliminados, numActa);
            return eliminados;

        } catch (Exception e) {
            log.error("Error al eliminar inventarios del acta {}: {}", numActa, e.getMessage(), e);
            return 0;
        }
    }

}