package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiActaVisitaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActaVisitaRepository extends JpaRepository<SiiActaVisitaEntity, Long> {

    @Query("SELECT av FROM SiiActaVisitaEntity av WHERE av.aviNumActa = :numActa")
    Optional<SiiActaVisitaEntity> findByNumActa(@Param("numActa") Integer numActa);

    @Query("SELECT av FROM SiiActaVisitaEntity av WHERE av.siiAutoComisorio.aucCodigo = :aucCodigo")
    Optional<SiiActaVisitaEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

    @Query("SELECT av FROM SiiActaVisitaEntity av WHERE av.aviEstadoNotificacion IN ('PENDIENTE', 'ERROR') AND av.aviIntentosNotificacion < :maxIntentos")
    List<SiiActaVisitaEntity> findActasParaNotificar(@Param("maxIntentos") Integer maxIntentos);

}