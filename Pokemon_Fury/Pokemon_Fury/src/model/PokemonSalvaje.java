package model;

public class PokemonSalvaje extends Pokemon {
    private String zona;
    private boolean capturable;
    private double probabilidadHuida;

    public PokemonSalvaje() {
        super();
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public boolean isCapturable() {
        return capturable;
    }

    public void setCapturable(boolean capturable) {
        this.capturable = capturable;
    }

    public double getProbabilidadHuida() {
        return probabilidadHuida;
    }

    public void setProbabilidadHuida(double probabilidadHuida) {
        this.probabilidadHuida = probabilidadHuida;
    }
}