# 🧪 Guía de Pruebas - LEVEL UP GAMER BackEnd

## Configuración Rápida

### 1. **Configuración en application.properties**

```properties
# JWT Configuration
app.jwtSecret=miClaveSecretaParaJWTDelProyectoLevelUpGamer2025SoloParaDesarrollo
app.jwtExpirationMs=86400000  # 24 horas

# Server
server.port=8080
server.servlet.context-path=/
```

### 2. **Iniciar la Aplicación**

```bash
cd BackEnd
./mvnw.cmd spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

---

## Endpoints Disponibles

### 🔓 **Públicos (Sin Autenticación)**

```
POST   /api/auth/register      - Registrar nuevo usuario
POST   /api/auth/login         - Login y obtener token
POST   /api/auth/validate      - Validar token
```

### 🔒 **Protegidos (Requieren Token)**

```
GET    /api/usuarios           - Listar todos los usuarios
GET    /api/usuarios/{id}      - Obtener usuario por ID
GET    /api/usuarios/email/{email} - Obtener usuario por email
PUT    /api/usuarios/{id}      - Actualizar usuario
DELETE /api/usuarios/{id}      - Eliminar usuario
```

---

## 📚 Ejemplos de Pruebas con cURL

### 1. **Registro de Usuario**

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "password": "Password123456",
    "passwordConfirm": "Password123456"
  }'
```

**Respuesta Exitosa (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "apiKey": "ZXhhbXBsZV9hcGlfa2V5X2Jhc2U2NG==",
  "usuarioId": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "rol": "USER"
}
```

---

### 2. **Login**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "Password123456"
  }'
```

**Respuesta Exitosa (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "apiKey": "ZXhhbXBsZV9hcGlfa2V5X2Jhc2U2NG==",
  "usuarioId": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "rol": "USER"
}
```

---

### 3. **Obtener Todos los Usuarios (Protegido)**

```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
```

O usando API Key:

```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "X-API-Key: ZXhhbXBsZV9hcGlfa2V5X2Jhc2U2NG=="
```

---

### 4. **Obtener Usuario por ID (Protegido)**

```bash
curl -X GET http://localhost:8080/api/usuarios/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
```

---

### 5. **Actualizar Usuario (Protegido)**

```bash
curl -X PUT http://localhost:8080/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..." \
  -d '{
    "nombre": "Juan Pérez Actualizado"
  }'
```

---

### 6. **Eliminar Usuario (Protegido)**

```bash
curl -X DELETE http://localhost:8080/api/usuarios/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
```

---

### 7. **Validar Token**

```bash
curl -X POST http://localhost:8080/api/auth/validate \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
```

---

## 🧪 Pruebas de Validación

### Registro con Email Inválido

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "email": "invalid-email",
    "password": "Pass123",
    "passwordConfirm": "Pass123"
  }'
```

**Respuesta (400 Bad Request):**
```json
{
  "timestamp": "2025-11-23T18:00:00",
  "status": 400,
  "message": "Error de validación",
  "errors": {
    "email": "El email debe ser válido"
  }
}
```

---

### Registro con Contraseña Muy Corta

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "email": "test@example.com",
    "password": "123",
    "passwordConfirm": "123"
  }'
```

**Respuesta (400 Bad Request):**
```json
{
  "timestamp": "2025-11-23T18:00:00",
  "status": 400,
  "message": "Error de validación",
  "errors": {
    "password": "La contraseña debe tener al menos 6 caracteres"
  }
}
```

---

### Registro con Contraseñas Diferentes

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "email": "test@example.com",
    "password": "Password123",
    "passwordConfirm": "DifferentPassword"
  }'
```

**Respuesta (400 Bad Request):**
```json
{
  "timestamp": "2025-11-23T18:00:00",
  "status": 400,
  "message": "Las contraseñas no coinciden"
}
```

---

### Login con Email No Existente

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "noexiste@example.com",
    "password": "Password123"
  }'
