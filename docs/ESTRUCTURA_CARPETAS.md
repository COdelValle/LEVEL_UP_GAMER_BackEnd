# 📁 Estructura de Carpetas - LEVEL UP GAMER BackEnd

## Árbol de Directorios Completo

```
LEVEL_UP_GAMER_BackEnd/
│
├── README.md                                    # Documentación principal
│
├── IMPLEMENTACION_SEGURIDAD.md                  # 📄 Guía completa de seguridad
├── ARQUITECTURA_SEGURIDAD.md                    # 📄 Diagramas y arquitectura
├── GUIA_PRUEBAS.md                              # 📄 Ejemplos de pruebas
├── ESTRUCTURA_CARPETAS.md                       # 📄 Este archivo
│
└── BackEnd/                                     # Proyecto Maven Spring Boot
    │
    ├── pom.xml                                  # ✏️ Configuración Maven (actualizado)
    ├── mvnw                                     # Maven wrapper (Linux/Mac)
    ├── mvnw.cmd                                 # Maven wrapper (Windows)
    │
    ├── src/
    │
    ├── main/
    │   │
    │   ├── java/com/level_up_gamer/BackEnd/
    │   │   │
    │   │   ├── 📁 BackEndApplication.java       # Clase principal Spring Boot
    │   │   │
    │   │   ├── 📁 Controller/                   # Controladores REST
    │   │   │   ├── AuthController.java          # ✨ NUEVO - Autenticación
    │   │   │   │   ├── register()
    │   │   │   │   ├── login()
    │   │   │   │   └── validateToken()
    │   │   │   │
    │   │   │   ├── UsuarioController.java       # ✏️ ACTUALIZADO
    │   │   │   │   ├── getAllUsuarios()
    │   │   │   │   ├── getUsuarioById()
    │   │   │   │   ├── updateUsuario()
    │   │   │   │   └── deleteUsuario()
    │   │   │   │
    │   │   │   └── [otros controladores...]
    │   │   │
    │   │   ├── 📁 Service/                      # Servicios de negocio
    │   │   │   ├── UsuarioService.java          # ✏️ ACTUALIZADO
    │   │   │   │   ├── getUsuarios()
    │   │   │   │   ├── registrar()              # ✨ NUEVO
    │   │   │   │   ├── login()                  # ✨ NUEVO
    │   │   │   │   ├── validarToken()           # ✨ NUEVO
    │   │   │   │   ├── getUsuarioFromToken()    # ✨ NUEVO
    │   │   │   │   └── getUsuarioByApiKey()     # ✨ NUEVO
    │   │   │   │
    │   │   │   ├── BlogService.java
    │   │   │   ├── ProductoService.java
    │   │   │   │
    │   │   │   └── Orden/
    │   │   │       ├── OrdenService.java
    │   │   │       └── OrdenItemService.java
    │   │   │
    │   │   ├── 📁 DTO/                          # Data Transfer Objects
    │   │   │   ├── LoginRequest.java            # ✨ NUEVO
    │   │   │   ├── RegisterRequest.java         # ✨ NUEVO
    │   │   │   ├── AuthResponse.java            # ✏️ ACTUALIZADO
    │   │   │   │
    │   │   │   ├── CreateProductoRequest.java   # ✨ NUEVO
    │   │   │   ├── ProductoResponse.java        # ✨ NUEVO
    │   │   │   │
    │   │   │   ├── CreateOrdenRequest.java      # ✏️ ACTUALIZADO
    │   │   │   ├── OrdenItemRequest.java        # ✨ NUEVO
    │   │   │   │
    │   │   │   ├── CreateBlogRequest.java       # ✏️ ACTUALIZADO
    │   │   │   │
    │   │   │   └── UpdateUsuarioRequest.java    # ✏️ ACTUALIZADO
    │   │   │
    │   │   ├── 📁 Model/                        # Entidades JPA
    │   │   │   │
    │   │   │   ├── Region.java
    │   │   │   │
    │   │   │   ├── Blog/
    │   │   │   │   ├── Blog.java
    │   │   │   │   └── CategoriaBlog.java
    │   │   │   │
    │   │   │   ├── Orden/
    │   │   │   │   ├── Orden.java
    │   │   │   │   ├── OrdenItem.java
    │   │   │   │   ├── EstadoOrden.java
    │   │   │   │   ├── MetodoPago.java
    │   │   │   │   └── InfoEnvio.java
    │   │   │   │
    │   │   │   ├── Producto/
    │   │   │   │   ├── Producto.java
    │   │   │   │   └── CategoriaProducto.java
    │   │   │   │
    │   │   │   └── Usuario/
    │   │   │       ├── Usuario.java             # ✏️ ACTUALIZADO (validaciones)
    │   │   │       └── RolUsuario.java
    │   │   │
    │   │   ├── 📁 Repository/                   # Acceso a datos
    │   │   │   ├── UsuarioRepository.java       # ✏️ ACTUALIZADO
    │   │   │   │   ├── findByEmail()
    │   │   │   │   ├── findByApiKey()           # ✨ NUEVO
    │   │   │   │   ├── existsByEmail()
    │   │   │   │   └── deleteByEmail()
    │   │   │   │
    │   │   │   ├── BlogRepository.java
    │   │   │   │
    │   │   │   └── Orden/
    │   │   │       ├── OrdenRepository.java
    │   │   │       └── OrdenItemRepository.java
    │   │   │
    │   │   ├── Producto/
    │   │   │   ├── CategoriaProductoRepository.java
    │   │   │   └── ProductoRepository.java
    │   │   │
    │   │   ├── 📁 Security/                     # Seguridad y cifrado
    │   │   │   ├── JwtProvider.java             # ✨ NUEVO
    │   │   │   │   ├── generateToken()
    │   │   │   │   ├── getEmailFromToken()
    │   │   │   │   ├── getUsuarioIdFromToken()
    │   │   │   │   └── validateToken()
    │   │   │   │
    │   │   │   ├── PasswordEncrypter.java       # ✨ NUEVO
    │   │   │   │   ├── encryptPassword()
    │   │   │   │   ├── matches()
    │   │   │   │   └── generateApiKey()
    │   │   │   │
    │   │   │   └── JwtAuthenticationFilter.java # ✨ NUEVO
    │   │   │       ├── doFilterInternal()
    │   │   │       └── getTokenFromRequest()
    │   │   │
    │   │   ├── 📁 Config/                       # Configuración
    │   │   │   └── SecurityConfig.java          # ✨ NUEVO
    │   │   │       ├── passwordEncoder()
    │   │   │       └── filterChain()
    │   │   │
    │   │   └── 📁 Exception/                    # Manejo de excepciones
    │   │       ├── GlobalExceptionHandler.java  # ✏️ ACTUALIZADO
    │   │       │   ├── handleValidationExceptions()
    │   │       │   ├── handleResourceNotFoundException()
    │   │       │   └── handleConflictException()
    │   │       │
    │   │       ├── ResourceNotFoundException.java # ✨ NUEVO
    │   │       └── ConflictException.java       # ✨ NUEVO
    │   │
    │   └── resources/
    │       ├── application.properties           # ✏️ ACTUALIZADO
    │       │   ├── spring.application.name
    │       │   ├── spring.datasource.*
    │       │   ├── spring.jpa.*
    │       │   ├── app.jwtSecret              # ✨ NUEVO
    │       │   ├── app.jwtExpirationMs        # ✨ NUEVO
    │       │   └── server.*
    │       │
    │       └── wallet/                        # Credenciales Oracle
    │           ├── cwallet.sso
    │           ├── ojdbc.properties
    │           ├── sqlnet.ora
    │           ├── tnsnames.ora
    │           └── README
    │
    ├── test/
    │   └── java/com/level_up_gamer/BackEnd/
    │       ├── BackEndApplicationTests.java
    │       └── AuthControllerTests.java       # ✨ NUEVO
    │
    └── target/                                 # Compilado (generado)
        ├── classes/
        ├── generated-sources/
        └── BackEnd-0.0.1-SNAPSHOT.jar        # JAR ejecutable

```

