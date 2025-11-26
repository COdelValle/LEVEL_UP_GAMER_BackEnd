# 📖 GUÍA COMPLETA DE ENDPOINTS - LEVEL UP GAMER

## 🚀 Inicio Rápido

### 1. URL Base
```
http://localhost:8080
```

### 2. Primero: Registrate
```http
POST /api/v1/auth/register HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "Password123!",
  "passwordConfirm": "Password123!"
}
```

**Respuesta (201 Created):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQGV4YW1wbGUuY29tIiwi...",
  "apiKey": "abc123xyz789...",
  "usuarioId": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "rol": "USER"
}
```

**⚠️ Importante:** Guarda el `token` que recibes. Lo usarás en TODOS los demás endpoints.

### 3. Usa el token en todos los requests
En el header `Authorization` de cada request (excepto auth):
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuQGV4YW1wbGUuY29tIiwi...
```

---

## 🔐 AUTENTICACIÓN

### Registrar usuario
```http
POST /api/v1/auth/register HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "Password123!",
  "passwordConfirm": "Password123!"
}
```

**Status:** 201 Created  
**Validaciones:**
- nombre: 2-100 caracteres
- email: formato válido y único
- password: 6-100 caracteres
- passwordConfirm: debe coincidir exactamente

---

### Login
```http
POST /api/v1/auth/login HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "Password123!"
}
```

**Status:** 200 OK  
**Respuesta:**
```json
{
  "token": "JWT...",
  "apiKey": "API_KEY...",
  "usuarioId": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "rol": "USER"
}
```

---

### Validar token
```http
POST /api/v1/auth/validate HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI

(sin body)
```

**Status:** 200 OK si es válido, 401 si expiró

---

## 👥 USUARIOS

### Obtener todos los usuarios
```http
GET /api/v1/usuarios HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

**Status:** 200 OK  
**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "rol": "USER",
    "puntos": 0,
    "fechaCreacion": "2025-11-26T10:00:00"
  }
]
```

---

### Obtener usuario por ID
```http
GET /api/v1/usuarios/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

---

### Obtener usuario por email
```http
GET /api/v1/usuarios/email/juan@example.com HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

---

### Actualizar usuario
```http
PUT /api/v1/usuarios/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado",
  "email": "newemail@example.com",
  "password": "NuevaPassword123!",
  "passwordConfirm": "NuevaPassword123!",
  "rol": "ADMIN"
}
```

**Status:** 200 OK

**Campos actualizables (todos opcionales):**
- `nombre`: nuevo nombre (2-100 caracteres)
- `email`: nuevo email (debe ser único)
- `password`: nueva contraseña (6-100 caracteres, requiere passwordConfirm)
- `passwordConfirm`: confirmación de contraseña (debe coincidir exactamente)
- `rol`: nuevo rol (USER, ADMIN, SELLER, GUEST)

**Nota:** Solo proporciona los campos que deseas actualizar. Todos son opcionales.

---

### Eliminar usuario
```http
DELETE /api/v1/usuarios/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

**Status:** 200 OK  
**Respuesta:**
```json
{
  "message": "Usuario eliminado correctamente"
}
```

---

## 🎮 PRODUCTOS

### Obtener todos los productos
```http
GET /api/v1/productos HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

---

### Obtener producto por ID
```http
GET /api/v1/productos/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

---

### Crear producto (ADMIN o SELLER)
```http
POST /api/v1/productos HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
Content-Type: application/json

{
  "nombre": "PS5 Controller",
  "precio": 59.99,
  "categoria": "ACCESORIOS",
  "stock": 50,
  "descripcion": "Control inalámbrico para PlayStation 5",
  "imagen": "https://example.com/ps5-controller.jpg",
  "destacado": true,
  "nuevo": true
}
```

**Status:** 201 Created  
**Requiere:** Rol ADMIN o SELLER

---

### Actualizar producto (ADMIN o SELLER)
```http
PUT /api/v1/productos/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
Content-Type: application/json

{
  "nombre": "PS5 Controller - Mejorado",
  "precio": 64.99,
  "categoria": "ACCESORIOS",
  "stock": 45,
  "descripcion": "Control inalámbrico mejorado",
  "imagen": "https://example.com/ps5-controller.jpg",
  "destacado": true,
  "nuevo": false
}
```

**Status:** 200 OK

---

### Eliminar producto (ADMIN)
```http
DELETE /api/v1/productos/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

**Status:** 200 OK  
**Requiere:** Rol ADMIN

---

## 📰 BLOG

### Obtener todos los artículos (sin autenticación)
```http
GET /api/v1/blog HTTP/1.1
Host: localhost:8080
```

**Status:** 200 OK  
**Nota:** Este endpoint no requiere token

---

### Obtener artículo por ID
```http
GET /api/v1/blog/1 HTTP/1.1
Host: localhost:8080
```

