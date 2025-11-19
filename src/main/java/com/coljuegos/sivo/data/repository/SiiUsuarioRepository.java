package com.coljuegos.sivo.data.repository;

import com.coljuegos.sivo.data.entity.SiiUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SiiUsuarioRepository extends JpaRepository<SiiUsuarioEntity, Long> {

    @Query("select u from SiiUsuarioEntity u where u.usuNombreUsuario = :username")
    Optional<SiiUsuarioEntity> findByUsuNombreUsuario(@Param("username") String username);

}
