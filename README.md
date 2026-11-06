Pokemon Fury - PokeApp

Integrantes:

Joey Morrell

Alex Kim

Tecnologías y Requisitos del Sistema

Tecnología principal: JavaFX

Java Development Kit: Java 21 (versión probada: JDK 21.0.12.1)

Servidor Web / Base de Datos: USBWebServer v8.6.2

Requisito previo: Asegurarse de tener la base de datos Pokeapp importada y activa en el servidor antes de ejecutar la aplicación.

Dependencias (Maven)

El proyecto utiliza las siguientes librerías de JavaFX (v17.0.10):

XML
<dependencies>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>17.0.10</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-graphics</artifactId>
        <version>17.0.10</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-base</artifactId>
        <version>17.0.10</version>
    </dependency>
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-fxml</artifactId>
        <version>17.0.10</version>
    </dependency>
</dependencies>
Instrucciones de Ejecución en Visual Studio Code (VSC)

Configuración previa del entorno

Instalar la extensión Extension Pack for Java en Visual Studio Code.

Configurar las Variables de Entorno del sistema operativo para JAVA_HOME y MAVEN_HOME (o incluir sus rutas en el PATH) para que VSC reconozca ambas herramientas.

Configurar el Runtime en VSC

Abrir la paleta de comandos: Ctrl + Shift + P (o Cmd + Shift + P en macOS).

Escribir y seleccionar: Java: Configure Java Runtime.

Seleccionar el proyecto PokeApp-Pokemon Fury y asignar la versión correcta del JDK.

Ejecución
Abrir la terminal integrada en VSC dentro de la raíz del proyecto y ejecutar el siguiente comando:

Bash
mvn clean javafx:run
