package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiVerificacionJuegoResponsableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface VerificacionJuegoResponsableRepository extends JpaRepository<SiiVerificacionJuegoResponsableEntity, Long> {

    @Query("SELECT v FROM SiiVerificacionJuegoResponsableEntity v WHERE v.siiAutoComisorio.aucCodigo = :aucCodigo")
    Optional<SiiVerificacionJuegoResponsableEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

}