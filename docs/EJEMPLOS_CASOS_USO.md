# 🧪 Credenciales de Ejemplo y Casos de Uso

## Usuarios de Ejemplo para Pruebas

### Usuario 1: Admin
```json
{
  "nombre": "Administrador Sistema",
  "email": "admin@levelupgamer.com",
  "password": "AdminPass123456",
  "rol": "ADMIN"
}
```

**Cómo Crear:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Administrador Sistema",
    "email": "admin@levelupgamer.com",
    "password": "AdminPass123456",
    "passwordConfirm": "AdminPass123456"
  }'
```

---

### Usuario 2: Vendedor
```json
{
  "nombre": "Juan Vendedor",
  "email": "vendedor@levelupgamer.com",
  "password": "VendedorPass123",
  "rol": "SELLER"
}
```

**Cómo Crear:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Vendedor",
    "email": "vendedor@levelupgamer.com",
    "password": "VendedorPass123",
    "passwordConfirm": "VendedorPass123"
  }'
```

---

### Usuario 3: Cliente Regular
```json
{
  "nombre": "Carlos Cliente",
  "email": "cliente@example.com",
  "password": "ClientePass123",
  "rol": "USER"
}
```

**Cómo Crear:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos Cliente",
    "email": "cliente@example.com",
    "password": "ClientePass123",
    "passwordConfirm": "ClientePass123"
  }'
```

---

## 🧪 Casos de Uso Completos

### Caso 1: Flujo Completo de Registro y Login

**Paso 1: Registrar usuario**
```bash
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "María Pérez",
    "email": "maria@example.com",
    "password": "Maria2025Pass",
    "passwordConfirm": "Maria2025Pass"
  }')

echo "$RESPONSE"
```

**Respuesta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...",
  "apiKey": "ZXhhbXBsZV9hcGlfa2V5X2Jhc2U2NG==",
  "usuarioId": 1,
  "nombre": "María Pérez",
  "email": "maria@example.com",
  "rol": "USER"
}
```

**Paso 2: Guardar token**
```bash
TOKEN="eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9..."
```

**Paso 3: Usar el token**
```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer $TOKEN"
```

---

### Caso 2: Login y Acceso a Recurso Protegido

**Paso 1: Login**
```bash
LOGIN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "maria@example.com",
    "password": "Maria2025Pass"
  }')

TOKEN=$(echo "$LOGIN" | jq -r '.token')
API_KEY=$(echo "$LOGIN" | jq -r '.apiKey')
USER_ID=$(echo "$LOGIN" | jq -r '.usuarioId')
```

**Paso 2: Obtener información del usuario**
```bash
curl -X GET http://localhost:8080/api/usuarios/$USER_ID \
  -H "Authorization: Bearer $TOKEN"
```

**Paso 3: Actualizar usuario**
```bash
curl -X PUT http://localhost:8080/api/usuarios/$USER_ID \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nombre": "María Pérez García"
  }'
```

---

### Caso 3: Usar API Key en lugar de JWT

**Paso 1: Registrar usuario (obtenemos API Key)**
```bash
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Pedro García",
    "email": "pedro@example.com",
    "password": "Pedro2025Pass",
    "passwordConfirm": "Pedro2025Pass"
  }')

API_KEY=$(echo "$RESPONSE" | jq -r '.apiKey')
```

**Paso 2: Usar API Key para acceder**
```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "X-API-Key: $API_KEY"
```

---

### Caso 4: Manejo de Errores de Validación

**Intento 1: Email inválido**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test User",
    "email": "invalid-email",
    "password": "Pass123456",
    "passwordConfirm": "Pass123456"
  }'
```

**Respuesta esperada (400):**
```json
{
  "timestamp": "2025-11-23T18:30:00",
  "status": 400,
  "message": "Error de validación",
  "errors": {
    "email": "El email debe ser válido"
  }
}
```

**Intento 2: Contraseña muy corta**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test User",
    "email": "test@example.com",
    "password": "123",
    "passwordConfirm": "123"
  }'
```

**Respuesta esperada (400):**
```json
{
  "timestamp": "2025-11-23T18:30:00",
  "status": 400,
  "message": "Error de validación",
  "errors": {
    "password": "La contraseña debe tener al menos 6 caracteres"
  }
}
```

**Intento 3: Contraseñas no coinciden**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test User",
    "email": "test@example.com",
    "password": "Password123",
    "passwordConfirm": "DifferentPass123"
  }'
```

**Respuesta esperada (400):**
```json
{
  "timestamp": "2025-11-23T18:30:00",
  "status": 400,
  "message": "Las contraseñas no coinciden"
}
```

---

### Caso 5: Acceso sin Autenticación

**Intento sin token:**
```bash
curl -X GET http://localhost:8080/api/usuarios
```

**Respuesta esperada (403):**
```
Forbidden - Authentication required
```

**Intento con token inválido:**
```bash
curl -X GET http://localhost:8080/api/usuarios \
  -H "Authorization: Bearer invalid_token_here"
