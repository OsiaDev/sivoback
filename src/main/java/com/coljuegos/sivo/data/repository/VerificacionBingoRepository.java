package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiVerificacionBingoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificacionBingoRepository extends JpaRepository<SiiVerificacionBingoEntity, Long> {

    @Query("SELECT v FROM SiiVerificacionBingoEntity v WHERE v.siiAutoComisorio.aucCodigo = :aucCodigo")
    Optional<SiiVerificacionBingoEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

}
