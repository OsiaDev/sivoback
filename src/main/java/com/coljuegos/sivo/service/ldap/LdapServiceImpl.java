package com.coljuegos.sivo.service.ldap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.util.Hashtable;

@Slf4j
@Service
public class LdapServiceImpl implements LdapService {

    @Value("${ldap.url}")
    private String urlDirectorio;

    @Value("${ldap.domain}")
    private String dominioDirectorio;

    /**
     * Autentica un usuario contra el Directorio Activo de Coljuegos.
     * Replica exactamente la lógica de DirectorioActivo.autenticar() del sistema legacy.
     * Formato principal: usuario@coljuegos → ldap://coljuegos.local:389
     *
     * @param usuario    login del usuario (sin dominio)
     * @param contrasena contraseña en texto plano (ya descifrada con AES)
     * @return true si el AD autentica correctamente, false en cualquier error
     */
    public boolean autenticar(String usuario, String contrasena) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, urlDirectorio);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, usuario + "@" + dominioDirectorio);
        env.put(Context.SECURITY_CREDENTIALS, contrasena);

        try {
            DirContext ctx = new InitialDirContext(env);
            ctx.close();
            log.info("Autenticación LDAP exitosa para usuario: {}", usuario);
            return true;
        } catch (AuthenticationException ex) {
            log.warn("LDAP - Credenciales incorrectas para usuario: {}", usuario);
            return false;
        } catch (CommunicationException ex) {
            log.error("LDAP - Error de comunicación con el servidor {}: {}", urlDirectorio, ex.getMessage());
            return false;
        } catch (NamingException ex) {
            log.warn("LDAP - NamingException para usuario {}: {}", usuario, ex.getMessage());
            return false;
        } catch (Exception ex) {
            log.error("LDAP - Error inesperado para usuario {}: {}", usuario, ex.getMessage(), ex);
            return false;
        }
    }

}
