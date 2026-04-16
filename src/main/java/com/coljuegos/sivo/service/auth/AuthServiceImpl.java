package com.coljuegos.sivo.service.auth;

import com.coljuegos.sivo.data.dto.AuthRequestDTO;
import com.coljuegos.sivo.data.dto.AuthResponseDTO;
import com.coljuegos.sivo.data.dto.UserDTO;
import com.coljuegos.sivo.data.dto.UserResponseDTO;
import com.coljuegos.sivo.service.ldap.LdapService;
import com.coljuegos.sivo.service.user.UserDetailsService;
import com.coljuegos.sivo.util.AesUtil;
import com.coljuegos.sivo.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserDetailsService userDetailsService;

    private final AesUtil aesUtil;

    private final JwtUtil jwtUtil;

    private final LdapService ldapService;

    @Override
    public AuthResponseDTO auth(AuthRequestDTO authRequest) {
        try {
            if (authRequest == null || authRequest.getUsername() == null || authRequest.getPassword() == null) {
                log.warn("Intento de autenticación con datos incompletos");
                throw new BadCredentialsException("Credenciales incompletas");
            }

            String username = authRequest.getUsername().trim().toUpperCase();

            log.info("Iniciando proceso de autenticación para usuario: {}", username);

            UserDTO user = (UserDTO) this.userDetailsService.loadUserByUsername(username);

            if (user == null) {
                log.warn("Usuario {} no encontrado en el sistema", username);
                throw new BadCredentialsException("Credenciales inválidas");
            }

            String decryptedPassword;
            try {
                decryptedPassword = this.aesUtil.decrypt(authRequest.getPassword());
                log.debug("Contraseña descifrada exitosamente para usuario: {}", authRequest.getUsername());
            } catch (Exception ex) {
                log.error("Error al descifrar la contraseña para usuario: {}", authRequest.getUsername(), ex);
                throw new BadCredentialsException("Error en el formato de credenciales");
            }

            //  PASO 1: Autenticación por BD (hash MD5 con salt)
            String hashedPassword = convertPassword(decryptedPassword, user.getSalt());
            boolean autenticadoPorBD = hashedPassword.equalsIgnoreCase(user.getPassword());


            if (!autenticadoPorBD) {
                // PASO 2: Fallback → Directorio Activo (LDAP)
                // Igual que el legacy: si falla BD, intenta AD con password en texto plano
                log.info("Autenticación BD fallida para '{}', intentando LDAP...", username);

                boolean ldapOk = ldapService.autenticar(authRequest.getUsername().trim(), decryptedPassword);

                if (!ldapOk) {
                    log.warn("Autenticación LDAP también fallida para usuario: {}", username);
                    throw new BadCredentialsException("Credenciales inválidas");
                }

                log.info("Autenticación LDAP exitosa para usuario: {}", username);
                // El usuario ya fue cargado desde BD arriba — continuamos con ese objeto
            } else {
                log.info("Autenticación por BD exitosa para usuario: {}", username);
            }

            String token;
            try {
                token = this.jwtUtil.generateToken(user);
                log.debug("Token JWT generado exitosamente para usuario: {}", user.getUsername());
            } catch (Exception ex) {
                log.error("Error al generar token JWT para usuario: {}", authRequest.getUsername(), ex);
                throw new RuntimeException("Error interno del servidor");
            }

            log.info("Autenticación exitosa para usuario: {} con {} roles",
                    user.getUsername(), user.getAuthorities().size());

            UserResponseDTO userResponse = this.mapUser(user);

            return new AuthResponseDTO(token, user.getUsername(), userResponse);

        } catch (BadCredentialsException ex) {
            throw ex;
        } catch (Exception ex) {
            log.error("Error inesperado durante autenticación para usuario: {}",
                    authRequest != null ? authRequest.getUsername() : "unknown", ex);
            throw new BadCredentialsException(ex.getMessage());
        }
    }

    public static String bytesMD5(String cadena) {
        if (cadena == null || cadena.isEmpty()) {
            throw new IllegalArgumentException("La cadena para hash MD5 no puede ser nula o vacía");
        }

        MessageDigest md;
        StringBuilder tempCadena = new StringBuilder();
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] mdBytes = md.digest(cadena.getBytes());
            int tempEntero;

            for (byte mdByte : mdBytes) {
                tempEntero = mdByte;
                if (tempEntero < 0) {
                    tempEntero = 256 + tempEntero;
                }
                if (tempEntero < 16) {
                    tempCadena.append("0").append(Integer.toHexString(tempEntero));
                } else {
                    tempCadena.append(Integer.toHexString(tempEntero));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("Error al generar hash MD5 - Algoritmo no disponible", e);
            throw new RuntimeException("Error en la configuración del sistema de seguridad", e);
        }
        return tempCadena.toString();
    }

    public static String convertPassword(String password, String salt) {
        if (password == null || salt == null) {
            throw new IllegalArgumentException("La contraseña y el salt no pueden ser nulos");
        }

        String workingPassword = password;

        for (int i = 0; i < 1000; i++) {
            if (i == 500) {
                workingPassword = salt + workingPassword + salt;
            }
            workingPassword = bytesMD5(workingPassword);
        }
        return "{MD5}" + workingPassword;
    }

    private UserResponseDTO mapUser(UserDTO user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();

        if (user != null) {
            userResponseDTO.setNameUser(user.getUsername() != null ? user.getUsername() : "Usuario desconocido");
            userResponseDTO.setIdUser(user.getIdUser() != null ? user.getIdUser() : user.getUsername());
            userResponseDTO.setEmailUser(user.getEmail() != null ? user.getEmail() : "");
            userResponseDTO.setPerCodigo(user.getPerCodigo() != null ? user.getPerCodigo() : 0L);
            userResponseDTO.setFullNameUser(user.getFullName());

            log.debug("Usuario mapeado: ID={}, Name={}, Email={}, PerCodigo={}",
                    userResponseDTO.getIdUser(),
                    userResponseDTO.getNameUser(),
                    userResponseDTO.getEmailUser(),
                    userResponseDTO.getPerCodigo());
        } else {
            log.warn("Intento de mapear un usuario nulo");
        }

        return userResponseDTO;
    }

}
