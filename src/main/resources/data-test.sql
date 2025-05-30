-- Insertar datos para la tabla Pasajeros
INSERT INTO pasajero (nombres, apellidos, correo, password, telefono, fecha_hora_registro, username) VALUES
                                                                                               ('Jose', 'Perez Perez', 'jose@gmail.com', 'jose123', '987654321', CURRENT_TIMESTAMP, 'josep'),
                                                                                               ('Maria', 'Gonzales Lopez', 'maria@gmail.com', 'maria456', '987654322', CURRENT_TIMESTAMP, 'mariaj'),
                                                                                               ('Carlos', 'Sanchez Ruiz', 'carlos@gmail.com', 'carlos789', '987654323', CURRENT_TIMESTAMP, 'carlosc'),
                                                                                               ('Luisa', 'Diaz Mendoza', 'luisa@gmail.com', 'luisa012', '987654324', CURRENT_TIMESTAMP, 'luisp'),
                                                                                               ('Pedro', 'Martinez Vega', 'pedro@gmail.com', 'pedro345', '987654325', CURRENT_TIMESTAMP, 'pedrot')
    ON CONFLICT DO NOTHING;

-- Insertar datos en la tabla conductor
INSERT INTO conductor (nombres, apellidos, correo, password, telefono, fecha_hora_registro, username) VALUES
                                                                                                ('Juan', 'Gomez Herrera', 'juan@gmail.com', 'juan123', '987654326', CURRENT_TIMESTAMP, 'juanf'),
                                                                                                ('Ana', 'Torres Flores', 'ana@gmail.com', 'ana456', '987654327', CURRENT_TIMESTAMP, 'anaw'),
                                                                                                ('Luis', 'Ramirez Castro', 'luis@gmail.com', 'luis789', '987654328', CURRENT_TIMESTAMP, 'luisy'),
                                                                                                ('Sofia', 'Vargas Paredes', 'sofia@gmail.com', 'sofia012', '987654329', CURRENT_TIMESTAMP,'sofiat'),
                                                                                                ('Miguel', 'Silva Rios', 'miguel@gmail.com', 'miguel345', '987654330', CURRENT_TIMESTAMP, 'miguelr')
    ON CONFLICT DO NOTHING;

-- Insertar datos en la tabla tarjeta
INSERT INTO tarjeta (numero_tarjeta, nombre_titular, correo, fecha_vencimiento, cvv, pasajero_id, conductor_id, saldo) VALUES
                                                                                                                    ('4111111111111111', 'Jose Perez', 'jose@gmail.com', '12/25', '123', 1, NULL, 100),
                                                                                                                    ('4222222222222222', 'Maria Gonzales', 'maria@gmail.com', '06/24', '456', 2, NULL, 100),
                                                                                                                    ('4333333333333333', 'Juan Gomez', 'juan@gmail.com', '09/26', '789', NULL, 1, 30),
                                                                                                                    ('4444444444444444', 'Ana Torres', 'ana@gmail.com', '03/25', '012', NULL, 2, 50),
                                                                                                                    ('4555555555555555', 'Carlos Sanchez', 'carlos@gmail.com', '11/24', '345', 3, NULL, 100),
                                                                                                                    ('4666666666666666', 'Luisa Diaz', 'luisa@gmail.com', '08/25', '678', 4, NULL, 100),
                                                                                                                    ('4777777777777777', 'Luis Ramirez', 'luis@gmail.com', '05/26', '901', NULL, 3, 55),
                                                                                                                    ('4888888888888888', 'Sofia Vargas', 'sofia@gmail.com', '10/24', '234', NULL, 4, 55),
                                                                                                                    ('4999999999999999', 'Pedro Martinez', 'pedro@gmail.com', '07/25', '567', 5, NULL, 70),
                                                                                                                    ('4000111122223333', 'Miguel Silva', 'miguel@gmail.com', '04/26', '890', NULL, 5, 50)
    ON CONFLICT DO NOTHING;

