package model;

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
    protected int experiencia;

    public Pokemon() {
    }

    public Pokemon(String nombre, int nivel, int hpMaximo, int ataque, int defensa, int velocidad) {
        this.nombre = nombre;
        this.nivel = nivel;
        this.hpMaximo = hpMaximo;
        this.hpActual = hpMaximo;
        this.ataque = ataque;
        this.defensa = defensa;
        this.velocidad = velocidad;
    }

    public int getIdPokemon() {
        return idPokemon;
    }

    public void setIdPokemon(int idPokemon) {
        this.idPokemon = idPokemon;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoPrimario() {
        return tipoPrimario;
    }

    public void setTipoPrimario(String tipoPrimario) {
        this.tipoPrimario = tipoPrimario;
    }

    public String getTipoSecundario() {
        return tipoSecundario;
    }

    public void setTipoSecundario(String tipoSecundario) {
        this.tipoSecundario = tipoSecundario;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getHpMaximo() {
        return hpMaximo;
    }

    public void setHpMaximo(int hpMaximo) {
        this.hpMaximo = hpMaximo;
    }

    public int getHpActual() {
        return hpActual;
    }

    public void setHpActual(int hpActual) {
        this.hpActual = hpActual;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }
}