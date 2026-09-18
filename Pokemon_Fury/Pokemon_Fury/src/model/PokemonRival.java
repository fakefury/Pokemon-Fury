package model;

public class PokemonRival extends Pokemon {
    private int nivelEntrenador;
    private String estrategia;
    private int bonoRival;

    public PokemonRival() {
        super();
    }

    public int getNivelEntrenador() {
        return nivelEntrenador;
    }

    public void setNivelEntrenador(int nivelEntrenador) {
        this.nivelEntrenador = nivelEntrenador;
    }

    public String getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(String estrategia) {
        this.estrategia = estrategia;
    }

    public int getBonoRival() {
        return bonoRival;
    }

    public void setBonoRival(int bonoRival) {
        this.bonoRival = bonoRival;
    }
}