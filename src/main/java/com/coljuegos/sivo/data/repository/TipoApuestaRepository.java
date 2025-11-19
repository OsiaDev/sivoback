package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.SiiTipoApuestaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface TipoApuestaRepository extends JpaRepository<SiiTipoApuestaEntity, Long> {

    @Query("SELECT t FROM SiiTipoApuestaEntity t ORDER BY t.tapNombre ASC")
    Collection<SiiTipoApuestaEntity> findAllOrderByNombre();

}
