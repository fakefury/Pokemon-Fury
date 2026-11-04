module com.pokeapp {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.pokeapp to javafx.fxml;
    opens com.pokeapp.controller to javafx.fxml;
    exports com.pokeapp;
}
