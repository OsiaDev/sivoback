package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiNovedadRegistradaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface NovedadRegistradaRepository extends JpaRepository<SiiNovedadRegistradaEntity, Long> {

    /**
     * Busca novedades registradas por número de acta
     */
    @Query("SELECT nr FROM SiiNovedadRegistradaEntity nr WHERE nr.norNumActa = :numActa")
    Collection<SiiNovedadRegistradaEntity> findByNumActa(@Param("numActa") Integer numActa);

    /**
     * Busca novedades registradas por código de auto comisorio
     */
    @Query("SELECT nr FROM SiiNovedadRegistradaEntity nr WHERE nr.siiAutoComisorio.aucCodigo = :aucCodigo")
    Collection<SiiNovedadRegistradaEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

    /**
     * Busca novedades registradas por serial de MET
     */
    @Query("SELECT nr FROM SiiNovedadRegistradaEntity nr WHERE nr.norSerial = :serial")
    Collection<SiiNovedadRegistradaEntity> findBySerial(@Param("serial") String serial);

    /**
     * Cuenta la cantidad de novedades registradas en un acta
     */
    @Query("SELECT COUNT(nr) FROM SiiNovedadRegistradaEntity nr WHERE nr.norNumActa = :numActa")
    Long countByNumActa(@Param("numActa") Integer numActa);

    /**
     * Busca novedades por estado operativo
     */
    @Query("SELECT nr FROM SiiNovedadRegistradaEntity nr WHERE nr.norOperando = :operando")
    Collection<SiiNovedadRegistradaEntity> findByOperando(@Param("operando") String operando);

    /**
     * Busca novedades por si tienen placa (1=SI, 0=NO)
     */
    @Query("SELECT nr FROM SiiNovedadRegistradaEntity nr WHERE nr.norTienePlaca = :tienePlaca")
    Collection<SiiNovedadRegistradaEntity> findByTienePlaca(@Param("tienePlaca") Integer tienePlaca);

    /**
     * Elimina novedades registradas por número de acta
     */
    @Query("DELETE FROM SiiNovedadRegistradaEntity nr WHERE nr.norNumActa = :numActa")
    void deleteByNumActa(@Param("numActa") Integer numActa);

}