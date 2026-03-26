# Guia de Pruebas de API con Postman - NexusBackEnd

**Autor:** Henry Wong (hwongu@gmail.com)
**Version:** 1.5.0
**Entorno de Pruebas:** `http://localhost:8080/NexusBE/api`

## Introduccion

Este documento proporciona una guia detallada para la ejecucion de pruebas funcionales sobre la API REST de la aplicacion NexusBackEnd. Cada seccion corresponde a un recurso de la API y detalla las operaciones disponibles, los metodos HTTP, las cabeceras, los cuerpos de solicitud de ejemplo y los codigos de respuesta esperados. El objetivo es validar la correcta implementacion de la logica de negocio y el manejo de datos en cada endpoint.

La URL base utilizada en esta guia se construye a partir de las propiedades `server.port` y `server.context-path` definidas en el archivo `config.properties`. Con la configuracion actual (`server.port=8080` y `server.context-path=NexusBE/api`), la URL base resultante es `http://localhost:8080/NexusBE/api`. Si estas propiedades son modificadas o sobrescritas mediante variables de entorno, se debe reemplazar la URL base de todos los ejemplos por el nuevo valor resultante.

---

## 1. Modulo de Gestion de Categorias

Este modulo gestiona las operaciones CRUD para las categorias de productos.

### 1.1. Listar todas las categorias
- **Operacion:** Obtener una lista de todas las categorias existentes.
- **URL:** `http://localhost:8080/NexusBE/api/categorias`
- **Metodo:** `GET`
- **Cabeceras:** No se requieren cabeceras adicionales.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `200 OK`: La solicitud se proceso correctamente y la respuesta contiene la lista de categorias.

### 1.2. Obtener una categoria por ID
- **Operacion:** Buscar y obtener una categoria especifica utilizando su identificador unico.
- **URL:** `http://localhost:8080/NexusBE/api/categorias/1`
- **Metodo:** `GET`
- **Cabeceras:** No se requieren cabeceras adicionales.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `200 OK`: Se encontro la categoria y se devuelve en el cuerpo de la respuesta.
  - `404 Not Found`: No existe una categoria con el ID proporcionado.

### 1.3. Crear una nueva categoria
- **Operacion:** Registrar una nueva categoria en el sistema.
- **URL:** `http://localhost:8080/NexusBE/api/categorias`
- **Metodo:** `POST`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):**
  ```json
  {
    "nombre": "Monitores y Pantallas",
    "descripcion": "Dispositivos de visualizacion para computadoras de escritorio y laptops."
  }
  ```
- **Respuesta Exitosa (Body):**
  ```json
  {
    "idCategoria": 5,
    "nombre": "Monitores y Pantallas",
    "descripcion": "Dispositivos de visualizacion para computadoras de escritorio y laptops."
  }
  ```
- **Codigos de Respuesta:**
  - `201 Created`: La categoria se creo exitosamente. El cuerpo de la respuesta contiene el objeto de la categoria creada, incluyendo su nuevo `idCategoria`.
  - `500 Internal Server Error`: Ocurrio un error en la base de datos durante la insercion.

### 1.4. Actualizar una categoria existente
- **Operacion:** Modificar los datos de una categoria existente.
- **URL:** `http://localhost:8080/NexusBE/api/categorias/1`
- **Metodo:** `PUT`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):**
  ```json
  {
    "nombre": "Laptops de Alto Rendimiento",
    "descripcion": "Equipos portatiles optimizados para gaming y diseno profesional."
  }
  ```
- **Codigos de Respuesta:**
  - `200 OK`: La categoria se actualizo correctamente.
  - `404 Not Found`: No se encontro la categoria a actualizar.

### 1.5. Eliminar una categoria
- **Operacion:** Eliminar una categoria del sistema por su ID.
- **URL:** `http://localhost:8080/NexusBE/api/categorias/1`
- **Metodo:** `DELETE`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `204 No Content`: La categoria se elimino exitosamente.
  - `404 Not Found`: No se encontro la categoria a eliminar.
  - `409 Conflict`: La categoria no se puede eliminar porque tiene productos asociados.

---

## 2. Modulo de Gestion de Productos

Este modulo gestiona las operaciones CRUD para los productos.

