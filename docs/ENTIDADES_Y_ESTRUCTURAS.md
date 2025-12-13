# ENTIDADES Y ESTRUCTURAS DE DATOS - LEVEL UP GAMER

## Documento de Referencia para API Spring Boot

Fecha de creación: Noviembre 2025
Descripción: Mapeo completo de todas las entidades del sitio web LEVEL-UP_GAMER con sus estructuras de datos.

---

## TABLA DE CONTENIDOS

1. [Producto](#producto)
2. [Usuario](#usuario)
3. [Carrito](#carrito)
4. [Orden/Pedido](#ordenpedido)
5. [Blog/Artículo](#blogarticulo)
6. [Categoría](#categoría)
7. [Región/Comuna (Ubicación)](#regióncomuna)
8. [Sesión Admin](#sesión-admin)

---

## PRODUCTO

### Descripción
Entidad que representa los productos disponibles en la tienda. Incluye información sobre especificaciones técnicas, precios, stock y ofertas.

### Estructura JSON
```json
{
  "id": 1,
  "nombre": "PlayStation 5",
  "precio": 699990,
  "categoria": "consolas",
  "imagen": "https://images.unsplash.com/photo-1606813907291-d86efa9b94db?w=600&h=400&fit=crop",
  "descripcion": "La última consola de Sony con tecnología de vanguardia",
  "stock": 15,
  "destacado": true,
  "nuevo": false,
  "oferta": false,
  "precioOferta": null,
  "especificaciones": {
    "Almacenamiento": "825GB SSD",
    "Resolución": "4K",
    "Características": "Ray Tracing, 120Hz"
  }
}
```

### Campos
| Campo | Tipo | Descripción | Requerido |
|-------|------|-------------|-----------|
| `id` | Long | Identificador único del producto | ✅ |
| `nombre` | String | Nombre del producto | ✅ |
| `precio` | Integer | Precio en pesos chilenos (CLP) | ✅ |
| `categoria` | String | Categoría del producto (ej: consolas, perifericos, etc) | ✅ |
| `imagen` | String | URL de la imagen del producto | ✅ |
| `descripcion` | String | Descripción detallada del producto | ✅ |
| `stock` | Integer | Cantidad disponible en stock | ✅ |
| `destacado` | Boolean | Si el producto aparece en destacados | ✅ |
| `nuevo` | Boolean | Si el producto es nuevo | ✅ |
| `oferta` | Boolean | Si el producto tiene oferta activa | ❌ |
| `precioOferta` | Integer | Precio con oferta (null si no hay oferta) | ❌ |
| `especificaciones` | Object | Especificaciones técnicas clave-valor | ❌ |

### Categorías disponibles
- `consolas` - Consolas de videojuegos
- `pc-gamers` - Computadoras gaming
- `perifericos` - Teclados, mouse, auriculares
- `sillas` - Sillas gaming ergonómicas
- `monitores` - Monitores gaming
- `accesorios` - Diversos accesorios
- `audio` - Equipos de audio
- `streaming` - Equipo para streaming
- `creativo` - Equipamiento creativo
- `laptops` - Laptops gaming
- `juegos-mesa` - Juegos de mesa
- `juegos-de-mesa` - Alternativa juegos de mesa
- `mouse` - Mouse gaming específicos
- `mousepad` - Mousepads
- `ropa` - Ropa gaming

### Clase Java Equivalente
```java
@Entity
@Table(name = "productos")
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false)
    private Integer precio;
    
    @Column(nullable = false)
    private String categoria;
    
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String imagen;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = false)
    private Integer stock;
    
    @Column(nullable = false)
    private Boolean destacado = false;
    
    @Column(nullable = false)
    private Boolean nuevo = false;
    
    @Column(nullable = true)
    private Boolean oferta = false;
    
    @Column(nullable = true)
    private Integer precioOferta;
    
    @Column(columnDefinition = "JSON")
    private String especificaciones; // Guardar como JSON string
    
    // Getters y Setters...
}
```

---

## USUARIO

### Descripción
Entidad que representa los usuarios registrados en el sistema. Incluye autenticación y datos de perfil.

### Estructura JSON
```json
{
  "id": 1,
  "nombre": "Benjamin",
  "email": "benjamin@levelup.cl",
  "password": "123456",
  "duoc": true,
  "puntos": 1200
}
```

### Campos
| Campo | Tipo | Descripción | Requerido |
|-------|------|-------------|-----------|
| `id` | Long | Identificador único del usuario | ✅ |
| `nombre` | String | Nombre del usuario | ✅ |
| `email` | String | Email único del usuario | ✅ |
| `password` | String | Contraseña hasheada | ✅ |
| `rol` | Enum | Rol del usuario (admin, user, guest) | ✅ |
| `duoc` | Boolean | Si es estudiante/funcionario DUOC | ❌ |
| `puntos` | Integer | Puntos acumulados en el programa de lealtad | ❌ |
| `fechaCreacion` | LocalDateTime | Fecha de creación de la cuenta | ✅ |
| `ultimoAcceso` | LocalDateTime | Última fecha de acceso/login | ❌ |

### Clase Java Equivalente
```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password; // Usar BCryptPasswordEncoder
    
    @Column(nullable = true)
    private Boolean duoc = false;
    
    @Column(nullable = true)
    private Integer puntos = 0;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolUsuario rol = RolUsuario.USER;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(nullable = true)
    private LocalDateTime ultimoAcceso;
    
    // Getters y Setters...
}

public enum RolUsuario {
    ADMIN,
    USER,
    GUEST
}
```

---

## CARRITO

### Descripción
Entidad que representa el carrito de compras del usuario. Contiene los productos seleccionados antes de confirmar la compra.

### Estructura JSON
```json
{
  "cartItems": [
    {
      "id": 1,
      "nombre": "PlayStation 5",
      "precio": 699990,
      "categoria": "consolas",
      "imagen": "https://images.unsplash.com/...",
      "descripcion": "La última consola de Sony...",
      "stock": 15,
      "destacado": true,
      "nuevo": false,
      "quantity": 2,
      "especificaciones": {
        "Almacenamiento": "825GB SSD"
      }
    }
  ]
}
```

### Cálculos derivados
| Campo | Cálculo |
|-------|---------|
| `totalItems` | Sum(quantity) |
| `totalPrice` | Sum(precio * quantity) |

### Campos del Item en Carrito
| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | ID del producto |
| `nombre` | String | Nombre del producto |
| `precio` | Integer | Precio unitario |
| `quantity` | Integer | Cantidad en carrito |
| (todos los campos de Producto) | - | Datos del producto |

### Clase Java Equivalente
```java
@Entity
@Table(name = "carritos")
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "carrito_id")
    private List<CarritoItem> items = new ArrayList<>();
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    // Getters y Setters...
}

@Entity
@Table(name = "carrito_items")
public class CarritoItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    // Getters y Setters...
}
```

---

## ORDEN/PEDIDO

### Descripción
Entidad que representa una orden completada. Incluye información de envío, detalles de la compra y estado.

### Estructura JSON
```json
{
  "id": 1,
  "numero": "ORD-001",
  "fecha": "2024-01-15",
  "total": 699990,
  "estado": "completado",
  "metodoPago": "tarjeta",
  "items": [
    {
      "nombre": "PlayStation 5",
      "cantidad": 1,
      "precio": 699990
    }
  ],
  "shipping": {
    "nombre": "Benjamin",
    "apellido": "García",
    "email": "benjamin@levelup.cl",
    "telefono": "+56912345678",
    "direccion": "Calle Principal 123",
    "departamento": "Apto 4B",
    "ciudad": "Santiago",
    "region": "metropolitana",
    "comuna": "Las Condes",
    "codigoPostal": "8320000",
    "pais": "Chile"
  }
}
```

### Campos
| Campo | Tipo | Descripción | Requerido |
|-------|------|-------------|-----------|
| `id` | Long | Identificador único de la orden | ✅ |
| `numero` | String | Número de orden amigable (ORD-001) | ✅ |
| `userId` | Long | ID del usuario que realizó la compra | ✅ |
| `fecha` | LocalDateTime | Fecha y hora de creación de la orden | ✅ |
| `total` | Integer | Monto total de la orden en CLP | ✅ |
| `estado` | String | Estado de la orden (completado, pendiente, cancelado, enviado) | ✅ |
| `metodoPago` | String | Método de pago (tarjeta, transferencia, paypal) | ✅ |
| `items` | List\<OrdenItem\> | Lista de productos en la orden | ✅ |
| `shipping` | Object | Información de envío | ✅ |
| `user` | Object | Datos del usuario | ❌ |

### Estados posibles
- `pendiente` - Orden creada, no confirmada
- `completado` - Orden pagada exitosamente
- `enviado` - Orden en tránsito
- `entregado` - Orden recibida
- `cancelado` - Orden cancelada

### Métodos de pago
- `tarjeta` - Tarjeta de débito/crédito
- `transferencia` - Transferencia bancaria
- `paypal` - PayPal
- `otro` - Otro método

### Información de Envío (shipping)
```json
{
  "nombre": "Benjamin",
  "apellido": "García",
  "email": "benjamin@levelup.cl",
  "telefono": "+56912345678",
  "direccion": "Calle Principal 123",
  "departamento": "Apto 4B",
  "ciudad": "Santiago",
  "region": "metropolitana",
  "comuna": "Las Condes",
  "codigoPostal": "8320000",
  "pais": "Chile"
}
```

### Clase Java Equivalente
```java
@Entity
@Table(name = "ordenes")
public class Orden {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String numero; // Ej: ORD-001, ORD-002
    
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();
    
    @Column(nullable = false)
    private Integer total;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoOrden estado;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MetodoPago metodoPago;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orden_id")
    private List<OrdenItem> items;
    
    @Embedded
    private InfoEnvio infoEnvio;
    
    // Getters y Setters...
}

public enum EstadoOrden {
    PENDIENTE,
    COMPLETADO,
    ENVIADO,
    ENTREGADO,
    CANCELADO
}

public enum MetodoPago {
    TARJETA,
    TRANSFERENCIA,
    PAYPAL,
    OTRO
}

@Entity
@Table(name = "orden_items")
public class OrdenItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(nullable = false)
    private Integer precioUnitario;
    
    // Getters y Setters...
}

@Embeddable
public class InfoEnvio {
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = true)
    private String apellido;
    
    @Column(nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String telefono;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column(nullable = true)
    private String departamento;
    
    @Column(nullable = false)
    private String ciudad;
    
    @Column(nullable = false)
    private String region;
    
    @Column(nullable = false)
    private String comuna;
    
    @Column(nullable = true)
    private String codigoPostal;
    
    @Column(nullable = false)
    private String pais;
    
    // Getters y Setters...
}
```

---

## BLOG/ARTICULO

### Descripción
Entidad que representa artículos del blog. Incluye contenido HTML, metadatos y estadísticas de interacción. Soporta incremento público de vistas y likes sin requerer autenticación.

### Estructura JSON (BlogResponse)
```json
{
  "id": 1,
  "title": "Cómo Armar el Setup Gamer Perfecto en 2025",
  "excerpt": "Descubre los componentes esenciales para crear tu estación de juego ideal...",
  "content": "<h2>La Guía Definitiva para tu Setup Gaming</h2><p>En 2025...</p>",
  "categoria": "GUIAS",
  "author": "Level-Up Gamer Team",
  "fecha": "2025-01-15",
  "readTime": "15 min lectura",
  "image": "https://...",
  "gradient": "from-blue-600 to-purple-600",
  "featured": true,
  "likes": 124,
  "tags": ["Setup", "Gaming", "Hardware", "Ergonomía"],
  "views": 2847
}
```

### Campos
| Campo | Tipo | Descripción | Requerido | Actualizable públicamente |
|-------|------|-------------|-----------|--------------------------|
| `id` | Long | Identificador único del artículo | ✅ | ❌ |
| `title` | String | Título del artículo | ✅ | ✅ (ADMIN) |
| `excerpt` | String | Resumen corto del artículo | ✅ | ✅ (ADMIN) |
| `content` | Text | Contenido HTML del artículo | ✅ | ✅ (ADMIN) |
| `categoria` | Enum | Categoría (GUIAS, REVIEWS, NOTICIAS, TUTORIALES) | ✅ | ✅ (ADMIN) |
| `author` | String | Autor del artículo | ✅ | ✅ (ADMIN) |
| `fecha` | LocalDate | Fecha de publicación (se asigna al crear) | ✅ | ❌ |
| `readTime` | String | Tiempo estimado de lectura | ❌ | ✅ (ADMIN) |
| `image` | String | URL de imagen de portada | ❌ | ✅ (ADMIN) |
| `gradient` | String | Clases de gradiente Tailwind | ❌ | ✅ (ADMIN) |
| `featured` | Boolean | Si aparece en destacados | ✅ | ❌ |
| `likes` | Integer | Cantidad de likes (incrementable públicamente) | ✅ | ✅ (público: POST /blog/{id}/like, /unlike) |
| `tags` | List<String> | Tags de clasificación | ❌ | ❌ |
| `views` | Integer | Cantidad de visualizaciones (incrementable públicamente) | ✅ | ✅ (público: POST /blog/{id}/views) |

### Endpoints para Blog
- `GET /api/v1/blog` — Listar todos los artículos
- `GET /api/v1/blog/{id}` — Obtener artículo por ID
- `GET /api/v1/blog/destacados` — Obtener artículos destacados
- `GET /api/v1/blog/autor/{autor}` — Obtener artículos por autor
- `POST /api/v1/blog/{id}/views` — **Incrementar vistas en 1** (público, sin autenticación)
- `POST /api/v1/blog/{id}/like` — **Incrementar likes en 1** (público, sin autenticación)
- `POST /api/v1/blog/{id}/unlike` — **Decrementar likes en 1** (público, sin autenticación; no va por debajo de 0)
- `POST /api/v1/blog` — Crear artículo (requiere rol `ADMIN`)
- `POST /api/v1/blog/bulk` — Crear múltiples artículos (requiere rol `ADMIN`)
- `PUT /api/v1/blog/{id}` — Actualizar artículo (requiere rol `ADMIN`)
- `DELETE /api/v1/blog/{id}` — Eliminar artículo (requiere rol `ADMIN`)

### Categorías de Blog
- `GUIAS` - Guías y tutoriales
- `REVIEWS` - Reseñas de productos/juegos
- `NOTICIAS` - Noticias de la industria
- `TUTORIALES` - Tutoriales paso a paso

### Clase Java Equivalente
```java
@Entity
@Table(name = "blog")
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "El título es requerido")
    @Size(min = 10, max = 200, message = "El título debe tener entre 10 y 200 caracteres")
    private String title;
    
    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    @NotBlank(message = "El resumen (excerpt) es requerido")
    @Size(min = 50, max = 500, message = "El resumen debe tener entre 50 y 500 caracteres")
    private String excerpt;
    
    @Lob
    @Column(nullable = false, columnDefinition = "CLOB")
    @NotBlank(message = "El contenido es requerido")
    @Size(min = 100, message = "El contenido debe tener al menos 100 caracteres")
    private String content;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CategoriaBlog categoria;
    
    @Column(nullable = false)
    @NotBlank(message = "El autor es requerido")
    private String author;
    
    @Column(nullable = false)
    @NotNull(message = "La fecha es requerida")
    private LocalDate fecha;
    
    @Column(nullable = true)
    private String readTime;
    
    @Column(nullable = true)
    private String image;
    
    @Column(nullable = true)
    private String gradient;
    
    @Column(nullable = false)
    private Boolean featured = false;
    
    @Column(nullable = true)
    private Integer likes = 0;
    
    @ElementCollection
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();
    
    @Column(nullable = true)
    private Integer views = 0;
    
    // Getters y Setters...
}

public enum CategoriaBlog {
    GUIAS,
    REVIEWS,
    NOTICIAS,
    TUTORIALES
}
```

### DTO de Respuesta (BlogResponse)
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Información detallada de un artículo de blog")
public class BlogResponse {
    
    @Schema(description = "ID único del artículo", example = "1")
    private Long id;
    
    @Schema(description = "Título del artículo")
    private String title;
    
    @Schema(description = "Resumen del artículo")
    private String excerpt;
    
    @Schema(description = "Contenido completo del artículo")
    private String content;
    
    @Schema(description = "Categoría del artículo")
    private String categoria;
    
    @Schema(description = "Autor del artículo")
    private String author;
    
    @Schema(description = "Fecha de publicación")
    private LocalDate fecha;
    
    @Schema(description = "Tiempo estimado de lectura")
    private String readTime;
    
    @Schema(description = "URL de la imagen de portada")
    private String image;
    
    @Schema(description = "Código de gradiente para tema visual")
    private String gradient;
    
    @Schema(description = "Si el artículo está marcado como destacado")
    private Boolean featured;
    
    @Schema(description = "Cantidad de likes del artículo", example = "0")
    private Integer likes;
    
    @Schema(description = "Etiquetas asociadas al artículo")
    private List<String> tags;
    
    @Schema(description = "Cantidad de vistas del artículo", example = "0")
    private Integer views;
    
    // Getters y Setters...
}
```

### Servicio de Blog
```java
@Service
public class BlogService {
    @Autowired
    private BlogRepository repository;

    public List<Blog> getBlogs() { /* ... */ }
    public Blog getBlogByID(Long id) { /* ... */ }
    public List<Blog> getBlogByNombre(String autor) { /* ... */ }
    
    public Blog saveBlog(Blog o) { /* ... */ }
    public List<Blog> saveAllBlogs(List<Blog> blogs) { /* ... */ }
    
    public Boolean deleteBlog(Blog o) { /* ... */ }
    public Boolean deleteBlogById(Long id) { /* ... */ }
    
    // Métodos para interacción pública
    public Blog incrementViews(Long id) { /* incrementa views en +1 */ }
    public Blog incrementLikes(Long id) { /* incrementa likes en +1 */ }
    public Blog decrementLikes(Long id) { /* decrementa likes en -1 (min 0) */ }
}
```
    NOTICIAS,
    TUTORIALES
}
```

---

## CATEGORÍA

### Descripción
Entidad que representa las categorías de productos disponibles.

### Estructura JSON
```json
{
  "id": 1,
  "nombre": "Consolas",
  "slug": "consolas",
  "descripcion": "Las últimas consolas de videojuegos",
  "icono": "🎮"
}
```

### Campos
| Campo | Tipo | Descripción | Requerido |
|-------|------|-------------|-----------|
| `id` | Long | Identificador único | ✅ |
| `nombre` | String | Nombre de la categoría | ✅ |
| `slug` | String | URL amigable (consolas, perifericos, etc) | ✅ |
| `descripcion` | String | Descripción de la categoría | ❌ |
| `icono` | String | Emoji o URL del icono | ❌ |

### Clase Java Equivalente
```java
@Entity
@Table(name = "categorias")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String slug;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(nullable = true)
    private String icono;
    
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos = new ArrayList<>();
    
    // Getters y Setters...
}
```

---

## REGIÓN/COMUNA

### Descripción
Entidad que representa las regiones y comunas de Chile para el envío de órdenes.

### Estructura JSON
```json
{
  "id": "metropolitana",
  "nombre": "Región Metropolitana de Santiago",
  "comunas": [
    "Santiago", "Cerrillos", "Cerro Navia", "Conchalí",
    "El Bosque", "Estación Central", "Huechuraba", "Independencia"
  ]
}
```

### Campos
| Campo | Tipo | Descripción | Requerido |
|-------|------|-------------|-----------|
| `id` | String | ID único de la región | ✅ |
| `nombre` | String | Nombre completo de la región | ✅ |
| `comunas` | List\<String\> | Lista de comunas en la región | ✅ |

### Endpoints para Región
- `GET /api/v1/regiones` — Listar todas las regiones (público)
- `GET /api/v1/regiones/{id}` — Obtener región por ID (público)
- `GET /api/v1/regiones/{id}/comunas` — Obtener comunas de una región (público)
- `GET /api/v1/regiones/comunas` — Obtener todas las comunas del catálogo (público)
- `GET /api/v1/regiones/search?city={city}` — Buscar región por ciudad (público)
- `GET /api/v1/regiones/validate?regionId={id}&comuna={comuna}` — Validar si una comuna pertenece a una región (público)
- `GET /api/v1/regiones/validate-city?city={city}` — Validar existencia de ciudad (público)
- `POST /api/v1/regiones` — Crear región (requiere rol `ADMIN`)
- `POST /api/v1/regiones/bulk` — Crear múltiples regiones (requiere rol `ADMIN`)
- `PUT /api/v1/regiones/{id}` — Actualizar región (requiere rol `ADMIN`)
- `DELETE /api/v1/regiones/{id}` — Eliminar región (requiere rol `ADMIN`)

### Regiones disponibles
1. `arica` - Región de Arica y Parinacota
2. `tarapaca` - Región de Tarapacá
3. `antofagasta` - Región de Antofagasta
4. `atacama` - Región de Atacama
5. `coquimbo` - Región de Coquimbo
6. `valparaiso` - Región de Valparaíso
7. `metropolitana` - Región Metropolitana de Santiago
8. `libertador` - Región del Libertador General Bernardo O'Higgins
9. `maule` - Región del Maule
10. `nuble` - Región de Ñuble
11. `biobio` - Región del Biobío
12. `araucania` - Región de La Araucanía
13. `rios` - Región de Los Ríos
14. `lagos` - Región de Los Lagos
15. `aysen` - Región de Aysén
16. `magallanes` - Región de Magallanes

### Clase Java Equivalente
```java
@Entity
@Table(name = "region")
public class RegionEntity {
    
    @Id
    private String id;
    
    @Column(nullable = false)
    private String nombre;
    
    @ElementCollection
    @Column(name = "comuna")
    private List<String> comunas = new ArrayList<>();
    
    // Getters y Setters...
}
```

---

## SESIÓN ADMIN

### Descripción
Entidad que representa la sesión de administrador (basada en datos del AuthContext).

### Estructura JSON
```json
{
  "isAuthenticated": true,
  "username": "admin",
  "role": "admin",
  "expiresAt": 1704067200000,
  "createdAt": 1703462400000
}
```

### Campos
| Campo | Tipo | Descripción | Requerido |
|-------|------|-------------|-----------|
| `isAuthenticated` | Boolean | Si la sesión es válida | ✅ |
| `username` | String | Nombre de usuario admin | ✅ |
| `role` | String | Rol del usuario (admin, user) | ✅ |
| `expiresAt` | Long | Timestamp de expiración (7 días) | ✅ |
| `createdAt` | Long | Timestamp de creación | ✅ |

### Clase Java Equivalente
```java
@Entity
@Table(name = "sesiones_admin")
public class SesionAdmin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String username;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RolUsuario rol;
    
    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;
    
    @Column(nullable = false)
    private Boolean activa = true;
    
    // Getters y Setters...
}

public enum RolUsuario {
    ADMIN,
    USER,
    GUEST
}
```

---

## RESUMEN DE RELACIONES

```
Usuario (1) ──────→ (N) Orden
Usuario (1) ──────→ (1) Carrito
Usuario (1) ──────→ (N) ArticuloBlog (como autor)

Orden (1) ──────→ (N) OrdenItem
Carrito (1) ──────→ (N) CarritoItem

OrdenItem (N) ──────→ (1) Producto
CarritoItem (N) ──────→ (1) Producto

Producto (N) ──────→ (1) Categoria

Region (1) ──────→ (N) Comuna (implícito en lista)

SesionAdmin (N) ──────→ (1) Usuario (implícito)
```

---

## CONSIDERACIONES PARA SPRING BOOT

### Base de Datos
Se recomienda usar **MySQL 8.0+** o **PostgreSQL 13+**

### Dependencias Maven necesarias
```xml
<!-- Spring Data JPA -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- MySQL Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Lombok (opcional, para reducir código) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

### Configuración application.properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/levelup_gamer
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### Recomendaciones de Seguridad
1. **Contraseñas**: Usar BCryptPasswordEncoder
2. **Tokens JWT**: Para autenticación en API
3. **CORS**: Configurar correctamente para el frontend
4. **Validación de entrada**: Usar @Valid y custom validators
5. **SQL Injection**: JPA parametriza automáticamente

---

## DATOS DE EJEMPLO INICIALES

Se proporcionan 20 productos de ejemplo en productos.json
Se proporcionan 2 usuarios de ejemplo en usuarios.json
Se proporcionan 8 artículos de blog de ejemplo en blogs.json
Se proporcionan 16 regiones con comunas en chileRegions.js

---

## PRÓXIMOS PASOS

1. Crear las clases Entity en Spring Boot según las especificaciones
2. Crear los repositorios (Repository) para cada entidad
3. Crear los DTOs (Data Transfer Objects) para las transacciones
4. Implementar los servicios (Service Layer)
5. Crear los controladores REST (Controller Layer)
6. Configurar la seguridad y autenticación
7. Migrar los datos JSON a la base de datos

---

**Última actualización**: Noviembre 2025
**Documentación preparada para**: Integración con API Spring Boot
