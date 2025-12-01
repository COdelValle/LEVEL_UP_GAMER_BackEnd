# Tutorial: Conexión del Frontend con la API (APIHelper)

Este tutorial explica cómo conectar el frontend con el backend de LEVEL_UP_GAMER usando un `APIHelper` centralizado. Incluye ejemplos de autenticación (JWT y API Key), uso de `fetch`, manejo de errores comunes y recomendaciones sobre almacenamiento del token.

## Tabla de contenidos

- [Tutorial: Conexión del Frontend con la API (APIHelper)](#tutorial-conexión-del-frontend-con-la-api-apihelper)
  - [Tabla de contenidos](#tabla-de-contenidos)
  - [Objetivo](#objetivo)
  - [Requisitos](#requisitos)
- [Tutorial rápido: Conexión del Frontend con la API](#tutorial-rápido-conexión-del-frontend-con-la-api)

## Objetivo
- Proveer una clase sencilla y reutilizable (`APIHelper`) para realizar llamadas HTTP a la API.
- Facilitar el uso de `Authorization: Bearer {token}` y `X-API-Key: {apiKey}`.

## Requisitos
- Base URL por defecto: `http://localhost:8080` (ajustar según ambiente).
- Endpoints relevantes:
	- `POST /api/v1/auth/login` — login (retorna `token` y `apiKey`)
	- `POST /api/v1/auth/register` — registro

# Tutorial rápido: Conexión del Frontend con la API

Esta guía rápida muestra cómo integrar el frontend con la API backend usando un `APIHelper` ligero. Incluye un resumen de las rutas y los requisitos de seguridad por controlador.

Quickstart (2 pasos)
- 1) Importar la colección Postman: `docs/LEVEL_UP_GAMER.postman_collection.json`.
- 2) Usar el `APIHelper` para autenticar y hacer llamadas.

APIHelper mínimo (rápido) — implementación con `fetch`
```javascript
// src/lib/APIHelper.js (ejemplo con fetch)
export function createAPI(baseURL = 'http://localhost:8080') {
  let token = null;
  let apiKey = null;

  function setToken(t) { token = t; }
  function setApiKey(k) { apiKey = k; }

  async function request(method, path, data = null, opts = {}) {
    const url = new URL(baseURL + path);
    if (opts.params) {
      Object.keys(opts.params).forEach(k => url.searchParams.append(k, opts.params[k]));
    }

    const headers = Object.assign({}, opts.headers || {});
    if (token) headers['Authorization'] = `Bearer ${token}`;
    else if (apiKey) headers['X-API-Key'] = apiKey;
    if (data && !(data instanceof FormData)) headers['Content-Type'] = 'application/json';

    const res = await fetch(url.toString(), {
      method: method.toUpperCase(),
      headers,
      body: data && !(data instanceof FormData) ? JSON.stringify(data) : data
    });

    const text = await res.text();
    const content = text ? JSON.parse(text) : null;

    if (!res.ok) {
      const err = new Error(content?.message || `HTTP ${res.status}`);
      err.status = res.status;
      err.body = content;
      throw err;
    }

    return content;
  }

  async function login(email, password) {
    const data = await request('post', '/api/v1/auth/login', { email, password });
    if (data?.token) setToken(data.token);
    if (data?.apiKey) setApiKey(data.apiKey);
    return data;
  }

  return {
    setToken,
    setApiKey,
    login,
    request,
    get(path, opts) { return request('get', path, null, opts); },
    post(path, body, opts) { return request('post', path, body, opts); },
    put(path, body, opts) { return request('put', path, body, opts); },
    delete(path, opts) { return request('delete', path, null, opts); }
  };
}
```

Explicación paso a paso:

1) ¿Qué hace `createAPI`?
- `createAPI(baseURL)` crea un ayudante (helper) con funciones para hacer peticiones al backend. Devuelve un objeto con métodos: `setToken`, `setApiKey`, `login`, `get`, `post`, `put`, `delete`.

2) Almacenamiento de credenciales
- `setToken(t)` guarda el JWT en memoria dentro del helper. No se usa `localStorage` automáticamente: la app decide si guardar el token en `localStorage` o `sessionStorage`.
- `setApiKey(k)` guarda la API Key para integraciones máquina-a-máquina.

3) Cómo se construye una petición (`request`)
- Se construye la URL combinando `baseURL` y `path`. Si se pasan `opts.params`, se agregan como query string.
- Se crean los headers:
  - Si hay `token`, se añade `Authorization: Bearer {token}`.
  - Si no hay `token` pero hay `apiKey`, se añade `X-API-Key: {apiKey}`.
  - Si el body no es `FormData`, se pone `Content-Type: application/json`.
