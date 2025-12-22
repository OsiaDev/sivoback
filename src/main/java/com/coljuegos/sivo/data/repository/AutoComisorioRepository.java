package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.SiiAutoComisorioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface AutoComisorioRepository extends JpaRepository<SiiAutoComisorioEntity, Long> {

    @Query(value = "SELECT ac.* " +
            "FROM sii_auto_comisorio ac " +
            "JOIN sii_grupo_fiscalizacion gf ON ac.gfi_codigo = gf.gfi_codigo " +
            "JOIN sii_fiscalizador_sustanc fs ON gf.fsu_codigo_princip = fs.fsu_codigo " +
            "JOIN sii_persona p ON fs.per_codigo = p.per_codigo " +
            "WHERE p.per_codigo = :perCodigo " +
            "AND ac.auc_estado = :estado " +
            "AND ac.auc_fecha >= :fechaMesAtras " +
            "AND ( (ac.auc_tipo_visita = 'F' AND :fechaActual <= ac.auc_fecha_visita) " +
            "     OR ac.auc_tipo_visita IN ('C', 'F') ) " +
            "ORDER BY ac.auc_numero DESC",
            nativeQuery = true)
    Collection<SiiAutoComisorioEntity> findFiltradosPorPersonaEstadoTipoYFechas(
            @Param("perCodigo") Long perCodigo,
            @Param("estado") String estado,
            @Param("fechaMesAtras") LocalDate fechaMesAtras,
            @Param("fechaActual") LocalDate fechaActual);

    @Query("SELECT ac FROM SiiAutoComisorioEntity ac WHERE ac.aucNumero = :aucNumero")
    Optional<SiiAutoComisorioEntity> findByAucNumero(@Param("aucNumero") Integer aucNumero);

}