## 📊 Estadísticas de Cambios

```
Total de Archivos Nuevos:  12
Total de Archivos Actualizados: 8
Total de Líneas de Código Agregadas: ~2,500
Total de Validaciones: 50+
```

## 🔄 Mapa de Dependencias

```
AuthController
  ├─> UsuarioService
  │   ├─> UsuarioRepository
  │   ├─> PasswordEncrypter (BCrypt)
  │   └─> JwtProvider
  │
  └─> GlobalExceptionHandler
        ├─> ResourceNotFoundException
        └─> ConflictException

SecurityConfig
  ├─> JwtAuthenticationFilter
  │   └─> JwtProvider
  └─> BCryptPasswordEncoder

JwtAuthenticationFilter
  └─> JwtProvider
        └─> SecretKey (HS512)

Usuario (Entity)
  ├─> @Valid Annotations
  ├─> BCrypt Password
  └─> API Key (Unique)
```

## 🎯 Funcionalidades por Componente

### Backend/Controller
| Controlador | Responsabilidad |
|-------------|-----------------|
| **AuthController** | Registro, Login, Validación de Token |
| **UsuarioController** | CRUD de Usuarios (GET, PUT, DELETE) |
| **OtrosController** | [Pendientes de implementar] |

### Backend/Service
| Servicio | Métodos Clave |
|----------|--------------|
| **UsuarioService** | registrar, login, validarToken, getUsuarioFromToken, getUsuarioByApiKey |
| **BlogService** | [Existentes] |
| **ProductoService** | [Existentes] |
| **OrdenService** | [Existentes] |