- Se llama a `fetch(url, { method, headers, body })`.

4) Manejo de la respuesta
- Se lee el texto de la respuesta y se intenta parsear JSON.
- Si `res.ok` es falso (status >= 400), se lanza un `Error` con `status` y `body` para que el frontend lo capture.

5) Login
- `login(email,password)` realiza `POST /api/v1/auth/login`, guarda `token` y `apiKey` si vienen en la respuesta y devuelve los datos.

6) Uso práctico en la UI
- En el arranque de la app, si se guarda el token en `localStorage`, recuperarlo y llamar `api.setToken(savedToken)`.
- Para llamar un endpoint protegido, usar `api.post('/api/v1/usuarios/bulk', payload)` después de haber hecho `login`.

7) Buenas prácticas y seguridad
- Guardar tokens en memoria reduce riesgo XSS; para PWA/SPA que necesitan persistencia, `httpOnly` cookies son preferibles.
- No exponer `apiKey` en clientes públicos (sólo usarla en servidores de confianza).
- Manejar `401` (re-login) y `403` (mostrar permiso insuficiente) en UI.

Resumen de controladores y requisitos de seguridad

- `AuthController` (`/api/v1/auth`)
  - `POST /register`, `POST /login`, `POST /validate` — públicos. Devuelven `token` y `apiKey`.

- `BlogController` (`/api/v1/blog`)
  - `GET /`, `GET /destacados`, `GET /autor/{autor}`, `GET /{id}` — público.
  - `POST /` — requiere `ADMIN` (en código: `hasAnyRole('ADMIN')`).
  - `POST /bulk`, `PUT /{id}`, `DELETE /{id}` — `ADMIN`.

- `ProductoController` (`/api/v1/productos`)
  - `GET /`, `GET /{id}` — requieren autenticación por la configuración global.
  - `POST /`, `PUT /{id}` — `ADMIN` o `SELLER`.
  - `POST /bulk`, `DELETE /{id}` — `ADMIN`.

- `OrdenController` (`/api/v1/ordenes`)
  - `GET /`, `GET /{id}` — autenticados.
  - `POST /` — `USER`, `ADMIN` o `SELLER`.
  - `POST /bulk`, `PUT /{id}`, `DELETE /{id}` — `ADMIN`.

- `UsuarioController` (`/api/v1/usuarios`)
  - `GET` (todos), `GET /{id}`, `GET /email/{email}` — autenticados.
  - `PUT /{id}` — autenticado; cambio de campo `rol` está restringido en runtime a `ROLE_ADMIN`.
  - `DELETE /{id}` — autenticado.
  - `POST /bulk` — `ADMIN` (anotación `@PreAuthorize('hasRole("ADMIN")')`).

Notas rápidas para el frontend
- Para endpoints públicos no se requiere token.
- Para llamadas protegidas usar `Authorization: Bearer {token}`; como alternativa `X-API-Key: {apiKey}` para integraciones servidor→servidor.
- `401` = no autenticado (token inválido/expirado). `403` = autenticado pero rol insuficiente.

Ejemplo: crear usuarios en masa (ADMIN)
```javascript
const api = createAPI();
await api.login('admin@local','adminpass');
await api.post('/api/v1/usuarios/bulk', [{nombre:'A',email:'a@x',password:'123456',rol:'USER'}]);
```

Recomendaciones de seguridad (rápido)
- Mantener tokens en memoria o `httpOnly` cookies.
- Preferir `X-API-Key` para servicios sin interacción humana.
- Implementar manejo centralizado de 401/403 para redirigir a login o mostrar mensajes.
