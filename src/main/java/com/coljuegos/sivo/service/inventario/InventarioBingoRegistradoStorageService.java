package com.coljuegos.sivo.service.inventario;

import com.coljuegos.sivo.data.dto.acta.InventarioBingoRegistradoDTO;
import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.SiiInventarioBingoRegistradoEntity;

import java.util.List;

public interface InventarioBingoRegistradoStorageService {

    List<SiiInventarioBingoRegistradoEntity> guardarInventarios(List<InventarioBingoRegistradoDTO> inventariosDTO,
                                                           SiiAutoComisorioEntity autoComisorio,
                                                           Integer numActa);
                                                           
}
