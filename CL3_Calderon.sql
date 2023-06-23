SET GLOBAL time_zone = '+8:00';
-- borra la bd si existe
DROP DATABASE IF EXISTS CL3_Calderon;

-- Crear la base de datos
CREATE DATABASE CL3_Calderon;

-- Usar la base de datos
USE CL3_Calderon;

-- Tabla para productos
CREATE TABLE producto (
  id INT PRIMARY KEY AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion VARCHAR(255) NOT NULL,
  fecha_registro DATE DEFAULT(CURRENT_DATE()) NOT NULL
);

select * from producto