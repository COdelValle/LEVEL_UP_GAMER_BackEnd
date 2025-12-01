# LEVEL_UP_GAMER BackEnd 🚀

Una API RESTful para la plataforma LEVEL_UP_GAMER: gestión de usuarios, productos, órdenes y blog, con autenticación JWT y soporte de API Keys para integraciones máquina-a-máquina.

**Diseñado para**: desarrolladores y testers que desean ejecutar localmente el backend, probar endpoints y entender rápidamente las reglas de seguridad.

**Estado:** En desarrollo / Hotfix branch activo

**Stack principal**
- **Java 21** (configurado en `pom.xml`)
- **Spring Boot 3.5.7**
- **Spring Boot** (Web, Security)
- **JWT** para autenticación (tokens con claim `rol`)
- **API Keys** alternativamente mediante header `X-API-Key`
- **Maven** (wrapper incluido: `mvnw`)

**Estructura principal del proyecto**
**Stack principal**
- Java 21 (configurado en `pom.xml`)
- Spring Boot 3.5.7
- Spring Web (REST)
- Spring Security (autenticación y autorización)
- Spring Data JPA (persistencia)
- JWT (gestión de tokens con claim `rol`)
- Soporte de API Keys mediante header `X-API-Key`
- Maven (wrapper incluido: `mvnw`)
- Las rutas están protegidas por Spring Security: la regla general exige autenticación (`.anyRequest().authenticated()`).
- Los roles en el sistema son gestionados como `ROLE_USER`, `ROLE_ADMIN`, `ROLE_SELLER`, `ROLE_GUEST`.
- Las anotaciones `@PreAuthorize` están habilitadas (se usa `@EnableMethodSecurity`) y el filtro asigna authorities desde el claim `rol` del JWT o desde el usuario asociado a una `X-API-Key`.

**Primeros pasos (ejecutar localmente)**
1. Abrir PowerShell en la raíz del repo (donde está `BackEnd`).
2. Compilar y ejecutar:

```powershell
cd BackEnd
.\mvnw clean package -DskipTests
.\mvnw spring-boot:run
**Primeros pasos (ejecución local)**
1. Abrir consola en la raíz del repositorio (carpeta `BackEnd`).
2. Compilar y ejecutar con Maven (wrapper incluido):

```powershell
cd BackEnd
.\mvnw clean package -DskipTests
.\mvnw spring-boot:run
```

La aplicación se expone por defecto en `http://localhost:8080`.

**Autenticación rápida**
- Registro y login: las rutas públicas de autenticación están bajo `POST /api/v1/auth/**` y devuelven un token JWT y, cuando corresponde, una `apiKey`.
- Usar el header `Authorization: Bearer {token}` para llamadas autenticadas con JWT.
- Para integraciones máquina-a-máquina, usar el header `X-API-Key: {apiKey}`.
- Registrar / Iniciar sesión: las rutas públicas de autenticación están bajo `POST /api/v1/auth/**` (devuelven un token JWT y, cuando aplica, `apiKey`).
- Usar el header `Authorization: Bearer {token}` para llamadas con JWT.

```bash
# 1) Login (ejemplo genérico)
curl -X POST "http://localhost:8080/api/v1/auth/login" -H "Content-Type: application/json" -d '{"email":"tu@email","password":"tuPass"}'
```
**Colección Postman**
- Existe una colección lista para importar: `docs/LEVEL_UP_GAMER.postman_collection.json`.

Pasos para importar y usar la colección:
1. Postman → Import → seleccionar `docs/LEVEL_UP_GAMER.postman_collection.json`.
2. La colección está organizada por áreas: Autenticación, Usuarios, Productos, Blog, Órdenes.
3. Tras realizar login, copiar el token y colocarlo en la variable `{{token}}` de la colección o usar el header `Authorization: Bearer {TOKEN}`.
4. Para pruebas de integración máquina-a-máquina usar `X-API-Key: {API_KEY}`.

# 2) Usar token para llamar endpoint protegido

# 3) Usar API Key
**Tecnologías y dependencias principales**
El proyecto utiliza Maven y las dependencias más relevantes (nombres simplificados) son:

- Spring Web
- Spring Security
- Spring Data JPA
- SpringDoc (OpenAPI / Swagger)
- JJWT (biblioteca para JWT)
- Jakarta Validation
- BCrypt / Spring Security Crypto
- Oracle JDBC (driver)
- Oracle PKI
- Lombok (opcional)
- Librerías de test: Spring Boot Test y Spring Security Test
------------------
- Hay una colección lista para importar en `docs/LEVEL_UP_GAMER.postman_collection.json`.
**Notas sobre Java**
- El proyecto está configurado para usar Java 21 (`<java.version>21</java.version>` en `pom.xml`). Se recomienda compilar y ejecutar con JDK 21 para evitar incompatibilidades.
	2. La colección trae requests organizados por área (Auth, Usuarios, Productos, Blog, Órdenes).
	3. Después de hacer `login` copia el `token` y pégalo en la variable `{{token}}` de la colección o usa el header `Authorization: Bearer {TOKEN}`.
Material adicional
- En `docs/` está la colección Postman mencionada. Se puede añadir un archivo de entorno para Postman con variables ejemplo (`token`, `apiKey`, `baseUrl`) si se desea.