### 2.1. Listar todos los productos
- **Operacion:** Obtener una lista completa de productos con su informacion de categoria.
- **URL:** `http://localhost:8080/NexusBE/api/productos`
- **Metodo:** `GET`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `200 OK`: Solicitud exitosa.

### 2.2. Crear un nuevo producto
- **Operacion:** Registrar un nuevo producto, asociandolo a una categoria existente.
- **URL:** `http://localhost:8080/NexusBE/api/productos`
- **Metodo:** `POST`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):** (Asume que una categoria con `idCategoria: 1` ya existe)
  ```json
  {
    "idCategoria": 1,
    "nombre": "Teclado Mecanico RGB",
    "precio": 89.99,
    "stock": 150
  }
  ```
- **Respuesta Exitosa (Body):**
  ```json
  {
    "idProducto": 3,
    "idCategoria": 1,
    "nombreCategoria": "Perifericos",
    "nombre": "Teclado Mecanico RGB",
    "precio": 89.99,
    "stock": 150
  }
  ```
- **Codigos de Respuesta:**
  - `201 Created`: Producto creado exitosamente. La respuesta contiene el DTO del producto creado.
  - `500 Internal Server Error`: Error en la base de datos.

### 2.3. Actualizar un producto existente
- **Operacion:** Modificar los datos de un producto por su ID.
- **URL:** `http://localhost:8080/NexusBE/api/productos/1`
- **Metodo:** `PUT`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):**
  ```json
  {
    "idCategoria": 1,
    "nombre": "Teclado Mecanico RGB (Switch Blue)",
    "precio": 95.50,
    "stock": 140
  }
  ```
- **Codigos de Respuesta:**
  - `200 OK`: Producto actualizado.
  - `404 Not Found`: Producto no encontrado.

### 2.4. Eliminar un producto
- **Operacion:** Eliminar un producto del sistema por su ID.
- **URL:** `http://localhost:8080/NexusBE/api/productos/1`
- **Metodo:** `DELETE`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `204 No Content`: Producto eliminado.
  - `404 Not Found`: Producto no encontrado.
  - `409 Conflict`: El producto no se puede eliminar porque esta referenciado en un ingreso.

---

## 3. Modulo de Gestion de Usuarios

Este modulo gestiona las operaciones CRUD y de autenticacion para los usuarios del sistema.

### 3.1. Autenticar Usuario (Login)
- **Operacion:** Validar las credenciales de un usuario y obtener sus datos.
- **URL:** `http://localhost:8080/NexusBE/api/usuarios/login`
- **Metodo:** `POST`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):**
  ```json
  {
    "username": "admin_nexus",
    "password": "admin_password_123"
  }
  ```
- **Codigos de Respuesta:**
  - `200 OK`: Autenticacion exitosa. La respuesta contiene los datos del usuario (sin contrasena).
  - `401 Unauthorized`: Credenciales invalidas.

### 3.2. Listar todos los usuarios
- **Operacion:** Obtener una lista de todos los usuarios (sin contrasenas).
- **URL:** `http://localhost:8080/NexusBE/api/usuarios`
- **Metodo:** `GET`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `200 OK`: Solicitud exitosa.

### 3.3. Obtener Usuario por ID
- **Operacion:** Buscar y obtener un usuario especifico utilizando su identificador unico.
- **URL:** `http://localhost:8080/NexusBE/api/usuarios/1`
- **Metodo:** `GET`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `200 OK`: Se encontro el usuario y se devuelve su DTO (sin contrasena).
  - `404 Not Found`: No existe un usuario con el ID proporcionado.

### 3.4. Crear un nuevo usuario
- **Operacion:** Registrar un nuevo usuario en el sistema.
- **URL:** `http://localhost:8080/NexusBE/api/usuarios`
- **Metodo:** `POST`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):**
  ```json
  {
    "username": "operador_logistico",
    "password": "PasswordSeguro789",
    "estado": true
  }
  ```
- **Respuesta Exitosa (Body):**
  ```json
  {
    "idUsuario": 2,
    "username": "operador_logistico",
    "estado": true
  }
  ```
- **Codigos de Respuesta:**
  - `201 Created`: Usuario creado. La respuesta contiene el DTO del usuario (sin contrasena).
  - `500 Internal Server Error`: Si el nombre de usuario ya existe o hay un error de base de datos.

