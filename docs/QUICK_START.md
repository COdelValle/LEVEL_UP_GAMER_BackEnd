# ⚡ QUICK START - LEVEL UP GAMER BackEnd

## 🚀 En 5 Minutos

### 1. **Clonar y Preparar**
```bash
cd LEVEL_UP_GAMER_BackEnd/BackEnd
```

### 2. **Compilar**
```bash
mvnw clean compile
```

### 3. **Ejecutar**
```bash
mvnw spring-boot:run
```

### 4. **Verificar**
```
✅ Servidor iniciado en http://localhost:8080
✅ Base de datos conectada
✅ Endpoints listos
```

---

## 🔗 Endpoints Principales

### Registro
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Test",
    "email": "test@example.com",
    "password": "Pass123456",
    "passwordConfirm": "Pass123456"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Pass123456"
  }'
```

### Usar Token
```bash
curl -H "Authorization: Bearer <token>" \
  http://localhost:8080/api/usuarios
```

---

## 📋 Validaciones Incluidas

| Campo | Validación |
|-------|-----------|
| Email | @Email, unique |
| Nombre | @NotBlank, 2-100 chars |
| Contraseña | @NotBlank, min 6 chars |
| Teléfono | Pattern: 7-15 dígitos |
| Código Postal | Pattern: 6-8 dígitos |

---

## 🔐 Seguridad Implementada

- ✅ BCrypt password encryption
- ✅ JWT tokens (24h)
- ✅ API Key generation
- ✅ Spring Security filters
- ✅ Global exception handling

---

## 📄 Documentación Disponible

- `IMPLEMENTACION_SEGURIDAD.md` - Detalles completos
- `ARQUITECTURA_SEGURIDAD.md` - Diagramas
- `GUIA_PRUEBAS.md` - Ejemplos completos
- `ESTRUCTURA_CARPETAS.md` - Organización
- `RESUMEN_FINAL.md` - Este documento

---

## ⚙️ Configuración

Editar `application.properties`:
```properties
app.jwtSecret=tu-clave-secreta
app.jwtExpirationMs=86400000  # 24 horas
server.port=8080
```

---

## 🆘 Troubleshooting

**Error: Base de datos no conecta**
- Verificar `tnsnames.ora` en wallet/
- Verificar credenciales en application.properties

**Error: Puerto 8080 ocupado**
- Cambiar puerto: `server.port=8081`

**Error: Maven no compila**
- Limpiar cache: `mvnw clean`
- Actualizar: `mvnw help:describe`

---

## ✨ Características Principales

- 🔐 Autenticación JWT + API Key
- ✅ Validaciones completas
- 🔒 BCrypt password encryption
- 🛡️ Spring Security
- 📝 Global exception handling
- 🗄️ Oracle database
- 📚 Documentación completa

---

**Status: ✅ LISTO PARA USAR**
