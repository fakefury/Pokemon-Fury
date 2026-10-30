Pokemon Fury - PokeApp 
Integrantes: Joey Morrell y Alex Kim
Tecnología utilizada: JavaFX
Requisitos: Java 21 (Para ser mas precisos jdk-21.0.12.1) y usbwebserver_v8.6.2.
Librerias: 
Maven: org.openjfx:javafx-controls:17.0.10
Maven: org.openjfx:javafx-controls:17.0.10
Maven: org.openjfx:javafx-graphics:17.0.10
Maven: org.openjfx:javafx-graphics:17.0.10
Maven: org.openjfx:javafx-base:17.0.10
Maven: org.openjfx:javafx-base:17.0.10
Maven: org.openjfx:javafx-fxml:17.0.10
Maven: org.openjfx:javafx-fxml:17.0.10
Instrucciones de ejecución en VSC:
Tener los requisitos adquiridos, dentro de VSC tener descargado la extension pack for java, 
una vez obtenido eso necesitas tener las variables de entornos puesta de la versión de java y maven para que lo pueda detectar VSC.
Luego en VSC le das al engranaje, le tocas Command Palette, pones Java:Configure Java Runtime, seleccionas el proyecto (PokeApp-Pokemon Fury)
Una vez hecho eso tener que ejecutar en terminal el siguiente comando mvn clean javafx:run
