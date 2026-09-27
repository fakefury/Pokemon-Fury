package model;

public class Combate {

    private int idCombate;
    private int idPartida;
    private PokemonJugador pokemonJugador;
    private Pokemon pokemonEnemigo; 
    private String resultado;
    private int turnoActual;

    public Combate() {
    }

    public Combate(int idCombate, int idPartida, PokemonJugador pokemonJugador, Pokemon pokemonEnemigo, String resultado, int turnoActual) {
        this.idCombate = idCombate;
        this.idPartida = idPartida;
        this.pokemonJugador = pokemonJugador;
        this.pokemonEnemigo = pokemonEnemigo;
        this.resultado = resultado;
        this.turnoActual = turnoActual;
    }

    public int getIdCombate() {return idCombate;}
    public void setIdCombate(int idCombate) {this.idCombate = idCombate;}
    public int getIdPartida() {return idPartida;}
    public void setIdPartida(int idPartida) {this.idPartida = idPartida;}
    public PokemonJugador getPokemonJugador() {return pokemonJugador;}
    public void setPokemonJugador(PokemonJugador pokemonJugador) {this.pokemonJugador = pokemonJugador;}
    public Pokemon getPokemonEnemigo() {return pokemonEnemigo;}
    public void setPokemonEnemigo(Pokemon pokemonEnemigo) {this.pokemonEnemigo = pokemonEnemigo;}
    public String getResultado() {return resultado;}
    public void setResultado(String resultado) {this.resultado = resultado;}
    public int getTurnoActual() {return turnoActual;}
    public void setTurnoActual(int turnoActual) {this.turnoActual = turnoActual;}
}
