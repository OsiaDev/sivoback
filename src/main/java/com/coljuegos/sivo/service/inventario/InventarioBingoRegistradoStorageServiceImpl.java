package com.coljuegos.sivo.service.inventario;

import com.coljuegos.sivo.data.dto.acta.InventarioBingoRegistradoDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiInventarioBingoRegistradoEntity;
import com.coljuegos.sivo.data.repository.InventarioBingoRegistradoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class InventarioBingoRegistradoStorageServiceImpl implements InventarioBingoRegistradoStorageService {

    private final InventarioBingoRegistradoRepository inventarioBingoRegistradoRepository;

    @Override
    public List<SiiInventarioBingoRegistradoEntity> guardarInventarios(List<InventarioBingoRegistradoDTO> inventariosDTO,
                                                                  SiiAutoComisorioEntity autoComisorio,
                                                                  Integer numActa) {
        
        if (inventariosDTO == null || inventariosDTO.isEmpty()) {
            return new ArrayList<>();
        }

        log.info("Guardando {} inventarios de BINGO para acta: {}", inventariosDTO.size(), numActa);

        // Fetch current ones
        List<SiiInventarioBingoRegistradoEntity> existentes = this.inventarioBingoRegistradoRepository
                .findByAutoComisorioCodigo(autoComisorio.getAucCodigo());

        if (!existentes.isEmpty()) {
            log.info("Eliminando {} inventarios BINGO existentes para el acta {}", existentes.size(), numActa);
            this.inventarioBingoRegistradoRepository.deleteAll(existentes);
        }

        List<SiiInventarioBingoRegistradoEntity> entidadesAGuardar = inventariosDTO.stream()
                .map(dto -> {
                    SiiInventarioBingoRegistradoEntity entity = new SiiInventarioBingoRegistradoEntity();
                    entity.setSiiAutoComisorio(autoComisorio);
                    entity.setIbrNumActa(numActa);
                    
                    entity.setIbrSerial(dto.getSerial());
                    entity.setIbrMarca(dto.getMarca());
                    entity.setIbrCodigoApuesta(dto.getCodigoApuesta());
                    entity.setIbrEstado(dto.getEstado());
                    
                    Integer codDiferente = (dto.getCodigoApuestaDiferente() != null && dto.getCodigoApuestaDiferente()) ? 1 : 0;
                    entity.setIbrCodApuestaDiferente(codDiferente);
                    entity.setIbrCodApuestaDiferenteValor(dto.getCodigoApuestaDiferenteValor());
                    
                    Integer sillasDiferente = (dto.getSillasDiferente() != null && dto.getSillasDiferente()) ? 1 : 0;
                    entity.setIbrSillasDiferente(sillasDiferente);
                    entity.setIbrSillasValor(dto.getSillasValor());
                    entity.setIbrSillasOriginal(dto.getSillasOriginal());
                    
                    entity.setIbrObservaciones(dto.getObservaciones());
                    
                    return entity;
                })
                .collect(Collectors.toList());

        List<SiiInventarioBingoRegistradoEntity> guardados = this.inventarioBingoRegistradoRepository.saveAll(entidadesAGuardar);
        log.info("Guardados exitosamente {} inventarios de BINGO para el acta {}", guardados.size(), numActa);
        
        return guardados;
    }
}
