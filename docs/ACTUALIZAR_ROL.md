# ✅ ACTUALIZAR USUARIO EN POSTMAN (Nombre, Email, Contraseña, Rol)

## 📋 Pasos Rápidos

### 1. Abre Postman
Ve a la colección **LEVEL UP GAMER - Backend API** → **👥 Usuarios** → **Actualizar usuario (nombre y/o rol)**

### 2. En el Body (JSON) - Elige qué actualizar

**Solo nombre:**
```json
{
  "nombre": "Nuevo Nombre"
}
```

**Solo contraseña (recuperación):**
```json
{
  "password": "NuevaPassword123!",
  "passwordConfirm": "NuevaPassword123!"
}
```

**Solo email:**
```json
{
  "email": "newemail@example.com"
}
```

**Solo rol:**
```json
{
  "rol": "ADMIN"
}
```

**Actualizar todo a la vez:**
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "NuevaPassword123!",
  "passwordConfirm": "NuevaPassword123!",
  "rol": "ADMIN"
}
```

### 3. Headers (verifica que estén configurados)
```
Authorization: Bearer {{token}}
Content-Type: application/json
```

### 4. URL
```
PUT http://localhost:8080/api/v1/usuarios/1
```
**Cambia el 1 por el ID del usuario que quieres actualizar.**

### 5. Haz clic en Send ✅

### 4. URL
```
PUT http://localhost:8080/api/v1/usuarios/1
```
**Cambia el 1 por el ID del usuario que quieres actualizar.**

### 5. Haz clic en Send ✅

---

## 🎯 Ejemplo Completo en Postman

```
METHOD: PUT
URL: http://localhost:8080/api/v1/usuarios/1

HEADERS:
  Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFu...
  Content-Type: application/json

BODY (raw, JSON):
{
  "nombre": "Juan Pérez",
  "rol": "ADMIN"
}
```

**Respuesta (200 OK):**
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "$2a$10$...",
  "apiKey": "abc123xyz...",
  "rol": "ADMIN",
  "puntos": 0,
  "fechaCreacion": "2025-11-26T10:00:00",
  "ultimoAcceso": "2025-11-26T10:30:00"
}
```

---

## 🔑 ROLES DISPONIBLES

| Rol | Descripción |
|-----|-------------|
| **USER** | Usuario básico - acceso limitado |
| **ADMIN** | Administrador - acceso total |
| **SELLER** | Vendedor - puede crear/editar productos |
| **GUEST** | Usuario invitado - acceso mínimo |

---

## 📝 OPCIONES DE REQUEST

### Solo actualizar nombre
```json
{
  "nombre": "Nuevo Nombre"
}
```

### Solo actualizar rol
```json
{
  "rol": "SELLER"
}
```

### Actualizar ambos
```json
{
  "nombre": "Nuevo Nombre",
  "rol": "ADMIN"
}
```

---

## ⚠️ ERRORES COMUNES

| Error | Causa | Solución |
|-------|-------|----------|
| 401 Unauthorized | Token falta o expiró | Haz login de nuevo y copia el token |
| 404 Not Found | Usuario no existe | Verifica que el ID sea correcto |
| 400 Bad Request | Rol inválido | Usa: USER, ADMIN, SELLER o GUEST |
| 400 Bad Request | JSON mal formado | Verifica la sintaxis del JSON |

---

## 🛠️ EN POWERSHELL

### Cambiar solo contraseña
```powershell
$body = @{
    password = "NuevaPassword123!"
    passwordConfirm = "NuevaPassword123!"
} | ConvertTo-Json

Invoke-RestMethod -Method Put `
  -Uri "http://localhost:8080/api/v1/usuarios/1" `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

### Cambiar solo email
```powershell
$body = @{
    email = "newemail@example.com"
} | ConvertTo-Json

Invoke-RestMethod -Method Put `
  -Uri "http://localhost:8080/api/v1/usuarios/1" `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

### Cambiar solo rol
```powershell
$body = @{
    rol = "SELLER"
} | ConvertTo-Json

Invoke-RestMethod -Method Put `
  -Uri "http://localhost:8080/api/v1/usuarios/1" `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

### Actualizar múltiples campos
```powershell
$body = @{
    nombre = "Nuevo Nombre"
    email = "nuevo@example.com"
    password = "NuevaPassword123!"
    passwordConfirm = "NuevaPassword123!"
    rol = "ADMIN"
} | ConvertTo-Json

Invoke-RestMethod -Method Put `
  -Uri "http://localhost:8080/api/v1/usuarios/1" `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

---

## ✨ CON CURL

```bash
# 1. Login para obtener token
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"juan@example.com","password":"Password123!"}'

# Guardar el TOKEN de la respuesta

# 2. Actualizar rol
curl -X PUT http://localhost:8080/api/v1/usuarios/1 \
  -H "Authorization: Bearer TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Pérez","rol":"ADMIN"}'
```

---

## 📌 CHECKLIST ANTES DE ENVIAR

- ✅ Token en el header `Authorization: Bearer {{token}}`
- ✅ URL correcta: `/api/v1/usuarios/{ID}`
- ✅ Método: **PUT**
- ✅ Body en JSON con campos válidos
- ✅ Rol válido: USER, ADMIN, SELLER, GUEST
- ✅ Content-Type: `application/json`

¡Listo! Ya sabes cómo actualizar el rol de un usuario en Postman. 🎉
