package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.dto.InventarioProjection;
import com.coljuegos.sivo.data.entity.SiiInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface InventarioRepository extends JpaRepository<SiiInventarioEntity, Long> {

    @Query(value = """
                SELECT 
                    c.con_numero           AS conNumero,
                    e.est_nombre           AS estNombre,
                    m.met_serial           AS metSerial,
                    m.met_uid              AS metUid,
                    ma.mar_nombre          AS marcaNombre,
                    ta.tap_nombre          AS tipoApuestaNombre,
                    i.ins_codigo::text     AS instrumentoCodigo,
                    ta.tap_codigo_apuesta  AS tipoApuestaCodigo,
                    m.met_online           AS metOnline,
                    iv.inv_sillas          AS invSillas,
                    i.mca_codigo::text     AS mesaCodigo,
                    c.con_codigo           AS conCodigo,
                    auc.auc_numero         AS aucNumero,
                    e.est_codigo           AS estCodigo
                FROM sii_inventario iv
                LEFT JOIN sii_instrumento i ON iv.ins_codigo = i.ins_codigo
                LEFT JOIN sii_mesa_casino mc ON mc.mca_codigo = i.mca_codigo
                LEFT JOIN sii_juego_mesa jm ON mc.jme_codigo = jm.jme_codigo
                LEFT JOIN sii_met m ON i.met_codigo = m.met_codigo
                LEFT JOIN sii_marca ma ON m.mar_codigo = ma.mar_codigo
                LEFT JOIN sii_establecimiento e ON iv.est_codigo = e.est_codigo
                LEFT JOIN sii_novedad n ON iv.nov_codigo = n.nov_codigo
                LEFT JOIN sii_contrato c ON c.con_codigo = n.con_codigo
                LEFT JOIN sii_tipo_apuesta ta ON iv.tap_codigo = ta.tap_codigo
                LEFT JOIN sii_tipo_instrumento ti ON i.tin_codigo = ti.tin_codigo
                LEFT JOIN sii_tipo_novedad tn ON n.tno_codigo = tn.tno_codigo
                LEFT JOIN sii_operador o ON o.ope_codigo = c.ope_codigo
                INNER JOIN sii_auto_comisorio auc ON auc.con_codigo = c.con_codigo
                WHERE c.con_codigo IN :conCodigos
                  AND auc.auc_numero IN :aucNumeros
                  AND e.est_codigo IN :estCodigos
                  AND iv.inv_estado = 'A'
            """, nativeQuery = true)
    Collection<InventarioProjection> buscarInventariosPorFiltros(
            @Param("conCodigos") Collection<Long> conCodigos,
            @Param("aucNumeros") Collection<Integer> aucNumeros,
            @Param("estCodigos") Collection<Long> estCodigos
    );

}