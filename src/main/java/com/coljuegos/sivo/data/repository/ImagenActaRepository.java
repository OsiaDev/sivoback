package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiImagenActaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ImagenActaRepository extends JpaRepository<SiiImagenActaEntity, Long> {

    @Query("SELECT img FROM SiiImagenActaEntity img WHERE img.imaNumActa = :numActa")
    Collection<SiiImagenActaEntity> findByNumActa(@Param("numActa") Integer numActa);

    @Query("SELECT img FROM SiiImagenActaEntity img WHERE img.siiAutoComisorio.aucCodigo = :aucCodigo")
    Collection<SiiImagenActaEntity> findByAucCodigo(@Param("aucCodigo") Long aucCodigo);

    @Query("SELECT COUNT(img) FROM SiiImagenActaEntity img WHERE img.imaNumActa = :numActa")
    Long countByNumActa(@Param("numActa") Integer numActa);

}