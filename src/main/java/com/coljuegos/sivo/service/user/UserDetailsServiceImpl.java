package com.coljuegos.sivo.service.user;

import com.coljuegos.sivo.data.dto.UserDTO;
import com.coljuegos.sivo.data.entity.SiiPersonaEntity;
import com.coljuegos.sivo.data.entity.SiiUsuarioEntity;
import com.coljuegos.sivo.data.entity.SiiUsuarioRolEntity;
import com.coljuegos.sivo.data.repository.SiiUsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SiiUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        if (username == null || username.trim().isEmpty()) {
            log.warn("Intento de carga de usuario con username nulo o vacío");
            throw new UsernameNotFoundException("El nombre de usuario no puede estar vacío");
        }

        String normalizedUsername = username.trim().toUpperCase();
        log.info("Intentando cargar el usuario '{}'", normalizedUsername);

        final SiiUsuarioEntity usuarioEntity;
        try {
            usuarioEntity = usuarioRepository.findByUsuNombreUsuario(normalizedUsername)
                    .orElseThrow(() -> {
                        log.warn("Usuario '{}' no encontrado en la base de datos", normalizedUsername);
                        return new UsernameNotFoundException("Usuario '" + normalizedUsername + "' no encontrado.");
                    });
        } catch (Exception ex) {
            log.error("Error al consultar la base de datos para usuario '{}'", normalizedUsername, ex);
            throw new UsernameNotFoundException("Error al buscar el usuario en el sistema");
        }

        if (usuarioEntity.getSiiUsuarioRolList() == null || usuarioEntity.getSiiUsuarioRolList().isEmpty()) {
            log.warn("Usuario '{}' no tiene roles asignados - Acceso denegado", normalizedUsername);
            throw new UsernameNotFoundException("Usuario '" + normalizedUsername + "' no tiene roles asignados.");
        }

        final Set<GrantedAuthority> authorities = usuarioEntity.getSiiUsuarioRolList()
                .stream()
                .filter(this::isValidRole)
                .map(this::mapRoleToAuthority)
                .collect(Collectors.toSet());

        if (authorities.isEmpty()) {
            log.warn("Usuario '{}' no tiene roles válidos después del filtrado", normalizedUsername);
            throw new UsernameNotFoundException("Usuario '" + normalizedUsername + "' no tiene roles válidos.");
        }

        log.info("Usuario '{}' cargado exitosamente con {} roles válidos: {}",
                normalizedUsername,
                authorities.size(),
                authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        return this.getUserDTO(normalizedUsername, usuarioEntity, authorities);
    }

    private boolean isValidRole(SiiUsuarioRolEntity usuarioRol) {
        if (usuarioRol == null || usuarioRol.getSiiRol() == null) {
            log.warn("Rol nulo encontrado en la lista de roles del usuario");
            return false;
        }

        if (usuarioRol.getSiiRol().getRolNombre() == null || usuarioRol.getSiiRol().getRolNombre().trim().isEmpty()) {
            log.warn("Rol con nombre nulo o vacío encontrado: {}", usuarioRol.getSiiRol().getRolCodigo());
            return false;
        }

        if (usuarioRol.getSiiRol().getRolActivo() != null && !"S".equalsIgnoreCase(usuarioRol.getSiiRol().getRolActivo())) {
            log.debug("Rol inactivo filtrado: {}", usuarioRol.getSiiRol().getRolNombre());
            return false;
        }

        return true;
    }

    private GrantedAuthority mapRoleToAuthority(SiiUsuarioRolEntity usuarioRol) {
        try {
            String nombreRol = usuarioRol.getSiiRol().getRolNombre()
                    .trim()
                    .toUpperCase()
                    .replaceAll("\\s+", "_")
                    .replaceAll("[^A-Z0-9_]", "_");

            String authority = "ROLE_" + nombreRol;

            log.debug("Rol mapeado: '{}' -> '{}'", usuarioRol.getSiiRol().getRolNombre(), authority);

            return new SimpleGrantedAuthority(authority);
        } catch (Exception ex) {
            log.error("Error al mapear rol: {}", usuarioRol.getSiiRol().getRolNombre(), ex);
            return new SimpleGrantedAuthority("ROLE_USER");
        }
    }

    private UserDTO getUserDTO(String username, SiiUsuarioEntity usuarioEntity, Set<GrantedAuthority> authorities) {
        try {
            String nombre = this.extractUserDisplayName(usuarioEntity, username);
            String identificacion = this.extractIdentificacion(usuarioEntity);
            Long perCodigo = this.extractPerCodigo(usuarioEntity);
            String fullName = this.extractFullUserDisplayName(usuarioEntity, username);

            String password = usuarioEntity.getUsuContrasena();
            String salt = usuarioEntity.getUsuSalt();

            if (password == null || password.trim().isEmpty()) {
                log.warn("Usuario '{}' tiene contraseña nula o vacía", username);
                throw new UsernameNotFoundException("Configuración de usuario incompleta");
            }

            if (salt == null || salt.trim().isEmpty()) {
                log.warn("Usuario '{}' tiene salt nulo o vacío", username);
                throw new UsernameNotFoundException("Configuración de usuario incompleta");
            }

            UserDTO user = new UserDTO(nombre, password, salt, identificacion, perCodigo, fullName, authorities);

            user.setIdUser(username);

            String email = usuarioEntity.getUsuEmail();
            if (email != null && !email.trim().isEmpty()) {
                user.setEmail(email.trim());
            } else {
                log.debug("Usuario '{}' no tiene email configurado", username);
                user.setEmail("");
            }

            log.debug("UserDTO creado exitosamente para usuario: {}", username);
            return user;

        } catch (Exception ex) {
            log.error("Error al crear UserDTO para usuario: {}", username, ex);
            throw new UsernameNotFoundException("Error al procesar información del usuario");
        }
    }

    private String extractUserDisplayName(SiiUsuarioEntity usuarioEntity, String fallbackUsername) {
        try {
            if (usuarioEntity.getSiiPersona() != null) {
                String primerNombre = usuarioEntity.getSiiPersona().getPerPrimerNombre();
                if (primerNombre != null && !primerNombre.trim().isEmpty()) {
                    return primerNombre.trim();
                }

                String nombreCompleto = this.buildFullName(usuarioEntity.getSiiPersona());
                if (nombreCompleto != null && !nombreCompleto.trim().isEmpty()) {
                    return nombreCompleto.trim();
                }
            }

            return fallbackUsername;

        } catch (Exception ex) {
            log.warn("Error al extraer nombre de usuario, usando fallback: {}", fallbackUsername, ex);
            return fallbackUsername;
        }
    }

    private String extractFullUserDisplayName(SiiUsuarioEntity usuarioEntity, String fallbackUsername) {
        try {
            if (usuarioEntity.getSiiPersona() != null) {
                String fullName = usuarioEntity.getSiiPersona().getNombreCompleto();
                if (fullName != null && !fullName.trim().isEmpty()) {
                    return fullName.trim();
                }
            }

            return fallbackUsername;

        } catch (Exception ex) {
            log.warn("Error al extraer nombre de usuario, usando fallback: {}", fallbackUsername, ex);
            return fallbackUsername;
        }
    }

    private String extractIdentificacion(SiiUsuarioEntity usuarioEntity) {
        if (usuarioEntity.getSiiPersona() != null) {
            String identificacion = usuarioEntity.getSiiPersona().getPerNumIdentificacion();
            if (identificacion != null && !identificacion.trim().isEmpty()) {
                return identificacion.trim();
            }
        }
        return "";
    }

    private Long extractPerCodigo(SiiUsuarioEntity usuarioEntity) {
        if (usuarioEntity.getSiiPersona() != null) {
            return usuarioEntity.getSiiPersona().getPerCodigo();
        }
        return 0L;
    }

    private String buildFullName(SiiPersonaEntity persona) {
        if (persona == null) return null;

        StringBuilder fullName = new StringBuilder();

        if (persona.getPerPrimerNombre() != null && !persona.getPerPrimerNombre().trim().isEmpty()) {
            fullName.append(persona.getPerPrimerNombre().trim());
        }

        if (persona.getPerSegundoNombre() != null && !persona.getPerSegundoNombre().trim().isEmpty()) {
            if (!fullName.isEmpty()) fullName.append(" ");
            fullName.append(persona.getPerSegundoNombre().trim());
        }

        return !fullName.isEmpty() ? fullName.toString() : null;
    }

}
