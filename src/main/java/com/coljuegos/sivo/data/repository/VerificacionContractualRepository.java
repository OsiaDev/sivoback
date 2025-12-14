package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiVerificacionContractualEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificacionContractualRepository extends JpaRepository<SiiVerificacionContractualEntity, Long> {

    @Query("SELECT vc FROM SiiVerificacionContractualEntity vc WHERE vc.vcoNumActa = :numActa")
    Optional<SiiVerificacionContractualEntity> findByNumActa(@Param("numActa") Integer numActa);

    @Query("SELECT vc FROM SiiVerificacionContractualEntity vc WHERE vc.siiAutoComisorio.aucCodigo = :aucCodigo")
    Optional<SiiVerificacionContractualEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

}