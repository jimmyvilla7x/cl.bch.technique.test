# Microservicio de Login y Manejo de Clientes


Este proyecto consiste en la creación de un microservicio backend utilizando Java 17 y Spring Boot 3, con maven para realizar las siguientes funciones principales:

1.-Validar las credenciales de los usuarios y generar un token JWT.

2.-Consultar información de clientes almacenada en un archivo JSON y obtención de token actualizado.

3.- Simulación de guardado información de clientes, validando las etapas del flujo a través del token JWT y campos  través de anotaciones de validaciones en los request

# Instalación del Proyecto
* Clona el repositorio del proyecto:
  * git clone https://github.com/jimmyvilla7x/cl.bch.technique.test.git

* Construye el proyecto utilizando Maven:
    * mvn clean install

* Ejecuta la aplicación:
  * mvn spring-boot:run
  
* Ejecuta Pruebas Unitarias:
  * mvn test

# Estructura de los Servicios

1. Endpoint de Login /cliente/login

**Nota:** Ingresar Rut y password valida para generar token, si es incorrecto te notifica el error y se puede bloquear usuario.
  > curl --location --request POST 'http://localhost:8080/cliente/login' \
     --header 'Content-Type: application/json' \
     --data-raw '{
     "rut": "19797848-1",
     "password": "4567"
     }'

2. Endpoint de Login /cliente/consulta/{idUsuario} 

**Nota:** Se pasa como parámetro Id Usuario + Header: Auth-x (con el token generado previamente), si Id Usuario no existe o Token es invalido se notifica.
  > curl --location --request GET 'http://localhost:8080/cliente/consulta/1' \
  --header 'Auth-x: eyJhbGciOiJIUzI1NiJ9.eyJydXQiOiIxOTc5Nzg0OC0xIiwibnJvSW50ZW50byI6MSwiYmxvcXVlbyI6ZmFsc2UsInNpZ3VpZW50ZV9ldGFwYSI6ImNvbnN1bHRhX2NsaWVudGUiLCJpYXQiOjE3Mzc0NzcxMjgsImV4cCI6MTczNzQ4MDcyOH0.0yltgiOBDSJ2zJ7HmK0RoetJk56iBxUYvntHzHg5e74'

3. Endpoint de Login /cliente/login

**Nota:** Se neceita Header: Auth-x (con el token generado previamente que viene en el campo NuevoToken + datos del cliente), se validan varios campos ver ClienteRq
  > curl --location --request POST 'http://localhost:8080/cliente/guardar' \
  --header 'Auth-x: eyJhbGciOiJIUzI1NiJ9.eyJydXQiOiIxMjUwODM4Ny0wIiwibnJvSW50ZW50byI6MSwiYmxvcXVlbyI6ZmFsc2UsInNpZ3VpZW50ZV9ldGFwYSI6Imd1YXJkYXJfY2xpZW50ZSIsImlhdCI6MTczNzQ3NzE0MiwiZXhwIjoxNzM3NDgwNzQyfQ.ZgzgroAAIXFkNcNfJy4L9KRPtsHsVFVSrlNvTxJf96Y' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "rut": "12508387-1",
  "first_name": "test",
  "last_name": "test",
  "date_birth": "1996-05-10",
  "mobile_phone": "213212656",
  "email": "test@gmail.com",
  "address": null,
  "city_id": null,
  "session_active": true,
  "password": "1234"
  }'
  
Atte.
Jimmy Villa