package excepciones;

public class PokemonNoCapturableException extends Exception {
	public PokemonNoCapturableException(String mensaje) {
        super(mensaje);
    }
}
