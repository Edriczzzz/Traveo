set @@global.time_zone = '-5:00';
set @@session.time_zone = '-5:00';


USE dbtraveo;


/* Tabla de Usuarios (modificada para incluir campos relacionados con las reservas) */
CREATE TABLE Usuarios (
    ID_usuario INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Correo VARCHAR(100) UNIQUE NOT NULL,
    Telefono VARCHAR(15),
    Contrasena VARCHAR(255) NOT NULL,
    Fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    Fecha_nacimiento DATE,
    Asiento VARCHAR(5),
    Monto_pagado DECIMAL(10, 2),
    ID_vuelo INT,
    FOREIGN KEY (ID_vuelo) REFERENCES Vuelos(ID_vuelo)
);

ALTER TABLE Usuarios ADD COLUMN Rol ENUM('CLIENTE', 'ADMINISTRADOR') DEFAULT 'CLIENTE';

ALTER TABLE usuarios MODIFY COLUMN Monto_pagado FLOAT(53);

ALTER TABLE usuarios MODIFY COLUMN Fecha_registro DATE;

ALTER TABLE usuarios ADD COLUMN version INT DEFAULT 0;


/* Tabla de Aerolíneas */
CREATE TABLE Aerolineas (
    ID_aerolinea INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Codigo VARCHAR(10) UNIQUE NOT NULL,
    Pais VARCHAR(50) NOT NULL
);

/* Tabla de Ciudades */
CREATE TABLE Ciudades (
    ID_ciudad INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Codigo VARCHAR(10) UNIQUE NOT NULL,
    Pais VARCHAR(50) NOT NULL
);

/* Tabla de Rutas */
CREATE TABLE Rutas (
    ID_ruta INT PRIMARY KEY AUTO_INCREMENT,
    ID_ciudad_origen INT,
    ID_ciudad_destino INT,
    Distancia FLOAT,
    FOREIGN KEY (ID_ciudad_origen) REFERENCES Ciudades(ID_ciudad),
    FOREIGN KEY (ID_ciudad_destino) REFERENCES Ciudades(ID_ciudad)
);

/* Tabla de Vuelos */
CREATE TABLE Vuelos (
    ID_vuelo INT PRIMARY KEY AUTO_INCREMENT,
    Numero_vuelo VARCHAR(10) UNIQUE NOT NULL,
    ID_aerolinea INT,
    ID_ruta INT,
    Fecha_salida DATETIME,
    Fecha_llegada DATETIME,
    Precio DECIMAL(10, 2),
    Estado VARCHAR(20) NOT NULL,
    FOREIGN KEY (ID_aerolinea) REFERENCES Aerolineas(ID_aerolinea),
    FOREIGN KEY (ID_ruta) REFERENCES Rutas(ID_ruta)
);

ALTER TABLE vuelos MODIFY COLUMN Numero_vuelo VARCHAR(50) NOT NULL;


ALTER TABLE vuelos MODIFY COLUMN Fecha_salida datetime;
ALTER TABLE vuelos MODIFY COLUMN Fecha_llegada datetime;
ALTER TABLE vuelos MODIFY COLUMN precio float;

CREATE TABLE usuarios_vuelos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT NOT NULL,
    vuelo_id INT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(ID_usuario),
    FOREIGN KEY (vuelo_id) REFERENCES vuelos(ID_vuelo)
);

select * from usuarios_vuelos;

/* Tabla de Pagos 
CREATE TABLE Pagos (
    ID_pago INT PRIMARY KEY AUTO_INCREMENT,
    ID_usuario INT,
    Monto DECIMAL(10, 2) NOT NULL,
    Fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP,
    Metodo_pago VARCHAR(50) NOT NULL,
    Estado_pago VARCHAR(20) NOT NULL,
    FOREIGN KEY (ID_usuario) REFERENCES Usuarios(ID_usuario)
);*/

-- Usuarios
INSERT INTO Usuarios (Nombre, Correo, Telefono, Contrasena, Fecha_nacimiento, Asiento, Monto_pagado) 
VALUES ('Ana García', 'ana.garcia@email.com', '+34612345678', 'pass123', '1990-05-15', '12A', 350.00);

INSERT INTO Usuarios (Nombre, Correo, Telefono, Contrasena, Fecha_nacimiento, Asiento, Monto_pagado)
VALUES ('Carlos López', 'carlos.lopez@email.com', '+34687654321', 'pass456', '1985-10-20', '15F', 420.00);

-- Aerolíneas
INSERT INTO Aerolineas (Nombre, Codigo, Pais) 
VALUES ('Iberia', 'IBE', 'España');

INSERT INTO Aerolineas (Nombre, Codigo, Pais)
VALUES ('Air Europa', 'AEA', 'España');

-- Ciudades
INSERT INTO Ciudades (Nombre, Codigo, Pais)
VALUES ('Madrid', 'MAD', 'España');

INSERT INTO Ciudades (Nombre, Codigo, Pais)
VALUES ('Barcelona', 'BCN', 'España');

-- Rutas
INSERT INTO Rutas (ID_ciudad_origen, ID_ciudad_destino, Distancia)
VALUES (1, 2, 500.5);

INSERT INTO Rutas (ID_ciudad_origen, ID_ciudad_destino, Distancia)
VALUES (2, 1, 500.5);

-- Vuelos
INSERT INTO Vuelos (Numero_vuelo, ID_aerolinea, ID_ruta, Fecha_salida, Fecha_llegada, Precio, Estado)
VALUES ('IB1234', 1, 1, '2025-01-10 10:00:00', '2025-01-10 11:30:00', 150.00, 'Programado');

INSERT INTO Vuelos (Numero_vuelo, ID_aerolinea, ID_ruta, Fecha_salida, Fecha_llegada, Precio, Estado)
VALUES ('AE5678', 2, 2, '2025-01-10 15:00:00', '2025-01-10 16:30:00', 140.00, 'Programado');

-- Pagos
/*INSERT INTO Pagos (ID_usuario, Monto, Metodo_pago, Estado_pago)
VALUES (1, 350.00, 'Tarjeta de crédito', 'Completado');

INSERT INTO Pagos (ID_usuario, Monto, Metodo_pago, Estado_pago)
VALUES (2, 420.00, 'PayPal', 'Completado');*/

select * from vuelos;
select * from rutas;
DELETE FROM usuarios_vuelos
WHERE vuelo_id BETWEEN 4 AND 52;

DELETE FROM vuelos
WHERE id_vuelo BETWEEN 4 AND 52;

select * from rutas;

DELETE FROM rutas
WHERE ID_ruta BETWEEN 5 AND 28;


SET SQL_SAFE_UPDATES = 1;

select * from aerolineas;

select * from usuarios;

DESCRIBE usuarios;

select * from usuarios_vuelos;

insert into usuarios(Nombre, Correo, Telefono, Contrasena, Fecha_nacimiento, Asiento, Monto_pagado, version, rol)
values('admin','admin@mail.com', '(555) 555-5555', 'admin', '2025-01-06', '15F',0,0,'ADMINISTRADOR');

insert into usuarios(Nombre, Correo, Telefono, Contrasena, Fecha_nacimiento, Asiento, Monto_pagado, version, rol)
values('admin2','admin2@mail.com', '(555) 555-5555', 'admin', '2025-01-06', '15F',0,0,'ADMINISTRADOR');