---

### Obtener artículos por autor
```http
GET /api/v1/blog/autor/Juan García HTTP/1.1
Host: localhost:8080
```

---

### Obtener artículos destacados
```http
GET /api/v1/blog/destacados HTTP/1.1
Host: localhost:8080
```

---

### Crear artículo (ADMIN o EDITOR)
```http
POST /api/v1/blog HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
Content-Type: application/json

{
  "title": "Guía completa de PlayStation 5",
  "excerpt": "Descubre las mejores características y juegos para PS5",
  "content": "Este es el contenido completo del artículo. Debe tener al menos 100 caracteres y contiene toda la información detallada sobre PlayStation 5 y sus características únicas.",
  "categoria": "Gaming",
  "author": "Juan García",
  "image": "https://example.com/ps5.jpg",
  "gradient": "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
}
```

**Status:** 201 Created  
**Requiere:** Rol ADMIN o EDITOR  
**Categorías:** Gaming, Reviews, Tutoriales, Noticias, Competitivo  
**Validaciones:**
- title: 10-200 caracteres
- excerpt: 50-500 caracteres
- content: mínimo 100 caracteres

---

### Actualizar artículo (ADMIN o EDITOR)
```http
PUT /api/v1/blog/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
Content-Type: application/json

{
  "title": "Guía actualizada",
  "excerpt": "Nueva descripción",
  "content": "Contenido nuevo con al menos 100 caracteres...",
  "categoria": "Gaming",
  "image": "https://example.com/new.jpg",
  "gradient": "linear-gradient(...)"
}
```

**Status:** 200 OK

---

### Eliminar artículo (ADMIN)
```http
DELETE /api/v1/blog/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

**Status:** 200 OK  
**Requiere:** Rol ADMIN

---

## 📦 ÓRDENES

### Obtener todas las órdenes
```http
GET /api/v1/ordenes HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

---

### Obtener orden por ID
```http
GET /api/v1/ordenes/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

---

### Crear orden
```http
POST /api/v1/ordenes HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
Content-Type: application/json

{
  "usuarioId": 1,
  "items": [
    {
      "productoId": 1,
      "cantidad": 2,
      "precio": 59.99
    }
  ],
  "total": 119.98,
  "estado": "PENDIENTE",
  "metodoPago": "TARJETA_CREDITO"
}
```

**Status:** 201 Created

---

### Actualizar orden (ADMIN)
```http
PUT /api/v1/ordenes/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
Content-Type: application/json

{
  "estado": "ENVIADA",
  "metodoPago": "TARJETA_CREDITO"
}
```

**Status:** 200 OK  
**Requiere:** Rol ADMIN

---

### Eliminar orden (ADMIN)
```http
DELETE /api/v1/ordenes/1 HTTP/1.1
Host: localhost:8080
Authorization: Bearer JWT_TOKEN_AQUI
```

**Status:** 200 OK  
**Requiere:** Rol ADMIN

---

## 🔑 ROLES Y PERMISOS

| Endpoint | USER | ADMIN | SELLER | EDITOR |
|----------|------|-------|--------|--------|
| POST /auth/register | ✅ | ✅ | ✅ | ✅ |
| POST /auth/login | ✅ | ✅ | ✅ | ✅ |
| GET /usuarios | ✅ | ✅ | ✅ | ✅ |
| PUT /usuarios/{id} | ✅ | ✅ | ✅ | ✅ |
| DELETE /usuarios/{id} | ✅ | ✅ | ✅ | ✅ |
| GET /productos | ✅ | ✅ | ✅ | ✅ |
| POST /productos | ❌ | ✅ | ✅ | ❌ |
| PUT /productos/{id} | ❌ | ✅ | ✅ | ❌ |
| DELETE /productos/{id} | ❌ | ✅ | ❌ | ❌ |
| GET /blog | ✅ | ✅ | ✅ | ✅ |
| POST /blog | ❌ | ✅ | ❌ | ✅ |
| PUT /blog/{id} | ❌ | ✅ | ❌ | ✅ |
| DELETE /blog/{id} | ❌ | ✅ | ❌ | ❌ |
| GET /ordenes | ✅ | ✅ | ✅ | ✅ |
| POST /ordenes | ✅ | ✅ | ✅ | ✅ |
| PUT /ordenes/{id} | ❌ | ✅ | ❌ | ❌ |
| DELETE /ordenes/{id} | ❌ | ✅ | ❌ | ❌ |

---

## 📝 EJEMPLOS EN POSTMAN

### Configurar variable de token en Postman

1. Abre la colección LEVEL_UP_GAMER.postman_collection.json
2. Ve a la pestaña **Variables**
3. Busca `token` y en el campo **Current Value** pega tu JWT token
4. Los requests ahora usarán automáticamente `{{token}}` en el header

### Para cada request:
```
Authorization: Bearer {{token}}
```

---

## 🛠️ EJEMPLOS EN POWERSHELL

### Registrarse
```powershell
$body = @{
    nombre = "Juan Pérez"
    email = "juan@example.com"
    password = "Password123!"
    passwordConfirm = "Password123!"
} | ConvertTo-Json