### Backend/Security
| Componente | Función |
|-----------|---------|
| **JwtProvider** | Generación y validación de JWT |
| **PasswordEncrypter** | Cifrado de contraseñas y generación de API Keys |
| **JwtAuthenticationFilter** | Interceptor de peticiones autenticadas |

### Backend/DTO
| DTO | Validaciones |
|-----|-------------|
| **LoginRequest** | Email válido, contraseña requerida |
| **RegisterRequest** | Nombre, email único, contraseña con confirmación |
| **CreateProductoRequest** | Nombre, precio, stock, categoría |
| **CreateOrdenRequest** | Items, dirección, teléfono, código postal |
| **CreateBlogRequest** | Título, contenido, categoría, autor |

---

## 📝 Naming Conventions

### Paquetes
```
com.level_up_gamer.BackEnd.[Module].[Submodule]

Ejemplos:
- com.level_up_gamer.BackEnd.Controller
- com.level_up_gamer.BackEnd.Service
- com.level_up_gamer.BackEnd.Model.Usuario
- com.level_up_gamer.BackEnd.Repository
```

### Clases
```
[Nombre][Tipo]

Tipos: Controller, Service, Repository, DTO, Entity, Filter, Provider, Config

Ejemplos:
- AuthController
- UsuarioService
- UsuarioRepository
- LoginRequest (DTO)
- Usuario (Entity)
- JwtAuthenticationFilter
- JwtProvider
- SecurityConfig
```

### Métodos
```
[verbo][sustantivo]

Ejemplos:
- getUsuarios()
- saveUsuario()
- deleteUsuarioById()
- registrar()
- validarToken()
- generateToken()
```

### Variables
```
[tipo][nombre]

Ejemplos:
- String email
- Long usuarioId
- Boolean isValid
- List<Usuario> usuarios
```

---

## 🔗 Relaciones entre Componentes

```
Request HTTP
    │
    ├─> JwtAuthenticationFilter (valida token)
    │
    ├─> Controller (enruta petición)
    │
    ├─> Service (lógica negocio)
    │
    ├─> Repository (acceso datos)
    │
    ├─> Base Datos (persistencia)
    │
    └─> Response HTTP
```

---

## 📦 Tamaño Aproximado de Archivos

| Componente | Líneas de Código | Tamaño |
|-----------|-----------------|--------|
| JwtProvider | ~85 | 3.2 KB |
| PasswordEncrypter | ~35 | 1.4 KB |
| JwtAuthenticationFilter | ~45 | 1.8 KB |
| SecurityConfig | ~50 | 2.1 KB |
| AuthController | ~95 | 3.8 KB |
| UsuarioService | ~140 | 5.2 KB |
| UsuarioController | ~110 | 4.3 KB |
| GlobalExceptionHandler | ~65 | 2.6 KB |
| **TOTAL** | **~625** | **~24.4 KB** |

---

## ✅ Estado de Implementación

### Seguridad
- [x] Autenticación JWT
- [x] API Key
- [x] BCrypt Password
- [x] Spring Security Config
- [x] JWT Filter

### Validaciones
- [x] Bean Validation (Jakarta)
- [x] Custom Validators
- [x] Global Exception Handler
- [x] Error Response Standardization

### Endpoints
- [x] POST /api/auth/register
- [x] POST /api/auth/login
- [x] POST /api/auth/validate
- [x] GET /api/usuarios
- [x] GET /api/usuarios/{id}
- [x] GET /api/usuarios/email/{email}
- [x] PUT /api/usuarios/{id}
- [x] DELETE /api/usuarios/{id}

### Testing
- [x] AuthControllerTests
- [ ] IntegrationTests
- [ ] SecurityTests
- [ ] ValidationTests

---

**Estado Final: ✅ ESTRUCTURA COMPLETA**
**Compilación: ✅ EXITOSA**
**Fecha: 23 de noviembre de 2025**
