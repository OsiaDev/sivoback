package com.coljuegos.sivo.service.health;

import com.coljuegos.sivo.data.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class HealthCheckService {

    private final ActaVisitaRepository actaVisitaRepository;
    private final FirmaActaRepository firmaActaRepository;
    private final ImagenActaRepository imagenActaRepository;
    private final InventarioRegistradoRepository inventarioRegistradoRepository;
    private final NovedadRegistradaRepository novedadRegistradaRepository;
    private final ResumenInventarioRepository resumenInventarioRepository;
    private final VerificacionContractualRepository verificacionContractualRepository;
    private final VerificacionJuegoResponsableRepository verificacionJuegoResponsableRepository;
    private final VerificacionSiplaftRepository verificacionSiplaftRepository;
    private final JavaMailSender javaMailSender;

    @Value("${acta.imagenes.base-path}")
    private String basePath;

    @Value("${acta.imagenes.relative-path}")
    private String relativePath;

    @Value("${acta.reporte.jasper-sub-dir:jasperReports}")
    private String jasperSubDir;

    @Value("${acta.reporte.nombre-archivo:actaVisitaComercial}")
    private String jasperFileName;

    public HealthCheckService(ActaVisitaRepository actaVisitaRepository,
            FirmaActaRepository firmaActaRepository,
            ImagenActaRepository imagenActaRepository,
            InventarioRegistradoRepository inventarioRegistradoRepository,
            NovedadRegistradaRepository novedadRegistradaRepository,
            ResumenInventarioRepository resumenInventarioRepository,
            VerificacionContractualRepository verificacionContractualRepository,
            VerificacionJuegoResponsableRepository verificacionJuegoResponsableRepository,
            VerificacionSiplaftRepository verificacionSiplaftRepository,
            JavaMailSender javaMailSender) {
        this.actaVisitaRepository = actaVisitaRepository;
        this.firmaActaRepository = firmaActaRepository;
        this.imagenActaRepository = imagenActaRepository;
        this.inventarioRegistradoRepository = inventarioRegistradoRepository;
        this.novedadRegistradaRepository = novedadRegistradaRepository;
        this.resumenInventarioRepository = resumenInventarioRepository;
        this.verificacionContractualRepository = verificacionContractualRepository;
        this.verificacionJuegoResponsableRepository = verificacionJuegoResponsableRepository;
        this.verificacionSiplaftRepository = verificacionSiplaftRepository;
        this.javaMailSender = javaMailSender;
    }

    public Map<String, Object> checkHealth() {
        Map<String, Object> response = new HashMap<>();

        response.put("database", checkDatabase());
        response.put("fileSystem", checkFileSystem());
        response.put("jasperTemplate", checkJasperFile());
        response.put("emailConnection", checkEmailConnection());

        return response;
    }

    private Map<String, String> checkDatabase() {
        Map<String, String> dbStatus = new HashMap<>();

        dbStatus.put("SiiActaVisitaEntity", checkRepository(() -> actaVisitaRepository.count()));
        dbStatus.put("SiiFirmaActaEntity", checkRepository(() -> firmaActaRepository.count()));
        dbStatus.put("SiiImagenActaEntity", checkRepository(() -> imagenActaRepository.count()));
        dbStatus.put("SiiInventarioRegistradoEntity", checkRepository(() -> inventarioRegistradoRepository.count()));
        dbStatus.put("SiiNovedadRegistradaEntity", checkRepository(() -> novedadRegistradaRepository.count()));
        dbStatus.put("SiiResumenInventarioEntity", checkRepository(() -> resumenInventarioRepository.count()));
        dbStatus.put("SiiVerificacionContractualEntity",
                checkRepository(() -> verificacionContractualRepository.count()));
        dbStatus.put("SiiVerificacionJuegoResponsableEntity",
                checkRepository(() -> verificacionJuegoResponsableRepository.count()));
        dbStatus.put("SiiVerificacionSiplaftEntity", checkRepository(() -> verificacionSiplaftRepository.count()));

        return dbStatus;
    }

    private String checkRepository(Runnable method) {
        try {
            method.run();
            return "OK";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private Map<String, String> checkFileSystem() {
        Map<String, String> fsStatus = new HashMap<>();

        try {
            String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fullPathString = basePath + relativePath + "/" + datePath + "/test/";
            Path dirPath = Paths.get(fullPathString);

            fsStatus.put("path", dirPath.toString());

            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path filePath = dirPath.resolve("text.txt");
            Files.writeString(filePath, "text");

            fsStatus.put("status", "OK");
        } catch (Exception e) {
            fsStatus.put("status", "Error: " + e.getMessage());
        }

        return fsStatus;
    }

    private Map<String, String> checkJasperFile() {
        Map<String, String> jasperStatus = new HashMap<>();
        try {
            Path jasperPath = Paths.get(basePath, jasperSubDir, jasperFileName + ".jasper");
            jasperStatus.put("path", jasperPath.toString());
            
            if (Files.exists(jasperPath)) {
                if (Files.isReadable(jasperPath)) {
                    jasperStatus.put("status", "OK");
                } else {
                    jasperStatus.put("status", "Error: El archivo Jasper existe pero la aplicación no tiene permisos de lectura.");
                }
            } else {
                jasperStatus.put("status", "Error: El archivo de plantilla Jasper (.jasper) no existe en la ruta esperada.");
            }
        } catch (Exception e) {
            jasperStatus.put("status", "Error: " + e.getMessage());
        }
        return jasperStatus;
    }

    private Map<String, String> checkEmailConnection() {
        Map<String, String> emailStatus = new HashMap<>();
        try {
            if (javaMailSender instanceof JavaMailSenderImpl) {
                ((JavaMailSenderImpl) javaMailSender).testConnection();
                emailStatus.put("status", "OK");
            } else {
                emailStatus.put("status", "Error: No se puede probar la conexión pues el MailSender no es implementación por defecto.");
            }
        } catch (Exception e) {
            emailStatus.put("status", "Error de conexión/autenticación SMTP: " + e.getMessage());
        }
        return emailStatus;
    }

}
