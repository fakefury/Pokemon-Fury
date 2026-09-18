package model;

public class PokemonJefe extends Pokemon {
    private int faseActual;
    private double multiplicadorDefensa;
    private String recompensaHistoria;

    public PokemonJefe() {
        super();
    }

    public int getFaseActual() {
        return faseActual;
    }

    public void setFaseActual(int faseActual) {
        this.faseActual = faseActual;
    }

    public double getMultiplicadorDefensa() {
        return multiplicadorDefensa;
    }

    public void setMultiplicadorDefensa(double multiplicadorDefensa) {
        this.multiplicadorDefensa = multiplicadorDefensa;
    }

    public String getRecompensaHistoria() {
        return recompensaHistoria;
    }

    public void setRecompensaHistoria(String recompensaHistoria) {
        this.recompensaHistoria = recompensaHistoria;
    }
}