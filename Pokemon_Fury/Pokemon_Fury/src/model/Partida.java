package model;

public class Partida {

    private int idPartida;
    private int idJugador;
    private int progresoHistoria;
    private String ubicacionActual;
    private int cantidadPokeballs;
    private PokemonJugador pokemonActivo;
    private String finalObtenido;


    public Partida() {
    }

    public Partida(int idPartida, int idJugador, int progresoHistoria, String ubicacionActual, int cantidadPokeballs, PokemonJugador pokemonActivo, String finalObtenido) {
        this.idPartida = idPartida;
        this.idJugador = idJugador;
        this.progresoHistoria = progresoHistoria;
        this.ubicacionActual = ubicacionActual;
        this.cantidadPokeballs = cantidadPokeballs;
        this.pokemonActivo = pokemonActivo;
        this.finalObtenido = finalObtenido;
    }
    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {this.idPartida = idPartida;}
    public int getIdJugador() {return idJugador;}
    public void setIdJugador(int idJugador) {this.idJugador = idJugador;}
    public int getProgresoHistoria() {return progresoHistoria;}
    public void setProgresoHistoria(int progresoHistoria) {this.progresoHistoria = progresoHistoria;}
    public String getUbicacionActual() {return ubicacionActual;}
    public void setUbicacionActual(String ubicacionActual) {this.ubicacionActual = ubicacionActual;}
    public int getCantidadPokeballs() {return cantidadPokeballs;}
    public void setCantidadPokeballs(int cantidadPokeballs) {this.cantidadPokeballs = cantidadPokeballs;}
    public PokemonJugador getPokemonActivo() {return pokemonActivo;}
    public void setPokemonActivo(PokemonJugador pokemonActivo) {this.pokemonActivo = pokemonActivo;}
    public String getFinalObtenido() {return finalObtenido;}
    public void setFinalObtenido(String finalObtenido) {this.finalObtenido = finalObtenido;}
}
