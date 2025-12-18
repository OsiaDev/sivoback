package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiFirmaActaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FirmaActaRepository extends JpaRepository<SiiFirmaActaEntity, Long> {

    /**
     * Busca firmas del acta por número de acta
     */
    @Query("SELECT fa FROM SiiFirmaActaEntity fa WHERE fa.fiaNumActa = :numActa")
    Optional<SiiFirmaActaEntity> findByNumActa(@Param("numActa") Integer numActa);

    /**
     * Busca firmas del acta por código de auto comisorio
     */
    @Query("SELECT fa FROM SiiFirmaActaEntity fa WHERE fa.siiAutoComisorio.aucCodigo = :aucCodigo")
    Optional<SiiFirmaActaEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

    /**
     * Verifica si existe un registro de firmas para un acta
     */
    @Query("SELECT COUNT(fa) > 0 FROM SiiFirmaActaEntity fa WHERE fa.fiaNumActa = :numActa")
    boolean existsByNumActa(@Param("numActa") Integer numActa);

}