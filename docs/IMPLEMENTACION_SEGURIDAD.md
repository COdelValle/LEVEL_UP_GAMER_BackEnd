# 🔐 Implementación de Seguridad y Validaciones - LEVEL UP GAMER BackEnd

## Resumen de Implementaciones

Se han implementado exitosamente las siguientes características de seguridad y validación:

### ✅ 1. **Validaciones de Campos**

#### DTOs con Validaciones Completas:
- **LoginRequest**: Validaciones de email y contraseña
- **RegisterRequest**: Validaciones de nombre, email, contraseña y confirmación
- **CreateProductoRequest**: Validaciones para todos los campos de producto
- **CreateOrdenRequest**: Validaciones completas para órdenes y envíos
- **OrdenItemRequest**: Validaciones para items de órdenes
- **CreateBlogRequest**: Validaciones para posts de blog
- **UpdateUsuarioRequest**: Validaciones para actualizar perfil

**Validadores Utilizados:**
- `@NotBlank`, `@NotNull`, `@NotEmpty`: Campos requeridos
- `@Size`: Validación de longitud
- `@Email`: Validación de emails
- `@Min`, `@Max`, `@Positive`: Validación de números
- `@Pattern`: Validación con expresiones regulares (teléfono, código postal)

#### Modelo Usuario Actualizado:
```java
- Nombre: @NotBlank, @Size(min=2, max=100)
- Email: @NotBlank, @Email, unique
- Contraseña: @NotBlank, @Size(min=6)
- ApiKey: Campo único para autenticación por API
```

---

### 🔐 2. **Cifrado de Contraseñas**

**Clase: `PasswordEncrypter`**

Implementación usando BCrypt:
- `encryptPassword(String password)`: Encripta contraseñas de manera segura
- `matches(String rawPassword, String encodedPassword)`: Verifica contraseñas
- `generateApiKey()`: Genera claves API únicas y seguras

**Dependencias Utilizadas:**
- `org.springframework.security:spring-security-crypto`
- `BCryptPasswordEncoder`: Estándar de encriptación industrial

---

### 🔑 3. **API Key**

**Características:**
- Generación automática en el registro
- Almacenada en la base de datos (campo único)
- Puede ser utilizada como alternativa a JWT para autenticación
- Validable mediante el método `getUsuarioByApiKey()`

**Caso de Uso:**
```
Header: X-API-Key: <generated-api-key>
```

---

### 🎫 4. **JWT (JSON Web Tokens)**

**Clase: `JwtProvider`**

Funcionalidades:
- `generateToken(usuarioId, email)`: Genera token JWT
- `generateToken(usuarioId, email, rol)`: Genera token con información adicional
- `getEmailFromToken(token)`: Extrae email del token
- `getUsuarioIdFromToken(token)`: Extrae ID del token
- `validateToken(token)`: Valida la integridad del token

**Configuración:**
- Algoritmo: HS512
- Secreto: `app.jwtSecret` (configurable)
- Expiración: 24 horas (configurable: `app.jwtExpirationMs`)
- Versión: JJWT 0.12.3 (compatible con Java 21)

**Ubicación en Application.properties:**
```properties
app.jwtSecret=miClaveSecretaParaJWTDelProyectoLevelUpGamer2025SoloParaDesarrollo
app.jwtExpirationMs=86400000
```

---

### 📝 5. **Login y Autenticación**

**Clase: `AuthController`**

Endpoints Implementados:

#### 1. **Registro de Usuario**
```
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "Password123",
  "passwordConfirm": "Password123"
}

Response (201 Created):
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "apiKey": "base64-encoded-api-key",
  "usuarioId": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "rol": "USER"
}
```

#### 2. **Login**
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "Password123"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "apiKey": "base64-encoded-api-key",
  "usuarioId": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "rol": "USER"
}
```

#### 3. **Validar Token**
```
POST /api/auth/validate
Authorization: Bearer <token>

