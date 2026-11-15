-- phpMyAdmin SQL Dump
-- version 4.7.5
-- https://www.phpmyadmin.net/
--
-- Servidor: localhost
-- Tiempo de generación: 05-11-2026 a las 23:54:06
-- Versión del servidor: 5.6.34
-- Versión de PHP: 7.1.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `pokeapp`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `efectividad_tipo`
--

CREATE TABLE `efectividad_tipo` (
  `tipo_atacante` varchar(20) NOT NULL,
  `tipo_defensor` varchar(20) NOT NULL,
  `multiplicador` decimal(2,1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `efectividad_tipo`
--

INSERT INTO `efectividad_tipo` (`tipo_atacante`, `tipo_defensor`, `multiplicador`) VALUES
('Agua', 'Agua', '0.5'),
('Agua', 'Eléctrico', '2.0'),
('Agua', 'Fuego', '0.5'),
('Agua', 'Planta', '2.0'),
('Bicho', 'Fuego', '2.0'),
('Bicho', 'Lucha', '0.5'),
('Bicho', 'Planta', '0.5'),
('Bicho', 'Roca', '2.0'),
('Bicho', 'Tierra', '0.5'),
('Bicho', 'Volador', '2.0'),
('Eléctrico', 'Eléctrico', '0.5'),
('Eléctrico', 'Tierra', '2.0'),
('Eléctrico', 'Volador', '0.5'),
('Fantasma', 'Bicho', '0.5'),
('Fantasma', 'Fantasma', '2.0'),
('Fantasma', 'Lucha', '0.5'),
('Fantasma', 'Veneno', '0.5'),
('Fuego', 'Agua', '2.0'),
('Fuego', 'Bicho', '0.5'),
('Fuego', 'Fuego', '0.5'),
('Fuego', 'Planta', '0.5'),
('Fuego', 'Roca', '2.0'),
('Fuego', 'Tierra', '2.0'),
('Lucha', 'Bicho', '0.5'),
('Lucha', 'Psíquico', '2.0'),
('Lucha', 'Roca', '0.5'),
('Lucha', 'Volador', '2.0'),
('Planta', 'Agua', '0.5'),
('Planta', 'Bicho', '2.0'),
('Planta', 'Eléctrico', '0.5'),
('Planta', 'Fuego', '2.0'),
('Planta', 'Planta', '0.5'),
('Planta', 'Tierra', '0.5'),
('Planta', 'Veneno', '2.0'),
('Planta', 'Volador', '2.0'),
('Psíquico', 'Bicho', '2.0'),
('Psíquico', 'Fantasma', '2.0'),
('Psíquico', 'Lucha', '0.5'),
('Psíquico', 'Psíquico', '0.5'),
('Roca', 'Agua', '2.0'),
('Roca', 'Fuego', '0.5'),
('Roca', 'Lucha', '2.0'),
('Roca', 'Planta', '2.0'),
('Roca', 'Tierra', '2.0'),
('Roca', 'Veneno', '0.5'),
('Roca', 'Volador', '0.5'),
('Tierra', 'Agua', '2.0'),
('Tierra', 'Eléctrico', '0.5'),
('Tierra', 'Planta', '2.0'),
('Tierra', 'Roca', '0.5'),
('Tierra', 'Veneno', '0.5'),
('Veneno', 'Bicho', '0.5'),
('Veneno', 'Lucha', '0.5'),
('Veneno', 'Planta', '0.5'),
('Veneno', 'Psíquico', '2.0'),
('Veneno', 'Tierra', '2.0'),
('Veneno', 'Veneno', '0.5'),
('Volador', 'Bicho', '0.5'),
('Volador', 'Eléctrico', '2.0'),
('Volador', 'Lucha', '0.5'),
('Volador', 'Planta', '0.5'),
('Volador', 'Roca', '2.0'),
('Volador', 'Tierra', '0.5');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entrenador`
--

CREATE TABLE `entrenador` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `pokemon1` int(11) DEFAULT NULL,
  `pokemon2` int(11) DEFAULT NULL,
  `pokemon3` int(11) DEFAULT NULL,
  `pokemon4` int(11) DEFAULT NULL,
  `pokemon5` int(11) DEFAULT NULL,
  `pokemon6` int(11) DEFAULT NULL,
  `campeon` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `movimiento`
--

CREATE TABLE `movimiento` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `pp` int(11) NOT NULL,
  `hace_dano` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `movimiento`
--

INSERT INTO `movimiento` (`id`, `nombre`, `pp`, `hace_dano`) VALUES
(1, 'Lengüetazo', 30, 1),
(2, 'Tinieblas', 15, 1),
(3, 'Hipnosis', 20, 0),
(4, 'Rayo Confuso', 10, 0),
(5, 'Lanza Rocas', 15, 1),
(6, 'Tornillo Roca', 10, 1),
(7, 'Chirrido', 40, 0),
(8, 'Fortaleza', 30, 0),
(9, 'Chupavidas', 15, 1),
(10, 'Espora', 15, 0),
(11, 'Rayo Solar', 10, 1),
(12, 'Paralizador', 30, 0),
(13, 'Psíquico', 10, 1),
(14, 'Confusión', 25, 1),
(15, 'Anulación', 20, 0),
(16, 'Ataque Ala', 35, 1),
(17, 'Vuelo', 15, 1),
(18, 'Agilidad', 30, 0),
(19, 'Torbellino', 20, 0),
(20, 'Terremoto', 10, 1),
(21, 'Excavar', 10, 1),
(22, 'Ataque Arena', 15, 0),
(23, 'Fisura', 5, 1),
(24, 'Residuos', 20, 1),
(25, 'Tóxico', 10, 0),
(26, 'Polución', 20, 1),
(27, 'Autodestrucción', 5, 1),
(28, 'Golpe Kárate', 25, 1),
(29, 'Sumisión', 20, 1),
(30, 'Movimiento Sísmico', 20, 1),
(31, 'Contador', 20, 1),
(32, 'Lanzallamas', 15, 1),
(33, 'Ascuas', 25, 1),
(34, 'Llamarada', 5, 1),
(35, 'Látigo Cepa', 25, 1),
(36, 'Megagotar', 10, 1),
(37, 'Atadura', 20, 1),
(38, 'Rayo', 15, 1),
(39, 'Trueno', 10, 1),
(40, 'Onda Voltio', 20, 0),
(41, 'Surf', 15, 1),
(42, 'Hidrobomba', 5, 1),
(43, 'Rayo Hielo', 10, 1),
(44, 'Recuperación', 20, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pokemon`
--

CREATE TABLE `pokemon` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `tipo` varchar(20) NOT NULL,
  `atk` int(11) NOT NULL,
  `hp` int(11) NOT NULL,
  `spd` int(11) NOT NULL,
  `movimiento_1` varchar(50) DEFAULT NULL,
  `movimiento_2` varchar(50) DEFAULT NULL,
  `movimiento_3` varchar(50) DEFAULT NULL,
  `movimiento_4` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `pokemon`
--

INSERT INTO `pokemon` (`id`, `nombre`, `tipo`, `atk`, `hp`, `spd`, `movimiento_1`, `movimiento_2`, `movimiento_3`, `movimiento_4`) VALUES
(1, 'Haunter', 'Fantasma', 75, 65, 105, 'Lengüetazo', 'Hipnosis', 'Rayo Confuso', 'Tinieblas'),
(2, 'Onix', 'Roca', 65, 95, 80, 'Lanza Rocas', 'Tornillo Roca', 'Chirrido', 'Fortaleza'),
(3, 'Parasect', 'Bicho', 105, 85, 55, 'Chupavidas', 'Espora', 'Rayo Solar', 'Paralizador'),
(4, 'Hypno', 'Psíquico', 85, 95, 80, 'Psíquico', 'Confusión', 'Hipnosis', 'Anulación'),
(5, 'Pidgeot', 'Volador', 85, 85, 110, 'Ataque Ala', 'Vuelo', 'Agilidad', 'Torbellino'),
(6, 'Dugtrio', 'Tierra', 85, 65, 110, 'Terremoto', 'Excavar', 'Ataque Arena', 'Fisura'),
(7, 'Weezing', 'Veneno', 95, 90, 70, 'Residuos', 'Tóxico', 'Polución', 'Autodestrucción'),
(8, 'Hitmonchan', 'Lucha', 110, 80, 85, 'Golpe Kárate', 'Sumisión', 'Movimiento Sísmico', 'Contador'),
(9, 'Ninetales', 'Fuego', 90, 85, 100, 'Lanzallamas', 'Ascuas', 'Rayo Confuso', 'Llamarada'),
(10, 'Tangela', 'Planta', 75, 100, 65, 'Látigo Cepa', 'Megagotar', 'Espora', 'Atadura'),
(11, 'Electrode', 'Eléctrico', 65, 75, 125, 'Rayo', 'Trueno', 'Onda Voltio', 'Chirrido'),
(12, 'Starmie', 'Agua', 110, 75, 110, 'Surf', 'Hidrobomba', 'Rayo Hielo', 'Recuperación');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `efectividad_tipo`
--
ALTER TABLE `efectividad_tipo`
  ADD PRIMARY KEY (`tipo_atacante`,`tipo_defensor`);

--
-- Indices de la tabla `entrenador`
--
ALTER TABLE `entrenador`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_entrenador_pokemon1` (`pokemon1`),
  ADD KEY `fk_entrenador_pokemon2` (`pokemon2`),
  ADD KEY `fk_entrenador_pokemon3` (`pokemon3`),
  ADD KEY `fk_entrenador_pokemon4` (`pokemon4`),
  ADD KEY `fk_entrenador_pokemon5` (`pokemon5`),
  ADD KEY `fk_entrenador_pokemon6` (`pokemon6`);

--
-- Indices de la tabla `pokemon`
--
ALTER TABLE `pokemon`
  ADD PRIMARY KEY (`id`);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `entrenador`
--
ALTER TABLE `entrenador`
  ADD CONSTRAINT `fk_entrenador_pokemon1` FOREIGN KEY (`pokemon1`) REFERENCES `pokemon` (`id`),
  ADD CONSTRAINT `fk_entrenador_pokemon2` FOREIGN KEY (`pokemon2`) REFERENCES `pokemon` (`id`),
  ADD CONSTRAINT `fk_entrenador_pokemon3` FOREIGN KEY (`pokemon3`) REFERENCES `pokemon` (`id`),
  ADD CONSTRAINT `fk_entrenador_pokemon4` FOREIGN KEY (`pokemon4`) REFERENCES `pokemon` (`id`),
  ADD CONSTRAINT `fk_entrenador_pokemon5` FOREIGN KEY (`pokemon5`) REFERENCES `pokemon` (`id`),
  ADD CONSTRAINT `fk_entrenador_pokemon6` FOREIGN KEY (`pokemon6`) REFERENCES `pokemon` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
