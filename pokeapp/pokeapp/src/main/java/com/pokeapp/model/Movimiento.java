package com.pokeapp.model;

/** Representa un movimiento que puede usar un Pokémon durante el combate. */
public class Movimiento {
    private String nombre;
    private String tipo;
    private int potencia;
    private int ppMax;
    private int ppActual;
    private boolean haceDano;

    public Movimiento() {
    }

    public Movimiento(String nombre, int pp, boolean haceDano) {
        this(nombre, "NORMAL", 40, pp, haceDano);
    }

    public Movimiento(String nombre, String tipo, int potencia, int pp, boolean haceDano) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.potencia = potencia;
        this.ppMax = pp;
        this.ppActual = pp;
        this.haceDano = haceDano;
    }

    public Movimiento copiar() {
        Movimiento copia = new Movimiento(nombre, tipo, potencia, ppMax, haceDano);
        copia.ppActual = ppActual;
        return copia;
    }

    public boolean tienePP() { return ppActual > 0; }
    public void reducirPp() { if (ppActual > 0) ppActual--; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public int getPotencia() { return potencia; }
    public void setPotencia(int potencia) { this.potencia = potencia; }
    public int getPpMax() { return ppMax; }
    public void setPpMax(int ppMax) { this.ppMax = ppMax; }
    public int getPpActual() { return ppActual; }
    public void setPpActual(int ppActual) { this.ppActual = ppActual; }
    public boolean isHaceDano() { return haceDano; }
    public void setHaceDano(boolean haceDano) { this.haceDano = haceDano; }
}
