-- ==============================================================================
-- Archivo: init.sql
-- Autor: Henry Wong
-- Correo: hwongu@gmail.com
-- Descripción: 
-- Este script inicializa mi base de datos 'nexus_db' de forma automática cuando 
-- levanto el contenedor de Docker por primera vez. Aquí configuro la extensión 
-- para búsquedas insensibles a mayúsculas/minúsculas, creo la estructura completa 
-- de mis tablas relacionales (Categoría, Usuario, Producto, Ingreso y Detalle) 
-- e inserto la data semilla, asegurando que solo exista mi usuario 'hwongu' 
-- para iniciar sesión.
-- ==============================================================================

-- ==========================================
-- 1. CONFIGURACIÓN DE EXTENSIONES
-- ==========================================

-- Habilito la extensión citext para que mis búsquedas de texto ignoren si escribo en mayúsculas o minúsculas.
CREATE EXTENSION IF NOT EXISTS citext;

-- ==========================================
-- 2. CREACIÓN DE ESTRUCTURA (TABLAS)
-- ==========================================

-- Creo mi tabla 'categoria' para clasificar los productos de mi almacén.
-- Utilizo el tipo CITEXT en el nombre para que la búsqueda sea indistinta (ej: "Laptops" = "laptops").
CREATE TABLE categoria (
    id_categoria INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre CITEXT NOT NULL,
    descripcion TEXT
);

-- Creo mi tabla 'usuario' para controlar el acceso al sistema.
-- Uso CITEXT en el username para que mis alumnos puedan loguearse sin preocuparse por las mayúsculas.
CREATE TABLE usuario (
    id_usuario INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username CITEXT UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    estado BOOLEAN DEFAULT TRUE
);

-- Creo mi tabla 'producto' vinculada a una categoría mediante una llave foránea.
CREATE TABLE producto (
    id_producto INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_categoria INT NOT NULL,
    nombre CITEXT NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id_categoria)
);

-- Creo mi tabla 'ingreso' para registrar cuándo y quién recibe la mercadería.
CREATE TABLE ingreso (
    id_ingreso INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_usuario INT NOT NULL,
    fecha_ingreso TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(20) DEFAULT 'COMPLETADO',
    CONSTRAINT fk_ingreso_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

-- Creo mi tabla 'detalle_ingreso' para romper la relación muchos a muchos entre mi ingreso y el producto.
CREATE TABLE detalle_ingreso (
    id_detalle INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_ingreso INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_compra DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_detalle_ingreso FOREIGN KEY (id_ingreso) REFERENCES ingreso(id_ingreso),
    CONSTRAINT fk_detalle_producto FOREIGN KEY (id_producto) REFERENCES producto(id_producto)
);

-- ==========================================
-- 3. INSERCIÓN DE DATOS DE EJEMPLO (SEED DATA)
-- ==========================================

-- Inserto mi único usuario administrador con las credenciales exactas que solicité.
INSERT INTO usuario (username, password, estado) 
VALUES ('hwongu', 'clave', true);

-- Inserto algunas categorías iniciales para tener con qué trabajar en mis laboratorios.
INSERT INTO categoria (nombre, descripcion) 
VALUES 
('Laptops', 'Computadoras portátiles de alto rendimiento'),
('Periféricos', 'Teclados, ratones y accesorios');

-- Registro mis primeros productos asociándolos a las categorías que acabo de crear.
INSERT INTO producto (id_categoria, nombre, precio, stock) 
VALUES 
(1, 'Laptop Lenovo ThinkPad T14', 1200.50, 50),
(1, 'MacBook Pro M3', 2500.00, 20),
(2, 'Mouse Inalámbrico Logitech MX Master 3', 99.99, 100);

-- Genero un ingreso de prueba asignado a mi usuario administrador (hwongu, id=1).
INSERT INTO ingreso (id_usuario, estado) 
VALUES (1, 'COMPLETADO');

-- Detallo los productos que entraron en mi ingreso de prueba, justificando el stock inicial que tienen.
INSERT INTO detalle_ingreso (id_ingreso, id_producto, cantidad, precio_compra) 
VALUES 
(1, 1, 50, 1000.00), -- Compré 50 Laptops Lenovo
(1, 2, 20, 2200.00), -- Compré 20 MacBooks
(1, 3, 100, 75.00);  -- Compré 100 Mouses Logitech