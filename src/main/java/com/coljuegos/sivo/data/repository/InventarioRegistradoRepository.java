package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiInventarioRegistradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface InventarioRegistradoRepository extends JpaRepository<SiiInventarioRegistradoEntity, Long> {

    /**
     * Busca inventarios registrados por número de acta
     */
    @Query("SELECT ir FROM SiiInventarioRegistradoEntity ir WHERE ir.inrNumActa = :numActa")
    Collection<SiiInventarioRegistradoEntity> findByNumActa(@Param("numActa") Integer numActa);

    /**
     * Busca inventarios registrados por código de auto comisorio
     */
    @Query("SELECT ir FROM SiiInventarioRegistradoEntity ir WHERE ir.siiAutoComisorio.aucCodigo = :aucCodigo")
    Collection<SiiInventarioRegistradoEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

    /**
     * Busca inventarios registrados por serial de MET
     */
    @Query("SELECT ir FROM SiiInventarioRegistradoEntity ir WHERE ir.inrSerial = :serial")
    Collection<SiiInventarioRegistradoEntity> findBySerial(@Param("serial") String serial);

    /**
     * Cuenta la cantidad de inventarios registrados en un acta
     */
    @Query("SELECT COUNT(ir) FROM SiiInventarioRegistradoEntity ir WHERE ir.inrNumActa = :numActa")
    Long countByNumActa(@Param("numActa") Integer numActa);

    /**
     * Elimina inventarios registrados por número de acta
     */
    @Query("DELETE FROM SiiInventarioRegistradoEntity ir WHERE ir.inrNumActa = :numActa")
    void deleteByNumActa(@Param("numActa") Integer numActa);

}