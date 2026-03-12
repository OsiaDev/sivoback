package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiResumenInventarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ResumenInventarioRepository extends JpaRepository<SiiResumenInventarioEntity, Long> {

    @Query("SELECT ri FROM SiiResumenInventarioEntity ri WHERE ri.rsiNumActa = :numActa")
    Optional<SiiResumenInventarioEntity> findByNumActa(@Param("numActa") Integer numActa);

    @Query("SELECT ri FROM SiiResumenInventarioEntity ri WHERE ri.siiAutoComisorio.aucCodigo = :aucCodigo")
    Optional<SiiResumenInventarioEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

}