-- Insertar datos en la tabla notificacion
INSERT INTO notificacion (fecha_hora_envio, mensaje, leido, pasajero_id, conductor_id) VALUES
                                                                                           (CURRENT_TIMESTAMP, 'Tu viaje ha sido confirmado', false, 1, NULL),
                                                                                           (CURRENT_TIMESTAMP, 'Nuevo pasajero en tu viaje', false, NULL, 1),
                                                                                           (CURRENT_TIMESTAMP, 'Pago recibido correctamente', true, 2, NULL),
                                                                                           (CURRENT_TIMESTAMP, 'Recordatorio: Viaje mañana a las 8am', false, 3, NULL),
                                                                                           (CURRENT_TIMESTAMP, 'Califica tu último viaje', true, NULL, 2),
                                                                                           (CURRENT_TIMESTAMP, 'Promoción especial para conductores', false, NULL, 3),
                                                                                           (CURRENT_TIMESTAMP, 'Tu conductor está en camino', false, 4, NULL),
                                                                                           (CURRENT_TIMESTAMP, 'Nueva tarifa dinámica disponible', false, NULL, 4),
                                                                                           (CURRENT_TIMESTAMP, 'Viaje cancelado por el conductor', true, 5, NULL),
                                                                                           (CURRENT_TIMESTAMP, 'Felicitaciones por tus 50 viajes', false, NULL, 5)
    ON CONFLICT DO NOTHING;

-- Insertar datos en la tabla reclamo
INSERT INTO reclamo (fecha_hora_envio, mensaje, pasajero_id, conductor_id) VALUES
                                                                               (CURRENT_TIMESTAMP, 'El conductor tomó una ruta más larga', 1, NULL),
                                                                               (CURRENT_TIMESTAMP, 'El vehículo no coincidía con la descripción', 2, NULL),
                                                                               (CURRENT_TIMESTAMP, 'El pasajero dañó el asiento trasero', NULL, 1),
                                                                               (CURRENT_TIMESTAMP, 'Problemas con el cobro del viaje', 3, NULL),
                                                                               (CURRENT_TIMESTAMP, 'El pasajero no apareció en el punto de encuentro', NULL, 2),
                                                                               (CURRENT_TIMESTAMP, 'Mal comportamiento del conductor', 4, NULL),
                                                                               (CURRENT_TIMESTAMP, 'El pasajero comió dentro del vehículo', NULL, 3),
                                                                               (CURRENT_TIMESTAMP, 'Problemas con la aplicación', 5, NULL),
                                                                               (CURRENT_TIMESTAMP, 'El pasajero canceló en el último momento', NULL, 4),
                                                                               (CURRENT_TIMESTAMP, 'El conductor manejó de forma peligrosa', 1, NULL)
    ON CONFLICT DO NOTHING;

-- Insertar datos en la tabla vehiculo
INSERT INTO vehiculo (placa, marca, modelo, anio, cantidad_asientos, conductor_id) VALUES
                                                                                       ('ABC123', 'Toyota', 'Corolla', 2020, 4, 1),
                                                                                       ('DEF456', 'Nissan', 'Sentra', 2019, 4, 2),
                                                                                       ('GHI789', 'Hyundai', 'Accent', 2021, 4, 3),
                                                                                       ('JKL012', 'Kia', 'Rio', 2018, 4, 4),
                                                                                       ('MNO345', 'Volkswagen', 'Vento', 2022, 4, 5)
    ON CONFLICT DO NOTHING;

