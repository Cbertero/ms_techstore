-- =====================================================
-- data.sql  -  Datos iniciales
-- =====================================================

-- -----------------------------------------------------
-- Usuarios de prueba (contraseñas encriptadas con BCrypt, cost 10)
--   RUT          Rol             Contraseña en texto plano
--   11111111-1   ADMINISTRADOR   Admin123
--   12345678-5   VENDEDOR        Vendedor123
--   20456789-1   CLIENTE         Cliente123
-- -----------------------------------------------------
INSERT INTO usuario (rut, contrasena, rol) VALUES
('11111111-1', '$2a$10$8Mf7sfo1FXhZQjBqiBaUZ..t6B9VFoFmQrnxLCBGZH1CTQFKM055W', 'ADMINISTRADOR'),
('12345678-5', '$2a$10$C/CXY0yZSllnx6kQPGFsROsPkB.kpHJFE/XlCvLfEmMxBlbKCGc72', 'VENDEDOR'),
('20456789-1', '$2a$10$JtkXAb6fincfUsnjqN56Neb5bsUQ1kkeymYDzVTQqCFMqdVLWSEEy', 'CLIENTE');

-- -----------------------------------------------------
-- Productos: periféricos reales (precios referenciales en CLP)
-- -----------------------------------------------------
INSERT INTO producto (nombre, descripcion, categoria, precio, stock) VALUES
-- Mouse
('Logitech G502 HERO', 'Mouse gamer con sensor HERO 25K, 11 botones programables y pesas ajustables', 'Mouse', 44990, 25),
('Razer DeathAdder V3', 'Mouse ergonómico ultraligero de 59 g con sensor Focus Pro 30K', 'Mouse', 59990, 15),
('Logitech MX Master 3S', 'Mouse inalámbrico de productividad, clics silenciosos y scroll MagSpeed', 'Mouse', 89990, 12),

-- Teclados
('HyperX Alloy Origins', 'Teclado mecánico con switches HyperX Red y retroiluminación RGB', 'Teclado', 69990, 18),
('Redragon Kumara K552', 'Teclado mecánico TKL con switches Outemu Blue y cuerpo metálico', 'Teclado', 29990, 30),
('Logitech MX Keys S', 'Teclado inalámbrico de perfil bajo con retroiluminación inteligente', 'Teclado', 99990, 10),

-- Audífonos
('HyperX Cloud II', 'Audífonos gamer con sonido envolvente 7.1 virtual y micrófono desmontable', 'Audífonos', 74990, 20),
('Logitech G PRO X', 'Audífonos gamer con micrófono Blue VO!CE y drivers PRO-G de 50 mm', 'Audífonos', 99990, 8),
('Razer BlackShark V2 X', 'Audífonos gamer livianos con drivers TriForce de 50 mm', 'Audífonos', 44990, 22),

-- Monitores
('Samsung Odyssey G5 27"', 'Monitor curvo QHD 2560x1440, 165 Hz, 1 ms', 'Monitor', 249990, 6),
('LG UltraGear 27GN750', 'Monitor IPS Full HD 240 Hz, 1 ms, compatible con G-SYNC', 'Monitor', 279990, 5),

-- Cámaras web
('Logitech C920 HD Pro', 'Cámara web Full HD 1080p con micrófonos estéreo', 'Cámara web', 59990, 14),
('Razer Kiyo', 'Cámara web 1080p con anillo de luz integrado para streaming', 'Cámara web', 79990, 7),

-- Micrófonos
('HyperX QuadCast S', 'Micrófono USB de condensador con iluminación RGB y 4 patrones polares', 'Micrófono', 139990, 9),

-- Mousepads
('Logitech G240', 'Mousepad de tela para juegos, superficie de baja fricción', 'Mousepad', 14990, 40);
