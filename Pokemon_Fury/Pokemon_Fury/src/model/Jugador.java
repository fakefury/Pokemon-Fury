package model;

public class Jugador {

    private int idJugador;
    private String nombre;

    public Jugador() {
    }

    public Jugador(int idJugador, String nombre) {
        this.idJugador = idJugador;
        this.nombre = nombre;
    }

    public int getIdJugador() {return idJugador;}
    public void setIdJugador(int idJugador) {this.idJugador = idJugador;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
}
