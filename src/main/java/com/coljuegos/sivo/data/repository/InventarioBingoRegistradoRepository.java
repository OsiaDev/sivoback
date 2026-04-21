package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.visita.SiiInventarioBingoRegistradoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventarioBingoRegistradoRepository extends JpaRepository<SiiInventarioBingoRegistradoEntity, Long> {

    @Query("SELECT i FROM SiiInventarioBingoRegistradoEntity i WHERE i.siiAutoComisorio.aucCodigo = :aucCodigo")
    List<SiiInventarioBingoRegistradoEntity> findByAutoComisorioCodigo(@Param("aucCodigo") Long aucCodigo);

}