-- Insertar datos en la tabla viaje
INSERT INTO viaje (fecha_hora_creacion, fecha_hora_partida, asientos_disponibles, asientos_ocupados, estado, conductor_id) VALUES
                                                                                                                               (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 hour', 3, 1, 'ACEPTADO', 1),
                                                                                                                               (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2 hours', 2, 2, 'EN_CURSO', 2),
                                                                                                                               (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 minutes', 0, 4, 'SOLICITADO', NULL),
                                                                                                                               (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 hour', 0, 4, 'COMPLETADO', 4),
                                                                                                                               (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3 hours', 2, 2, 'ACEPTADO', 5),
                                                                                                                               (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2 hours', 1, 3, 'COMPLETADO', 3),
                                                                                                                               (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3 days', 2, 2, 'EN_CURSO', 3),
                                                                                                                               (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 day', 0, 3, 'SOLICITADO', NULL)
    ON CONFLICT DO NOTHING;

INSERT INTO pasajero_viaje (fecha_hora_union, fecha_hora_llegada, costo, estado, pasajero_id, viaje_id) VALUES
                                                                                                            (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 hour', 15.50, 'COMPLETADO', 1, 1),
                                                                                                            (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2 hours', 20.00, 'EN_CURSO', 2, 2),
                                                                                                            (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2 hours', 12.75, 'SOLICITADO', 3, 3),
                                                                                                            (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '30 minutes', 18.25, 'COMPLETADO', 4, 4),
                                                                                                            (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2 hours', 22.50, 'ACEPTADO', 5, 5),
                                                                                                            (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1 hour', 16.80, 'COMPLETADO', 1, 6),
                                                                                                            (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2 hours', 14.30, 'EN_CURSO', 1, 7),
                                                                                                            (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2 hours', 19.90, 'SOLICITADO', 3, 8)
    ON CONFLICT DO NOTHING;


-- Insertar datos en la tabla ubicacion
INSERT INTO ubicacion (latitud, longitud, direccion, provincia, viaje_id, pasajero_viaje_id) VALUES
                                                                              (-12.046373, -77.042754, 'Av. Arequipa 123', 'Lima', 1, NULL),
                                                                              (-12.056373, -77.052754, 'Av. Javier Prado 456', 'Lima', 2, NULL),
                                                                              (-12.066373, -77.062754, 'Av. La Marina 789', 'Lima', 3, NULL),
                                                                              (-12.076373, -77.072754, 'Av. Brasil 012', 'Lima', 4, NULL),
                                                                              (-12.086373, -77.082754, 'Av. Salaverry 345', 'Lima', 5, NULL),
                                                                              (-12.096373, -77.092754, 'Av. Universitaria 678', 'Lima', 6, NULL),
                                                                              (-12.106373, -77.102754, 'Av. Angamos 901', 'Lima', 7, NULL),
                                                                              (-12.116373, -77.112754, 'Av. Benavides 234', 'Lima', 8, NULL),
                                                                              (-11.853928, -77.119382, 'Av. San Martín 123', 'Huaral', NULL, 1),
                                                                              (-11.494987, -77.212345, 'Plaza de Armas', 'Huacho', NULL, 2),
                                                                              (-11.128976, -77.194563, 'Calle Comercio 456', 'Barranca', NULL, 3),
                                                                              (-12.231456, -76.897654, 'Av. Los Héroes 789', 'Cañete',  NULL, 4),
                                                                              (-10.876543, -77.654321, 'Jr. Lima 234', 'Huaura', NULL, 5),
                                                                              (-12.345678, -76.789012, 'Av. Progreso 567', 'Yauyos', NULL, 6),
                                                                              (-11.567890, -76.345678, 'Calle Grau 890', 'Cajatambo', NULL, 7),
                                                                              (-12.456789, -76.234567, 'Av. Libertad 123', 'Huarochirí', NULL, 8)

    ;

-- Insertar datos en la tabla calificacion
INSERT INTO calificacion (estrellas, comentario, conductor_id, pasajero_id, viaje_id) VALUES
                                                                                          (5, 'Excelente servicio', 4, 1, 4),
                                                                                          (4, 'Buen conductor, pero llegó tarde', 4, 2, 4),
                                                                                          (3, 'El auto estaba sucio', 3, 3, 6),
                                                                                          (5, 'Muy amable y buen manejo', 4, 4, 4),
                                                                                          (2, 'Tomó una ruta muy larga', 3, 5, 6),
                                                                                          (5, 'Puntual y seguro', 4, 2, 4),
                                                                                          (4, 'Buen viaje en general', 4, 3, 4),
                                                                                          (1, 'Mala experiencia, muy brusco al frenar', 3, 4, 6),
                                                                                          (5, 'Recomendado 100%', 4, 5, 4),
                                                                                          (3, 'El auto hacía ruidos extraños', 3, 1, 6)
    ON CONFLICT DO NOTHING;

