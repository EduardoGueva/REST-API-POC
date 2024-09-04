# API REST básica con Spring

Este desarrollo se realizó para una POC. Es una API REST básica con Spring que proporciona operaciones CRUD para gestionar entidades `Cliente`. Utiliza RedisOM y se conecta a una base de datos definida en el archivo `application.properties`. No cuenta con carterísticas empresariales por lo que no debe utilizarse en entornos productivos. 

## Requisitos previos

Para ejecutar este proyecto, necesitas tener instalados los siguientes componentes:

- Java 17
- Maven 3.8 o superior
- Base de datos Redis

## Estructura del proyecto

El proyecto consta de los siguientes componentes clave:

- **PocApplication.java**: La clase principal que arranca la aplicación Spring Boot.
- **Controller.java**: El controlador REST que maneja las solicitudes HTTP y las dirige a los métodos de servicio apropiados.
- **Cliente.java**: La clase de entidad que representa la entidad `Cliente` en la base de datos.
- **RegionBolsa.java**: Una clase auxiliar para la funcionalidad de `RegionBolsa`.
- **ClienteRepository.java**: La interfaz del repositorio que extiende `RedisDocumentRepository` para las operaciones CRUD en las entidades `Cliente`.
- **application.properties**: El archivo de configuración que contiene los ajustes de conexión a la base de datos y otras propiedades.

## Comenzando

### Paso 1: Clonar el repositorio

```bash
git clone https://github.com/EduardoGueva/REST-API-POC.git
cd REST-API-POC
```

### Paso 2: Configurar la base de datos Redis

Abre el archivo `src/main/resources/application.properties` y actualiza las propiedades de conexión de la base de datos.

### Paso 3: Construir el proyecto

Usa Maven para construir el proyecto:

```bash
mvn clean install
```

### Paso 4: Ejecutar la aplicación

Después de construir el proyecto, puedes ejecutar la aplicación Spring Boot con el siguiente comando:

```bash
mvn spring-boot:run
```

### Paso 5: Acceder a la API

Accede a la interfaz de Swagger para probar la API en `http://<IP API>:8080/swagger-ui/index.html`