```

**Respuesta (401 Unauthorized):**
```json
{
  "timestamp": "2025-11-23T18:00:00",
  "status": 401,
  "message": "Usuario no encontrado"
}
```

---

### Login con Contraseña Incorrecta

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "PasswordIncorrecto"
  }'
```

**Respuesta (401 Unauthorized):**
```json
{
  "timestamp": "2025-11-23T18:00:00",
  "status": 401,
  "message": "Contraseña incorrecta"
}
```

---

### Acceso sin Token

```bash
curl -X GET http://localhost:8080/api/usuarios
```

**Respuesta (403 Forbidden):**
Token requerido para acceder a este recurso.

---

### Token Expirado o Inválido

```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer invalid_token_here"
```

**Respuesta (401 Unauthorized):**
Token inválido o expirado.

---

## 🔧 Herramientas Recomendadas para Pruebas

### Postman
1. Descargar [Postman](https://www.postman.com/downloads/)
2. Importar la colección incluida en `docs/Postman_Collection.json`
3. Ejecutar las pruebas preconfiguradas

### Thunder Client (VS Code)
1. Instalar extensión: `Thunder Client`
2. Importar colección desde `docs/Thunder_Collection.json`

### Insomnia
1. Descargar [Insomnia](https://insomnia.rest/)
2. Importar colección: `docs/Insomnia_Collection.json`

### CURL en Terminal
```bash
# Guardar token en variable
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"juan@example.com","password":"Password123"}' | jq -r '.token')

# Usar el token en siguiente petición
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer $TOKEN"
```

---

## 📊 Test de Carga

### Instalar Apache Bench
```bash
# Windows (con chocolatey)
choco install apache-bench

# Linux
sudo apt-get install apache2-utils
```

### Realizar Pruebas
```bash
# 1000 requests, 10 concurrentes
ab -n 1000 -c 10 -H "Authorization: Bearer <token>" \
  http://localhost:8080/api/usuarios
```

---

## 🔍 Debugging

### Habilitar Logs JWT
```properties
logging.level.com.level_up_gamer.BackEnd.Security=DEBUG
```

### Habilitar Logs Spring Security
```properties
logging.level.org.springframework.security=DEBUG
```

### Ver Stack Trace Completo
```properties
logging.level.root=TRACE
```

---

## ✅ Checklist de Pruebas

- [ ] Registro con datos válidos
- [ ] Registro con email duplicado
- [ ] Registro con contraseñas diferentes
- [ ] Login exitoso
- [ ] Login con contraseña incorrecta
- [ ] Login con email no existente
- [ ] Obtener token y acceder a recursos protegidos
- [ ] Validar token válido
- [ ] Validar token inválido
- [ ] Acceso sin token
- [ ] Token expirado
- [ ] API Key funciona como alternativa
- [ ] Actualizar usuario
- [ ] Eliminar usuario
- [ ] Validaciones de campos (email, contraseña)
- [ ] Manejo de excepciones global

---

## 📝 Notas Importantes

1. **Contraseña de Desarrollo**
   - Para pruebas locales usar: `Password123456`
   - En producción, cambiar `app.jwtSecret` a valor fuerte

2. **Expiración de Tokens**
   - Por defecto: 24 horas
   - Modificable en `application.properties`

3. **API Key**
   - Se genera automáticamente en registro
   - Es única por usuario
   - Puede ser usada en lugar de JWT

4. **Base de Datos**
   - Usar Oracle Database 21c o superior
   - Script DDL incluido en `docs/schema.sql`

5. **CORS**
   - Actualmente permite todos los orígenes
   - En producción, especificar orígenes permitidos

---

## 🚀 Deployment

### Local
```bash
./mvnw spring-boot:run
```

### Producción
```bash
./mvnw clean package
java -jar target/BackEnd-0.0.1-SNAPSHOT.jar
```

### Docker
```bash
docker build -t level-up-gamer:latest .
docker run -p 8080:8080 level-up-gamer:latest
```

---

**Estado: ✅ LISTO PARA PRUEBAS**
**Última Actualización: 23 de noviembre de 2025**
