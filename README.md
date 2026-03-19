# Documentación Sivo Backend (sivo-back)

Esta documentación proporciona una visión general de la arquitectura, configuración y los endpoints principales del backend de Sivo, diseñada para ayudar a los futuros desarrolladores a entender y operar la aplicación rápidamente.

## 1. Stack Tecnológico
- **Framework:** Spring Boot (Java)
- **Base de Datos:** Oracle Database (usando `OracleDialect` con Hibernate/JPA)
- **Seguridad:** Spring Security con JWT (JSON Web Tokens)
- **Reportes:** JasperReports (generación de PDFs de actas)
- **Almacenamiento:** Sistema de archivos local (NAS o disco para imágenes y reportes)

## 2. Configuración y Variables de Entorno

El proyecto se configura principalmente a través de `src/main/resources/application.yml`. Para ejecutar el proyecto localmente o en un servidor, necesitas configurar las siguientes variables de entorno:

| Variable | Descripción | Valor por defecto (si aplica) |
|---|---|---|
| `PORT` | Puerto de ejecución del servidor | `8180` |
| `URL_DB` | URL de conexión de Oracle (ej: `jdbc:oracle:thin:@localhost:1521/XE`) | N/A (Requerido) |
| `USER_DB` | Usuario de la base de datos de Oracle | N/A (Requerido) |
| `PASS_DB` | Contraseña de la base de datos | N/A (Requerido) |
| `MAIL_USERNAME` | Correo utilizado para el envío de notificaciones SMTP | N/A |
| `MAIL_PASSWORD` | Contraseña o App Password para el envío de correos | N/A |
| `MAIL_SENDER` | Remitente que aparece en el correo enviado | `no-reply@coljuegos.gov.co` |
| `SIVO_STORAGE_PATH` | Ruta base donde se guardarán las imágenes y PDFs | `E:/archivoSiicol` |

> **Nota importante sobre el contexto:** Todos los endpoints están bajo el *context-path* `/sivo`.

## 3. Endpoints Principales (API REST)

A continuación, se describen los controladores principales de la aplicación. La mayoría de los endpoints (excepto Auth y Health) requieren estar autenticados, pasando el JWT en la cabecera `Authorization: Bearer <token>`.

### 3.1. Autenticación (`AuthController`)
- **`POST /sivo/auth/login`**
  - **Función:** Realiza la autenticación en el sistema.
  - **Body Payload:** `AuthRequestDTO` (usualmente usuario y contraseña).
  - **Respuesta:** `AuthResponseDTO` (contiene el token JWT, expiración y código de persona asignado).

### 3.2. Gestión de Actas (`ActaController`)
> **Requieren autenticación (JWT).** Este controlador es el corazón de la sincronización de las inspecciones.
- **`GET /sivo/acta/obtenerActas`**
  - **Función:** Recupera las actas preasignadas o descargables por el usuario autenticado. (El ID/código del usuario se extrae automáticamente del token en la sesión).
- **`POST /sivo/acta/upload`**
  - **Función:** Endpoint principal para la subida de un acta completa (datos estructurados en JSON) luego de ser diligenciada de manera offline u online en la aplicación móvil.
  - **Body Payload:** `ActaCompleteDTO`.
- **`POST /sivo/acta/uploadImage`**
  - **Función:** Permite subir de manera individual imágenes asociadas a un acta. Muy útil en caso de que ocurran fallas en la red en el campo y se necesite reintentar la subida de fotos de forma puntual posteriormente.
  - **Body Payload:** `UploadImagenActaDTO`.

### 3.3. Obtención de Maestros (`MaestroController`)
> **Requiere autenticación (JWT).**
- **`GET /sivo/maestros/obtenerMaestros`**
  - **Función:** Retorna catálogos, listas y configuraciones maestras necesarias por la aplicación móvil (por ejemplo: listas de estados, tipos de documentos, listas de chequeo base) que la app debe precargar para poder operar offline.

### 3.4. Monitorización y Salud (`HealthCheckController` y Actuator)
- **`GET /sivo/health/visita`**
  - **Función:** Endpoint expuesto para realizar una validación proactiva y específica a nivel de base de datos, verificando que el backend puede transaccionar/consultar contra el esquema de **Visitas**. Ideal para pruebas de DevOps, balanceadores de red o scripts de automatización.
- **`GET /sivo/actuator/health`**
  - **Función:** Endpoint estándar de Spring Boot Actuator que expone el status genérico de la JVM y el servicio.

## 4. Instructivo Rápido para Ejecutar el Proyecto
Para arrancar el backend en tu máquina local:
1. Clona el repositorio y asegúrate de tener instalado el JDK de Java (11 o 17 usualmente, verifica el `pom.xml`) y Maven.
2. Configura las variables de entorno detalladas en la tabla anterior en tu IDE preferido (IntelliJ, Eclipse, VSCode) o en tu terminal.
3. Ejecuta el ciclo `mvn clean install` para actualizar las dependencias y constatar compilación exitosa.
4. Lanza la aplicación mediante: `mvn spring-boot:run` o ejecutando la función `main` en tu clase SivoApplication.
5. El servidor normalmente arrancará y expondrá el API en: `http://localhost:8180/sivo`.

## 5. Arquitectura del Flujo Funcional Típico
1. La **Aplicación Móvil** (cliente) se autentica mediante `/auth/login` y obtiene el JWT y credenciales.
2. Se realiza una sincronización inicial hacia abajo (Download): la app móvil descarga **Maestros** y sus **Actas** usando los correspondientes endpoints mencionados.
3. El inspector realiza el proceso de visita **de manera completamente offline** almacenando evidencias y estados en Room (base de datos SQLite del teléfono).
4. Cuando el inspector decide sincronizar, se envía el nuevo objeto consolidado de vuelta por el Endpoint `/acta/upload` y en paralelo/secuencia se envían las imágenes modificadas a través de `/acta/uploadImage`.
5. En el backend, tras asegurar la persistencia transaccional, ocurre un procesamiento (a menudo asíncrono utilizando WorkManager u otros jobs) que procesa la plantilla usando JasperReports, genera el **PDF definitivo** del Acta y eventualmente coordina el envío por un **correo electrónico** SMTP notificando a las entidades de control o puntos regulados implicados.