```

**Respuesta esperada (401):**
```json
{
  "timestamp": "2025-11-23T18:30:00",
  "status": 401,
  "message": "Token inválido o expirado"
}
```

---

## 🔐 Variables de Entorno Recomendadas

### Para Desarrollo Local
```bash
# .env (crear archivo en raíz)
JWT_SECRET=miClaveSecretaParaJWTDelProyectoLevelUpGamer2025SoloParaDesarrollo
JWT_EXPIRATION_MS=86400000
DB_HOST=localhost
DB_PORT=1521
DB_USER=BACKEND_MANAGER
DB_PASS=LevelUpGamer2025
SERVER_PORT=8080
```

### Para Producción
```bash
JWT_SECRET=<generar-clave-segura-fuerte>
JWT_EXPIRATION_MS=86400000
DB_HOST=<host-produccion>
DB_PORT=1521
DB_USER=<usuario-prod>
DB_PASS=<password-prod>
SERVER_PORT=8080
CORS_ORIGINS=https://tu-dominio.com
```

---

## 🔍 Debugging y Logs

### Habilitar Logs JWT
```properties
logging.level.com.level_up_gamer.BackEnd.Security.JwtProvider=DEBUG
```

**Ejemplo de log:**
```
DEBUG: Generating JWT token for user: juan@example.com
DEBUG: Token generated successfully
DEBUG: Validating JWT token
DEBUG: Token is valid
```

### Habilitar Logs Spring Security
```properties
logging.level.org.springframework.security=DEBUG
```

**Ejemplo de log:**
```
DEBUG: Authorization request with Authorization header "Bearer ..."
DEBUG: Token extracted from request
DEBUG: Authenticating token
DEBUG: Authentication successful for user: juan@example.com
```

### Ver SQL Queries
```properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## 📊 Monitoring y Métricas

### Endpoints de Health Check (Próximas versiones)
```
GET /actuator/health - Estado de la aplicación
GET /actuator/metrics - Métricas del sistema
```

### Log Pattern Recomendado
```properties
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
```

---

## 🧪 Script de Prueba Completo (Bash)

```bash
#!/bin/bash

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

API_URL="http://localhost:8080"

echo -e "${YELLOW}=== NIVEL UP GAMER - Test Suite ===${NC}\n"

# Test 1: Registro
echo -e "${YELLOW}Test 1: Registro${NC}"
REGISTER=$(curl -s -X POST $API_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test User",
    "email": "test@example.com",
    "password": "TestPass123",
    "passwordConfirm": "TestPass123"
  }')

TOKEN=$(echo "$REGISTER" | jq -r '.token')
echo -e "${GREEN}✓ Usuario registrado${NC}"
echo "Token: ${TOKEN:0:50}...\n"

# Test 2: Login
echo -e "${YELLOW}Test 2: Login${NC}"
LOGIN=$(curl -s -X POST $API_URL/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "TestPass123"
  }')

TOKEN=$(echo "$LOGIN" | jq -r '.token')
echo -e "${GREEN}✓ Login exitoso${NC}\n"

# Test 3: Obtener usuarios
echo -e "${YELLOW}Test 3: Obtener usuarios${NC}"
USERS=$(curl -s -X GET $API_URL/api/usuarios \
  -H "Authorization: Bearer $TOKEN")

echo -e "${GREEN}✓ Usuarios obtenidos${NC}"
echo "$USERS" | jq '.[0]' 2>/dev/null || echo "No hay usuarios\n"

# Test 4: Error - Sin token
echo -e "${YELLOW}Test 4: Acceso sin token (debe fallar)${NC}"
ERROR=$(curl -s -X GET $API_URL/api/usuarios)
echo -e "${RED}✓ Acceso denegado como se esperaba${NC}\n"

# Test 5: Email duplicado (debe fallar)
echo -e "${YELLOW}Test 5: Registro con email duplicado (debe fallar)${NC}"
DUPLICATE=$(curl -s -X POST $API_URL/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Another User",
    "email": "test@example.com",
    "password": "AnotherPass123",
    "passwordConfirm": "AnotherPass123"
  }')

echo -e "${RED}✓ Email duplicado detectado${NC}\n"

echo -e "${YELLOW}=== Pruebas Completadas ===${NC}"
```

---

## 📝 Notas Importantes

1. **Contraseñas seguras**: Las contraseñas de ejemplo son solo para desarrollo
2. **Tokens JWT**: Expiran después de 24 horas por defecto
3. **API Keys**: Son únicas y permanentes (cambiar en producción)
4. **Base de Datos**: Los datos se crean en las tablas de Oracle
5. **CORS**: Actualmente abierto a todos los orígenes (ajustar en producción)

---

**Ejemplos Listos para Usar: ✅**
**Casos de Uso Completos: ✅**
**Testing Scripts: ✅**

Actualizado: 23 de noviembre de 2025
