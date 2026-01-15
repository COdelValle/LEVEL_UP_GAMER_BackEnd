# LEVEL_UP_GAMER BackEnd 🚀

Una API RESTful para la plataforma LEVEL_UP_GAMER: gestión de usuarios, productos, órdenes y blog, con autenticación JWT y soporte de API Keys para integraciones máquina-a-máquina.

**Diseñado para**: desarrolladores y testers que desean ejecutar localmente el backend, probar endpoints y entender rápidamente las reglas de seguridad.

**Estado:** Finalizado.

## Tabla de contenidos

- [LEVEL\_UP\_GAMER BackEnd 🚀](#level_up_gamer-backend-)
  - [Tabla de contenidos](#tabla-de-contenidos)
  - [Endpoints públicos](#endpoints-públicos)
    - [Autenticación](#autenticación)
    - [Productos](#productos)
    - [Categorías](#categorías)
    - [Regiones](#regiones)
    - [Blog](#blog)
    - [Órdenes](#órdenes)
  - [Stack principal](#stack-principal)
  - [Estructura principal del proyecto](#estructura-principal-del-proyecto)
    - [Stack principal (detalles)](#stack-principal-detalles)
  - [Primeros pasos (ejecutar localmente)](#primeros-pasos-ejecutar-localmente)
  - [Autenticación rápida](#autenticación-rápida)
    - [Ejemplo rápido: Login](#ejemplo-rápido-login)
  - [Colección Postman](#colección-postman)
  - [Tutorial para Frontend](#tutorial-para-frontend)
  - [Usar token para llamar endpoint protegido](#usar-token-para-llamar-endpoint-protegido)
  - [Usar API Key](#usar-api-key)
  - [Colección Postman](#colección-postman-1)
  - [Tutorial para Frontend](#tutorial-para-frontend-1)
  - [Endpoints protegidos (requieren autenticación)](#endpoints-protegidos-requieren-autenticación)
    - [Usuarios](#usuarios)
    - [Productos](#productos-1)
    - [Categorías](#categorías-1)
    - [Regiones](#regiones-1)
    - [Blog](#blog-1)
    - [Órdenes](#órdenes-1)
  - [Usar token para llamar endpoint protegido](#usar-token-para-llamar-endpoint-protegido-1)
  - [Usar API Key](#usar-api-key-1)
  - [Funcionalidades principales](#funcionalidades-principales)
    - [✅ Autenticación y Autorización](#-autenticación-y-autorización)
    - [✅ Gestión de Usuarios](#-gestión-de-usuarios)
    - [✅ Productos y Categorías](#-productos-y-categorías)
    - [✅ Regiones y Comunas](#-regiones-y-comunas)
    - [✅ Blog](#-blog)
    - [✅ Documentación OpenAPI/Swagger](#-documentación-openapiswagger)
    - [Dependencias incluidas en `pom.xml`](#dependencias-incluidas-en-pomxml)
  - [Notas sobre Java](#notas-sobre-java)
  - [Información adicional](#información-adicional)
    - [Configuración de Base de Datos](#configuración-de-base-de-datos)
    - [Ejecución con Maven Wrapper](#ejecución-con-maven-wrapper)
    - [Generar documentación Swagger](#generar-documentación-swagger)
    - [Estructura de carpetas](#estructura-de-carpetas)
## Endpoints públicos

### Autenticación
- `POST /api/v1/auth/register` — Registro de usuario (devuelve `token` y `apiKey` automáticamente)
- `POST /api/v1/auth/login` — Login de usuario (devuelve `token` y `apiKey`)

### Productos
- `GET /api/v1/productos` — Listar todos los productos
- `GET /api/v1/productos/{id}` — Ver detalle de producto

### Categorías
- `GET /api/v1/categorias` — Listar todas las categorías
- `GET /api/v1/categorias/{id}` — Ver detalle de categoría

### Regiones
- `GET /api/v1/regiones` — Listar todas las regiones
- `GET /api/v1/regiones/{id}` — Ver región por ID
- `GET /api/v1/regiones/{id}/comunas` — Obtener comunas de una región
- `GET /api/v1/regiones/comunas` — Obtener todas las comunas del catálogo
- `GET /api/v1/regiones/search?city={city}` — Buscar región por ciudad/comuna
- `GET /api/v1/regiones/validate?regionId={id}&comuna={comuna}` — Validar si una comuna pertenece a una región
- `GET /api/v1/regiones/validate-city?city={city}` — Validar existencia de ciudad

### Blog
- `GET /api/v1/blog` — Listar artículos del blog (incluye todos los campos: featured, likes, tags, views)
- `GET /api/v1/blog/{id}` — Ver detalle de artículo (incluye featured, likes, tags, views)
- `GET /api/v1/blog/destacados` — Artículos destacados
- `GET /api/v1/blog/autor/{autor}` — Artículos por autor
- `POST /api/v1/blog/{id}/views` — **Incrementar vistas** (público, sin autenticación)
- `POST /api/v1/blog/{id}/like` — **Agregar like** (público, sin autenticación)
- `POST /api/v1/blog/{id}/unlike` — **Quitar like** (público, sin autenticación)

### Órdenes
- `POST /api/v1/ordenes` — Crear orden de compra

El resto de los endpoints requieren autenticación JWT o API Key según corresponda.

## Stack principal
- **Java 21** (configurado en `pom.xml`)
- **Spring Boot 3.5.7**
- **Spring Boot** (Web, Security)
- **JWT** para autenticación (tokens con claim `rol`)
- **API Keys** alternativamente mediante header `X-API-Key`
- **Maven** (wrapper incluido: `mvnw`)

## Estructura principal del proyecto
### Stack principal (detalles)
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

## Primeros pasos (ejecutar localmente)

1. Cambiar ruta de la carpeta del proyecto dentro de [application.properties](BackEnd/src/main/resources/application.properties).
2. Abrir PowerShell en la raíz del repo (carpeta `BackEnd`).
3. Compilar y ejecutar con Maven (wrapper incluido):

```powershell
cd BackEnd
.\mvnw clean package -DskipTests
.\mvnw spring-boot:run
```

La aplicación se expone por defecto en `http://localhost:8080`.

## Autenticación rápida

- **Registro y login:** las rutas públicas de autenticación están bajo `POST /api/v1/auth/**` y devuelven un token JWT y una `apiKey` generada automáticamente por el sistema.
- **Usar JWT:** usar el header `Authorization: Bearer {token}` para llamadas autenticadas con JWT.
- **Usar API Key:** para integraciones máquina-a-máquina, usar el header `X-API-Key: {apiKey}`.

### Ejemplo rápido: Login

```bash
curl -X POST "http://localhost:8080/api/v1/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"usuario@example.com","password":"miPassword"}'
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "apiKey": "api_1234567890abcdef",
  "usuarioId": 1,
  "nombre": "Juan García",
  "email": "usuario@example.com",
  "rol": "USER"
}
```
## Colección Postman

Existe una colección lista para importar: [docs/LEVEL_UP_GAMER.postman_collection.json](docs/LEVEL_UP_GAMER.postman_collection.json).

1. Postman → Import → seleccionar `docs/LEVEL_UP_GAMER.postman_collection.json`.
2. La colección está organizada por áreas: Autenticación, Usuarios, Productos, Blog, Órdenes.
3. Después de hacer `login` copia el `token` y pégalo en la variable `{{token}}` de la colección o usa el header `Authorization: Bearer {TOKEN}`.

## Tutorial para Frontend

- **Archivo:** [docs/Tutorial_conexión_FrontEnd.md](docs/Tutorial_conexión_FrontEnd.md) — contiene un `APIHelper` de ejemplo (implementación con `fetch`), instrucciones rápidas de uso, buenas prácticas de seguridad y ejemplos para llamadas autenticadas con `Authorization: Bearer {token}` o `X-API-Key`.
- **Para equipo de desarrollo:** importar la colección Postman en [docs/LEVEL_UP_GAMER.postman_collection.json](docs/LEVEL_UP_GAMER.postman_collection.json) y revisar [docs/Tutorial_conexión_FrontEnd.md](docs/Tutorial_conexión_FrontEnd.md) para un ejemplo práctico paso a paso sobre cómo autenticar y consumir los endpoints protegidos.

## Usar token para llamar endpoint protegido

## Usar API Key
## Colección Postman

Existe una colección lista para importar: [docs/LEVEL_UP_GAMER.postman_collection.json](docs/LEVEL_UP_GAMER.postman_collection.json).

1. Postman → Import → seleccionar `docs/LEVEL_UP_GAMER.postman_collection.json`.
2. La colección está organizada por áreas: Autenticación, Usuarios, Productos, Categorías, Regiones, Blog, Órdenes.
3. Después de hacer `login`, copia el `token` en la variable de colección `{{token}}` o usa el header `Authorization: Bearer {TOKEN}`.

## Tutorial para Frontend

- **Archivo:** [docs/Tutorial_conexión_FrontEnd.md](docs/Tutorial_conexión_FrontEnd.md) — contiene un `APIHelper` de ejemplo (implementación con `fetch`), instrucciones rápidas de uso, buenas prácticas de seguridad y ejemplos para llamadas autenticadas con `Authorization: Bearer {token}` o `X-API-Key`.
- **Documentación de entidades:** [docs/ENTIDADES_Y_ESTRUCTURAS.md](docs/ENTIDADES_Y_ESTRUCTURAS.md) — detalla la estructura de modelos, DTOs y relaciones entre entidades.

## Endpoints protegidos (requieren autenticación)

### Usuarios
- `GET /api/v1/usuarios` — Listar usuarios (requiere autenticación)
- `GET /api/v1/usuarios/{id}` — Ver usuario por ID
- `GET /api/v1/usuarios/email/{email}` — Buscar usuario por email
- `PUT /api/v1/usuarios/{id}` — Actualizar usuario
- `DELETE /api/v1/usuarios/{id}` — Eliminar usuario
- `POST /api/v1/usuarios/bulk` — **Crear múltiples usuarios** (requiere rol `ADMIN`, genera `apiKey` automáticamente)
- `POST /api/v1/usuarios` — **Crear usuario personalizado** (requiere rol `ADMIN`, genera `apiKey` automáticamente)

### Productos
- `POST /api/v1/productos` — Crear producto (requiere autenticación)
- `PUT /api/v1/productos/{id}` — Actualizar producto
- `DELETE /api/v1/productos/{id}` — Eliminar producto
- `POST /api/v1/productos/bulk` — Crear múltiples productos

### Categorías
- `POST /api/v1/categorias` — Crear categoría (requiere rol `ADMIN`)
- `POST /api/v1/categorias/bulk` — Crear múltiples categorías (requiere rol `ADMIN`)
- `PUT /api/v1/categorias/{id}` — Actualizar categoría (requiere rol `ADMIN`)
- `DELETE /api/v1/categorias/{id}` — Eliminar categoría (requiere rol `ADMIN`)

### Regiones
- `POST /api/v1/regiones` — Crear región (requiere rol `ADMIN`)
- `POST /api/v1/regiones/bulk` — Crear múltiples regiones (requiere rol `ADMIN`)
- `PUT /api/v1/regiones/{id}` — Actualizar región (requiere rol `ADMIN`)
- `DELETE /api/v1/regiones/{id}` — Eliminar región (requiere rol `ADMIN`)

### Blog
- `POST /api/v1/blog` — Crear artículo (requiere rol `ADMIN`)
- `POST /api/v1/blog/bulk` — Crear múltiples artículos (requiere rol `ADMIN`)
- `PUT /api/v1/blog/{id}` — Actualizar artículo (requiere rol `ADMIN`)
- `DELETE /api/v1/blog/{id}` — Eliminar artículo (requiere rol `ADMIN`)

### Órdenes
- `GET /api/v1/ordenes` — Listar órdenes (requiere rol `SELLER` o `ADMIN`)
- `GET /api/v1/ordenes/{id}` — Ver orden por ID (requiere rol `SELLER` o `ADMIN`)

## Usar token para llamar endpoint protegido

```bash
# Ejemplo: obtener usuarios autenticados
curl -X GET "http://localhost:8080/api/v1/usuarios" \
  -H "Authorization: Bearer {tu_token}"
```

## Usar API Key

```bash
# Ejemplo: obtener usuarios usando API Key
curl -X GET "http://localhost:8080/api/v1/usuarios" \
  -H "X-API-Key: {tu_api_key}"
```

## Funcionalidades principales

### ✅ Autenticación y Autorización
- JWT con claim de rol (`USER`, `ADMIN`, `SELLER`, `GUEST`)
- API Keys para autenticación máquina-a-máquina
- Roles y permisos configurables por endpoint
- Contraseñas encriptadas con BCrypt

### ✅ Gestión de Usuarios
- Registro e inicio de sesión
- Creación personalizada de usuarios por ADMIN (genera `apiKey` automáticamente)
- Creación en bulk de usuarios
- Búsqueda por email y RUT

### ✅ Productos y Categorías
- Productos con relación a `CategoriaProducto` (base de datos)
- Categorías con gestión completa (CRUD, bulk)
- Filtrado y búsqueda

### ✅ Regiones y Comunas
- Región con lista de comunas
- Endpoints para obtener comunas por región
- Búsqueda y validación de ciudades/comunas
- Endpoint bulk para crear múltiples regiones

### ✅ Blog
- Artículos con campos completos: `title`, `excerpt`, `content`, `categoria`, `author`, `fecha`, `readTime`, `image`, `gradient`, **`featured`**, **`likes`**, **`tags`**, **`views`**
- Incremento público de vistas y likes (sin autenticación)
- Creación, actualización y eliminación de artículos (requiere `ADMIN`)
- Artículos destacados

### ✅ Documentación OpenAPI/Swagger
- Accesible en: `http://localhost:8080/swagger-ui.html`
- Incluye esquemas `@Schema` y `@ApiResponse` en todos los controllers
- Seguridad documentada con `@SecurityRequirement`

El proyecto utiliza Maven y las dependencias más relevantes (nombres simplificados) son:

- **Spring Web** — REST endpoints
- **Spring Security** — autenticación y autorización
- **Spring Data JPA** — persistencia con ORM
- **SpringDoc OpenAPI / Swagger** — documentación interactiva
- **JJWT** — generación y validación de tokens JWT
- **Jakarta Validation** — validación de datos
- **BCrypt / Spring Security Crypto** — encriptación de contraseñas
- **Oracle JDBC** — driver para base de datos Oracle
- **Lombok** — reducción de boilerplate (getters, setters, etc.)
- **Spring Boot Test** — testing
- **Spring Security Test** — testing de seguridad

### Dependencias incluidas en `pom.xml`
Ejecuta `mvn dependency:tree` para ver la lista completa de dependencias transitivas.

## Notas sobre Java

- El proyecto está configurado para usar **Java 21** (`<java.version>21</java.version>` en `pom.xml`).
- Se recomienda compilar y ejecutar con JDK 21 para evitar incompatibilidades.
- Verifica tu versión de Java con: `java -version`

## Información adicional

### Configuración de Base de Datos
- El proyecto usa **Oracle Database** (configurado en `application.properties`).
- Se incluyen archivos de wallet para conexión segura en `BackEnd/src/main/resources/wallet/`.

### Ejecución con Maven Wrapper
Si no tienes Maven instalado, usa el wrapper incluido:

```powershell
# Windows
.\mvnw clean install
.\mvnw spring-boot:run

# Linux/Mac
./mvnw clean install
./mvnw spring-boot:run
```

### Generar documentación Swagger
La documentación OpenAPI se genera automáticamente y está disponible en:
- **UI Swagger:** `http://localhost:8080/docs`
- **JSON OpenAPI:** `http://localhost:8080/v1/api-docs`

### Estructura de carpetas
```
BackEnd/
├── src/main/java/com/level_up_gamer/BackEnd/
│   ├── Config/              # Configuraciones (Security, OpenAPI)
│   ├── Controller/          # REST Controllers
│   ├── Service/             # Lógica de negocio
│   ├── Repository/          # Acceso a datos (JPA)
│   ├── Model/               # Entidades JPA
│   ├── DTO/                 # Data Transfer Objects
│   ├── Exception/           # Excepciones personalizadas
│   ├── Security/            # JWT, Auth filters, Password encryption
│   └── Validation/          # Validadores personalizados
├── src/main/resources/
│   ├── application.properties
│   └── wallet/              # Oracle wallet files
├── pom.xml
└── target/                  # Compilados (después de build)
```


