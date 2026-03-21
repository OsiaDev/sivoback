package com.coljuegos.sivo.service.notificacion;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import com.coljuegos.sivo.data.entity.visita.*;
import com.coljuegos.sivo.data.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReenvioCorreoServiceImpl implements ReenvioCorreoService {

    private final AutoComisorioRepository autoComisorioRepository;
    private final ActaVisitaRepository actaVisitaRepository;
    private final VerificacionContractualRepository verificacionContractualRepository;
    private final VerificacionSiplaftRepository verificacionSiplaftRepository;
    private final VerificacionJuegoResponsableRepository verificacionJuegoResponsableRepository;
    private final FirmaActaRepository firmaActaRepository;
    private final ResumenInventarioRepository resumenInventarioRepository;
    private final InventarioRegistradoRepository inventarioRegistradoRepository;
    private final NovedadRegistradaRepository novedadRegistradaRepository;
    
    private final ActaNotificacionService actaNotificacionService;

    @Override
    @Transactional(readOnly = true)
    public void reenviarCorreoPost(Integer numActa) {
        log.info("[REENVIO] Iniciando reenvío de correo para el acta {}", numActa);

        SiiAutoComisorioEntity autoComisorio = autoComisorioRepository.findByAucNumero(numActa)
                .orElseThrow(() -> new RuntimeException("No se encontró el auto comisorio con número: " + numActa));

        Long aucCodigo = autoComisorio.getAucCodigo();

        inicializarAsociaciones(autoComisorio);

        SiiActaVisitaEntity actaVisita = actaVisitaRepository.findByAutoComisorioCodigo(aucCodigo).orElse(null);
        SiiVerificacionContractualEntity contractual = verificacionContractualRepository.findByAutoComisorioCodigo(aucCodigo).orElse(null);
        SiiVerificacionSiplaftEntity siplaft = verificacionSiplaftRepository.findByAutoComisorioCodigo(aucCodigo).orElse(null);
        SiiVerificacionJuegoResponsableEntity juegoResp = verificacionJuegoResponsableRepository.findByAutoComisorioCodigo(aucCodigo).orElse(null);
        SiiFirmaActaEntity firma = firmaActaRepository.findByAutoComisorioCodigo(aucCodigo).orElse(null);
        SiiResumenInventarioEntity resumen = resumenInventarioRepository.findByAutoComisorioCodigo(aucCodigo).orElse(null);
        
        List<SiiInventarioRegistradoEntity> inventarios = new ArrayList<>(
                inventarioRegistradoRepository.findByAutoComisorioCodigo(aucCodigo)
        );
        List<SiiNovedadRegistradaEntity> novedades = new ArrayList<>(
                novedadRegistradaRepository.findByAutoComisorioCodigo(aucCodigo)
        );

        actaNotificacionService.notificarActaAsync(
                autoComisorio, actaVisita, contractual, siplaft, juegoResp, firma, resumen, inventarios, novedades
        );
        log.info("[REENVIO] Solicitud de reenvío procesada y delegada asíncronamente para el acta {}", numActa);
    }

    private void inicializarAsociaciones(SiiAutoComisorioEntity auto) {
        if (auto.getSiiContrato() != null) {
            auto.getSiiContrato().getConNumero(); 
            if (auto.getSiiContrato().getSiiOperadorEntity() != null) {
                auto.getSiiContrato().getSiiOperadorEntity().getOpeCodigo(); 
                if (auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona() != null) {
                    auto.getSiiContrato().getSiiOperadorEntity().getSiiPersona().getPerNumIdentificacion(); 
                }
            }
        }
        if (auto.getSiiEstablecimiento() != null) {
            auto.getSiiEstablecimiento().getEstNombre(); 
            if (auto.getSiiEstablecimiento().getSiiUbicacion() != null) {
                auto.getSiiEstablecimiento().getSiiUbicacion().getUbiNombre(); 
                if (auto.getSiiEstablecimiento().getSiiUbicacion().getSiiUbicacionPadre() != null) {
                    auto.getSiiEstablecimiento().getSiiUbicacion().getSiiUbicacionPadre().getUbiNombre(); 
                }
            }
        }
    }
}
