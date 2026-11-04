package com.pokeapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 1050, 760);
        scene.getStylesheets().add(App.class.getResource("/com/pokeapp/gameboy.css").toExternalForm());
        stage.setTitle("Pokémon Fury");
        stage.setMinWidth(900);
        stage.setMinHeight(680);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/pokeapp/" + fxml + ".fxml"));
        return loader.load();
    }

    public static void main(String[] args) { launch(args); }
}