$response = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/v1/auth/register" `
  -Body $body `
  -ContentType "application/json"

# Guardar el token
$token = $response.token
Write-Host "Token: $token"
```

### Cambiar rol de un usuario (ADMIN)
```powershell
$body = @{
    nombre = "Juan Pérez"
    rol = "ADMIN"
} | ConvertTo-Json

Invoke-RestMethod -Method Put `
  -Uri "http://localhost:8080/api/v1/usuarios/1" `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

### Cambiar contraseña (recuperación)
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

### Cambiar email
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

### Actualizar múltiples campos a la vez
```powershell
$body = @{
    nombre = "Juan Pérez Nuevo"
    email = "newemail@example.com"
    password = "NuevaPassword123!"
    passwordConfirm = "NuevaPassword123!"
    rol = "SELLER"
} | ConvertTo-Json

Invoke-RestMethod -Method Put `
  -Uri "http://localhost:8080/api/v1/usuarios/1" `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

### Login
```powershell
$body = @{
    email = "juan@example.com"
    password = "Password123!"
} | ConvertTo-Json

$response = Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/v1/auth/login" `
  -Body $body `
  -ContentType "application/json"

$token = $response.token
```

### Obtener todos los usuarios
```powershell
Invoke-RestMethod -Method Get `
  -Uri "http://localhost:8080/api/v1/usuarios" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

### Obtener todos los productos
```powershell
Invoke-RestMethod -Method Get `
  -Uri "http://localhost:8080/api/v1/productos" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

### Crear producto (ADMIN/SELLER)
```powershell
$body = @{
    nombre = "PS5 Controller"
    precio = 59.99
    categoria = "ACCESORIOS"
    stock = 50
    descripcion = "Control para PlayStation 5"
    imagen = "https://example.com/ps5.jpg"
    destacado = $true
    nuevo = $true
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/v1/productos" `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

### Obtener todos los artículos del blog (sin autenticación)
```powershell
Invoke-RestMethod -Method Get `
  -Uri "http://localhost:8080/api/v1/blog" | ConvertTo-Json
```

### Crear artículo (ADMIN/EDITOR)
```powershell
$body = @{
    title = "Guía de PS5"
    excerpt = "Una guía completa"
    content = "Contenido del artículo con al menos 100 caracteres describiendo las características de PS5..."
    categoria = "Gaming"
    author = "Juan García"
    image = "https://example.com/ps5.jpg"
    gradient = "linear-gradient(135deg, #667eea 0%, #764ba2 100%)"
} | ConvertTo-Json

Invoke-RestMethod -Method Post `
  -Uri "http://localhost:8080/api/v1/blog" `
  -Body $body `
  -ContentType "application/json" `
  -Headers @{ Authorization = "Bearer $token" } | ConvertTo-Json
```

---

## ⚠️ ERRORES COMUNES

| Código | Mensaje | Solución |
|--------|---------|----------|
| 401 | Unauthorized | Falta el token o expiró. Haz login de nuevo. |
| 403 | Forbidden | No tienes el rol necesario para esta operación. |
| 404 | Not Found | El recurso no existe. Verifica el ID. |
| 400 | Bad Request | Datos inválidos. Revisa el JSON enviado. |
| 409 | Conflict | El email ya existe. Usa otro email. |
| 500 | Server Error | Error del servidor. Revisa los logs. |

---

## 📦 Descargar Colección Postman

El archivo `LEVEL_UP_GAMER.postman_collection.json` está en la raíz del proyecto.

### Importar en Postman:
1. Abre Postman
2. Haz clic en **Import**
3. Selecciona **File**
4. Busca y abre `LEVEL_UP_GAMER.postman_collection.json`
5. ¡Listo! Todos los endpoints están disponibles

---

## ✅ CHECKLIST DE PRUEBAS

- [ ] Registrarse con un nuevo usuario
- [ ] Hacer login y obtener token
- [ ] Validar token
- [ ] Obtener todos los usuarios
- [ ] Obtener todos los productos
- [ ] Obtener todos los artículos del blog
- [ ] Crear un nuevo artículo (con rol EDITOR/ADMIN)
- [ ] Crear un nuevo producto (con rol SELLER/ADMIN)
- [ ] Obtener artículos por autor
- [ ] Actualizar un usuario
- [ ] Crear una orden
- [ ] Listar todas las órdenes

---

¡Listo! Ya tienes todo lo necesario para usar la API de LEVEL UP GAMER. 🚀
