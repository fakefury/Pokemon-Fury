package com.pokeapp.exceptions;

/** Se lanza cuando el movimiento elegido ya no tiene puntos de poder disponibles. */
public class PpInsuficienteException extends Exception {
    public PpInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
