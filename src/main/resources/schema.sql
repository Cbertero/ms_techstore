-- =====================================================
-- schema.sql  -  Tienda de periféricos
-- Motor: MySQL 8.0.16+ (CHECK constraints activas)
-- =====================================================

-- Los scripts son idempotentes: se pueden ejecutar en cada arranque
-- sin borrar datos (CREATE TABLE IF NOT EXISTS + INSERT IGNORE en data.sql).
-- Para reiniciar la BD desde cero: DROP DATABASE tienda_perifericos; y volver a crearla.

-- -----------------------------------------------------
-- Tabla: usuario
-- El RUT es la clave primaria (formato: 12345678-5, sin puntos)
-- La contraseña se guarda como hash BCrypt (60 caracteres)
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS usuario (
    rut         VARCHAR(12)  NOT NULL,
    contrasena  VARCHAR(100) NOT NULL,
    rol         VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_usuario PRIMARY KEY (rut),
    CONSTRAINT chk_usuario_rol
        CHECK (rol IN ('ADMINISTRADOR', 'VENDEDOR', 'CLIENTE')),
    CONSTRAINT chk_usuario_rut_formato
        CHECK (rut REGEXP '^[0-9]{7,8}-[0-9Kk]$')
);

-- -----------------------------------------------------
-- Tabla: producto
-- Precio en pesos chilenos (CLP), sin decimales
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS producto (
    id           BIGINT        NOT NULL AUTO_INCREMENT,
    nombre       VARCHAR(100)  NOT NULL,
    descripcion  VARCHAR(500),
    categoria    VARCHAR(50)   NOT NULL,
    precio       INT           NOT NULL,
    stock        INT           NOT NULL DEFAULT 0,
    CONSTRAINT pk_producto PRIMARY KEY (id),
    CONSTRAINT chk_producto_precio CHECK (precio > 0),
    CONSTRAINT chk_producto_stock  CHECK (stock >= 0)
);