### 3.5. Actualizar un usuario
- **Operacion:** Modificar los datos de un usuario, incluyendo su contrasena o estado.
- **URL:** `http://localhost:8080/NexusBE/api/usuarios/1`
- **Metodo:** `PUT`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):**
  ```json
  {
    "username": "admin_nexus_updated",
    "password": "NuevoPasswordSuperSeguro456",
    "estado": false
  }
  ```
- **Codigos de Respuesta:**
  - `200 OK`: Usuario actualizado.
  - `404 Not Found`: Usuario no encontrado.

### 3.6. Eliminar un usuario
- **Operacion:** Eliminar un usuario del sistema por su ID.
- **URL:** `http://localhost:8080/NexusBE/api/usuarios/1`
- **Metodo:** `DELETE`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `204 No Content`: Usuario eliminado.
  - `404 Not Found`: Usuario no encontrado.
  - `409 Conflict`: El usuario no se puede eliminar porque tiene ingresos registrados a su nombre.

---

## 4. Modulo de Gestion de Ingresos

Este modulo gestiona el registro y anulacion de ingresos de mercancia.

### 4.1. Listar todos los ingresos
- **Operacion:** Obtener un historial de todos los ingresos registrados.
- **URL:** `http://localhost:8080/NexusBE/api/ingresos`
- **Metodo:** `GET`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `200 OK`: Solicitud exitosa.

### 4.2. Obtener detalles de un ingreso
- **Operacion:** Obtener la lista de productos y cantidades de un ingreso especifico.
- **URL:** `http://localhost:8080/NexusBE/api/ingresos/1/detalles`
- **Metodo:** `GET`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `200 OK`: Solicitud exitosa.
  - `404 Not Found`: Ingreso no encontrado.

### 4.3. Registrar un nuevo ingreso (Transaccional)
- **Operacion:** Crear un nuevo registro de ingreso con su cabecera y una lista de detalles.
- **URL:** `http://localhost:8080/NexusBE/api/ingresos`
- **Metodo:** `POST`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):** (Asume que el usuario con `idUsuario: 1` y los productos con `idProducto: 1` y `2` existen)
  ```json
  {
    "ingreso": {
      "idUsuario": 1,
      "fechaIngreso": "2024-05-21T10:30:00",
      "estado": "RECIBIDO"
    },
    "detalles": [
      {
        "idProducto": 1,
        "cantidad": 10,
        "precioCompra": 1450.00
      },
      {
        "idProducto": 2,
        "cantidad": 25,
        "precioCompra": 89.90
      }
    ]
  }
  ```
- **Codigos de Respuesta:**
  - `201 Created`: Ingreso y detalles registrados, y stock de productos actualizado.
  - `500 Internal Server Error`: Error durante la transaccion (se debe realizar un rollback).

### 4.4. Anular un ingreso (Transaccional)
- **Operacion:** Cambiar el estado de un ingreso a 'ANULADO' y revertir el stock de los productos asociados.
- **URL:** `http://localhost:8080/NexusBE/api/ingresos/1`
- **Metodo:** `DELETE`
- **Cabeceras:** No se requieren.
- **Cuerpo (Body):** No aplica.
- **Codigos de Respuesta:**
  - `200 OK`: Ingreso anulado y stock revertido.
  - `404 Not Found`: Ingreso no encontrado.
  - `500 Internal Server Error`: Error durante la transaccion de anulacion.

### 4.5. Actualizar estado de un ingreso
- **Operacion:** Cambiar el estado de un ingreso a un valor especifico (ej. 'PROCESADO', 'COMPLETADO').
- **URL:** `http://localhost:8080/NexusBE/api/ingresos/1/estado`
- **Metodo:** `PUT`
- **Cabeceras:**
  - `Content-Type`: `application/json`
- **Cuerpo (Body):**
  ```json
  {
    "estado": "PROCESADO"
  }
  ```
- **Codigos de Respuesta:**
  - `200 OK`: El estado del ingreso se actualizo correctamente.
  - `400 Bad Request`: El cuerpo de la solicitud es invalido o falta el campo 'estado'.
  - `404 Not Found`: No se encontro un ingreso con el ID proporcionado.
  - `500 Internal Server Error`: Ocurrio un error en el servidor durante la actualizacion.
