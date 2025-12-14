package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiVerificacionSiplaftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificacionSiplaftRepository extends JpaRepository<SiiVerificacionSiplaftEntity, Long> {

    @Query("SELECT vs FROM SiiVerificacionSiplaftEntity vs WHERE vs.vsiNumActa = :numActa")
    Optional<SiiVerificacionSiplaftEntity> findByNumActa(@Param("numActa") Integer numActa);

    @Query("SELECT vs FROM SiiVerificacionSiplaftEntity vs WHERE vs.siiAutoComisorio.aucCodigo = :aucCodigo")
    Optional<SiiVerificacionSiplaftEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

}