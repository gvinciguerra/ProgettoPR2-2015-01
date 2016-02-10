package exceptions;

/**
 * Eccezione sollevata quando a un metodo che richiede l'autenticazione viene
 * fornita una password errata.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super();
    }

    public UnauthorizedAccessException(String s) {
        super(s);
    }
}
