package com.pokeapp.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Muestra un indicador de daño breve sobre un Pokémon.
 * Está separado del controlador de batalla para que pueda quitarse
 * fácilmente si se decide no usar esta animación.
 */
public final class DamageIndicator {
    private DamageIndicator() { }

    public static void show(Label label, int damage) {
        if (label == null || damage <= 0) return;

        label.setText("-" + damage);
        label.setOpacity(1.0);
        label.setVisible(true);
        label.setManaged(false);

        FadeTransition fade = new FadeTransition(Duration.millis(650), label);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        TranslateTransition move = new TranslateTransition(Duration.millis(650), label);
        move.setFromY(0);
        move.setToY(-24);

        ParallelTransition animation = new ParallelTransition(fade, move);
        animation.setOnFinished(event -> {
            label.setVisible(false);
            label.setOpacity(1.0);
            label.setTranslateY(0);
            label.setText("");
        });
        animation.play();
    }
}
