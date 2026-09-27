package excepciones;

public class PokemonNoDisponibleException extends Exception {
	public PokemonNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
