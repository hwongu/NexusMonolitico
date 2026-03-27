# 🚀 Proyecto Nexus – Sistema de Gestión de Almacén (Arquitectura Monolítica)

Este repositorio contiene el código fuente, la configuración de la base de datos y la infraestructura para el sistema **Nexus**. Este proyecto ha sido diseñado como material educativo y de ejemplo para clases universitarias, demostrando cómo construir una aplicación completa (Full-Stack) utilizando tecnologías puras y estándares de la industria, sin depender inicialmente de frameworks pesados en el backend.

El sistema implementa:
* **Mantenimientos (CRUD):** Usuarios, Productos y Categorías.
* **Transacciones:** Registro de ingresos a almacén con actualización dinámica del stock de productos.

---

## 📁 Estructura del Repositorio

El proyecto está dividido en cuatro grandes módulos principales:

### 🗄️ 1_DataBase (Persistencia)
Contiene la infraestructura de datos dockerizada y los scripts de inicialización.
* **NexusDb:** Directorio que aloja el archivo `docker-compose.yml` para levantar **PostgreSQL 18** y los scripts SQL necesarios para crear las tablas base del sistema.

### ☕ 2_BackEnd (Lógica de Negocio y API)
Contiene el núcleo del sistema expuesto a través de servicios REST.
* **NexusBackEnd:** Proyecto construido con **Java 21** puro. Implementa un servidor HTTP nativo (`com.sun.net.httpserver.HttpServer`), gestión de dependencias con **Maven**, manejo de JSON con **Gson**, y acceso a datos mediante **JDBC** tradicional. Incluye pruebas unitarias con JUnit 5 y Mockito.

### 💻 3_FrontEnd (Interfaz de Usuario)
Contiene la aplicación cliente con la que interactúan los usuarios finales.
* **NexusFrontEnd:** Proyecto Single Page Application (SPA) desarrollado en **Angular 19**. Incluye scripts `.bat` personalizados para facilitar la compilación y generación de instaladores en entornos Windows.

### 🐳 4_Infraestructure (Despliegue)
Contiene los directorios de destino donde se unifican los artefactos compilados para levantar toda la infraestructura del sistema.
* **deploy:** Carpeta de destino para el ejecutable del backend.
* **frontend:** Carpeta de destino para los archivos estáticos compilados del frontend.

---

## ⚠️ Reglas de Oro (Para despliegue)

Para garantizar que el entorno local funcione correctamente, el orden de ejecución es fundamental:

1.  **La Base de Datos manda:** El contenedor de la base de datos es el corazón del sistema. **Siempre debe estar encendido primero** antes de intentar levantar o probar el backend.
2.  **Construcción independiente:** Asegúrate de compilar el backend (generando el `.jar`) y el frontend (generando el directorio `dist`) antes de intentar un despliegue completo en la carpeta de infraestructura.

---

## 🚀 Guía de Ejecución Paso a Paso

### 🗄️ Fase 0: Levantar la Base de Datos (Ambiente de Desarrollo)
El entorno de datos debe ser lo primero en inicializarse para probar tu código localmente.
1. Navega a la ruta: `1_DataBase\NexusDb`
2. Levanta la base de datos y ejecuta los scripts con el comando:
   `docker-compose up -d`
3. Cuando termines de trabajar en desarrollo, y **especialmente antes de pasar a la Fase 3**, asegúrate de destruir este entorno ejecutando:
   `docker-compose down -v`

### ☕ Fase 1: Compilar el Backend (Java 21)
Preparamos los servicios REST y empaquetamos el proyecto.
1. Navega a la ruta: `2_BackEnd\NexusBackEnd`
2. Genera el instalador y descarga las dependencias ejecutando:
   `mvn clean install`
3. Esto generará un archivo empaquetado. Identifica el archivo `NexusBackEnd-1.0.0-jar-with-dependencies.jar` dentro de la carpeta `target/`.

### 💻 Fase 2: Compilar el Frontend (Angular 19)
Preparamos la interfaz de usuario para producción.
1. Navega a la ruta: `3_FrontEnd\NexusFrontEnd`
2. Para desarrollo o pruebas de compilación rápida, puedes usar:
   `compilar.bat`
3. Para generar la versión final optimizada, ejecuta:
   `generar-instalador-produccion.bat`
4. Esto creará una carpeta `dist/`. Entra allí e identifica el contenido dentro de la subcarpeta `NexusFrontEnd/browser`.

### 🐳 Fase 3: Preparar la Infraestructura Completa (Despliegue Total)
**⚠️ Importante:** Este paso es únicamente si deseas levantar *toda* la infraestructura del sistema en conjunto. Si vas a hacer esto, la base de datos levantada en la **Fase 0** debe estar **destruida** (`docker-compose down -v`), ya que esa solo se utiliza para simular un ambiente de desarrollo local.

1. **Para el Backend:** Copia el archivo `NexusBackEnd-1.0.0-jar-with-dependencies.jar` (obtenido en la Fase 1) y pégalo dentro de la carpeta `4_Infraestructure\deploy`.
2. **Para el Frontend:** Copia **todo el contenido** que está dentro de `dist/NexusFrontEnd/browser` (obtenido en la Fase 2) y pégalo dentro de la carpeta `4_Infraestructure\frontend`.

*(Una vez ubicados los archivos en la Fase 3, el sistema estará listo para ser levantado por el orquestador de infraestructura principal).*

---

## 🛠️ Stack Tecnológico

* **Backend:** Java 21 (Nativo), HttpServer, JDBC, Gson
* **Testing:** JUnit 5, Mockito
* **Gestor de Dependencias:** Maven
* **Frontend:** Angular 19
* **Base de Datos:** PostgreSQL 18
* **Infraestructura:** Docker & Docker Compose

---

**Autor:** [Henry Wong](https://github.com/hwongu)  
*Docente Universitario*

---

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-Build-C71A22?style=for-the-badge&logo=apachemaven)
![Angular](https://img.shields.io/badge/Angular-19-DD0031?style=for-the-badge&logo=angular)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-18-336791?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-blue?style=for-the-badge&logo=docker)