Response (200 OK):
{
  "valid": true,
  "message": "Token válido"
}
```

**Características del Login:**
- ✅ Validación de credenciales
- ✅ Generación automática de JWT
- ✅ Generación automática de API Key (si no existe)
- ✅ Actualización de `ultimoAcceso`
- ✅ Manejo de errores con mensajes claros

---

### 🔒 6. **Seguridad Spring Security**

**Clase: `SecurityConfig`**

Configuración:
- CSRF deshabilitado (API REST)
- Sesiones con `STATELESS` (sin cookies)
- Rutas públicas: `/api/auth/**`, `/swagger-ui/**`, `/v3/api-docs/**`
- Rutas protegidas: Todas las demás requieren autenticación

**Filtro JWT: `JwtAuthenticationFilter`**

- Intercepta todas las peticiones
- Valida tokens en header `Authorization: Bearer <token>`
- Soporta API Keys en header `X-API-Key`
- Establece contexto de seguridad automáticamente

---

### ⚠️ 7. **Manejo Global de Excepciones**

**Clase: `GlobalExceptionHandler`**

Excepciones Manejadas:
- `MethodArgumentNotValidException`: Errores de validación
- `ResourceNotFoundException`: Recurso no encontrado
- `ConflictException`: Conflictos (ej: email duplicado)
- `Exception`: Excepciones genéricas

Respuesta Estandarizada:
```json
{
  "timestamp": "2025-11-23T18:00:00",
  "status": 400,
  "message": "Mensaje de error",
  "errors": {
    "email": "El email debe ser válido",
    "password": "La contraseña debe tener al menos 6 caracteres"
  }
}
```

---

## 📦 Dependencias Agregadas

```xml
<!-- JWT -->
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.12.3</version>
</dependency>

<!-- Validación -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Seguridad -->
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-crypto</artifactId>
</dependency>
```

---

## 🚀 Uso en Controladores

### Ejemplo: Proteger un Endpoint

```java
@GetMapping("/perfil")
public ResponseEntity<?> getPerfil(@RequestHeader("Authorization") String token) {
    try {
        Usuario usuario = usuarioService.getUsuarioFromToken(token.substring(7));
        return ResponseEntity.ok(usuario);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse(e.getMessage()));
    }
}
```

### Ejemplo: Usar API Key

```java
@GetMapping("/datos-privados")
public ResponseEntity<?> getDatos(@RequestHeader("X-API-Key") String apiKey) {
    try {
        Usuario usuario = usuarioService.getUsuarioByApiKey(apiKey);
        return ResponseEntity.ok(usuario);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
```

---

## 🧪 Pruebas Recomendadas

### 1. Registro
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test User",
    "email": "test@example.com",
    "password": "Test123456",
    "passwordConfirm": "Test123456"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Test123456"
  }'
```

### 3. Acceder a Endpoint Protegido
```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer <token>"
```

---

## 📋 Checklist de Implementaciones

- [x] Validaciones en todos los DTOs
- [x] Cifrado de contraseñas con BCrypt
- [x] Generación y almacenamiento de API Keys
- [x] JWT (tokens) con expiración
- [x] Endpoint de registro con validaciones
- [x] Endpoint de login con autenticación
- [x] Endpoint de validación de tokens
- [x] Filtro de autenticación JWT
- [x] Configuración de seguridad Spring
- [x] Manejo global de excepciones
- [x] DTOs para todos los modelos principales
- [x] Actualización del modelo Usuario
- [x] Actualización del repositorio Usuario

---

## 🔧 Próximos Pasos Sugeridos

1. **Implementar Roles y Permisos**
   - Usar `@PreAuthorize` para proteger endpoints específicos
   - Crear servicio de autorización

2. **Refresh Tokens**
   - Implementar tokens de refresco con expiración más larga
   - Endpoint para refrescar tokens expirados

3. **Rate Limiting**
   - Limitar intentos de login
   - Proteger contra brute force

4. **Auditoría**
   - Registrar cambios importantes
   - Rastrear acceso de usuarios

5. **CORS Configurado**
   - Ajustar origins permitidos según necesidad
   - Configurar métodos y headers permitidos

---

**Estado: ✅ COMPLETO**
**Fecha: 23 de noviembre de 2025**
**Proyecto: LEVEL UP GAMER - BackEnd**
