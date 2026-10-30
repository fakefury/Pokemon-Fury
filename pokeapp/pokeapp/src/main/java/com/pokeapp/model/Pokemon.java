package com.pokeapp.model;

public class Pokemon {
    protected int idPokemon;
    protected String nombre;
    protected String tipoPrimario;
    protected String tipoSecundario;
    protected int nivel;
    protected int hpMaximo;
    protected int hpActual;
    protected int ataque;
    protected int defensa;
    protected int velocidad;

    public Pokemon() { }

    public Pokemon(int idPokemon, String nombre, String tipoPrimario, String tipoSecundario,
                   int nivel, int hpMaximo, int ataque, int defensa, int velocidad) {
        this.idPokemon = idPokemon;
        this.nombre = nombre;
        this.tipoPrimario = tipoPrimario;
        this.tipoSecundario = tipoSecundario;
        this.nivel = nivel;
        this.hpMaximo = hpMaximo;
        this.hpActual = hpMaximo;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
    }

    public int recibirDanio(int danio) {
        int danioReal = Math.max(1, danio - defensa / 4);
        hpActual = Math.max(0, hpActual - danioReal);
        return danioReal;
    }

    public boolean estaDerrotado() { return hpActual <= 0; }
    public void recuperar() { hpActual = hpMaximo; }
    public int getIdPokemon() { return idPokemon; }
    public void setIdPokemon(int idPokemon) { this.idPokemon = idPokemon; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoPrimario() { return tipoPrimario; }
    public void setTipoPrimario(String tipoPrimario) { this.tipoPrimario = tipoPrimario; }
    public String getTipoSecundario() { return tipoSecundario; }
    public void setTipoSecundario(String tipoSecundario) { this.tipoSecundario = tipoSecundario; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public int getHpMaximo() { return hpMaximo; }
    public void setHpMaximo(int hpMaximo) { this.hpMaximo = hpMaximo; }
    public int getHpActual() { return hpActual; }
    public void setHpActual(int hpActual) { this.hpActual = hpActual; }
    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }
    public int getVelocidad() { return velocidad; }
    public void setVelocidad(int velocidad) { this.velocidad = velocidad; }
    @Override public String toString() { return nombre + " (" + tipoPrimario + (tipoSecundario == null ? "" : "/" + tipoSecundario) + ")"; }
}
