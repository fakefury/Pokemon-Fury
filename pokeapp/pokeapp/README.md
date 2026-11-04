# Pokémon Fury

Proyecto de videojuego por turnos desarrollado con **Java 11, JavaFX 17 y Maven**.
La propuesta de juego es elegir seis Pokémon de un catálogo de doce y combatir contra los seis restantes.

## Cómo ejecutar

1. Abrí la carpeta `pokeapp` como proyecto Maven en Eclipse o IntelliJ.
2. Verificá que el JDK configurado sea Java 11 o superior.
3. Esperá a que Maven descargue JavaFX.
4. Ejecutá desde una terminal ubicada en la carpeta del proyecto:

   ```bash
   mvn clean javafx:run
   ```

## Cómo se juega

- En el menú principal seleccioná **Nueva partida**.
- En la pantalla de equipo, hacé clic en los cuadros con los sprites chibi para elegir seis Pokémon.
- Los seis Pokémon que no elegiste forman automáticamente el equipo rival.
- En el combate, tu Pokémon aparece con su sprite trasero y el rival con su sprite frontal.
- Elegí uno de los cuatro movimientos disponibles. Los PP disminuyen al utilizar un movimiento.
- El rival responde cuando su Pokémon sobrevive al ataque.
- Cambiar de Pokémon consume un turno y permite que el rival responda.
- La batalla termina cuando uno de los equipos se queda sin Pokémon disponibles.
- Al comenzar cada combate se selecciona al azar uno de los cuatro fondos incluidos.

## Estructura

```text
src/main/java/com/pokeapp/
  App.java
  GameSession.java
  controller/   Pantallas y eventos JavaFX
  dao/          Catálogo y almacenamiento en memoria
  model/        Jugador, partida, Pokémon, movimientos y combate
  service/      Selección de equipo y reglas de batalla

src/main/resources/com/pokeapp/
  primary.fxml
  team.fxml
  battle.fxml
  gameboy.css
  sprites/      Sprites chibi, frontales, traseros y escenarios
```

## Sprites

Cada Pokémon tiene tres imágenes con una misma clave de archivo:

- `clave-chibi.png`: tarjeta seleccionable en el armado del equipo.
- `clave.png`: vista frontal del Pokémon rival.
- `clave-back.png`: vista trasera del Pokémon del jugador.

Las imágenes se cargan desde `src/main/resources/com/pokeapp/sprites/` usando el nombre de archivo asociado al Pokémon en `PokemonDAO`.

## Datos y persistencia

Esta versión utiliza DAO en memoria para que el juego se pueda ejecutar sin configurar una base MySQL. La clase `PokemonDAO` contiene los doce Pokémon y sus movimientos. Las partidas se generan al comenzar una nueva batalla.
