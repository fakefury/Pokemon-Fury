package excepciones;

public class PokemonDebilitadoException extends Exception {
	public PokemonDebilitadoException(String mensaje) {
        super(mensaje);
    }
}