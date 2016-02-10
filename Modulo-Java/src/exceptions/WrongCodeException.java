package exceptions;

/**
 * Eccezione sollevata per indicare che un codice di qualche tipo è errato.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class WrongCodeException extends RuntimeException {
    public WrongCodeException() {
        super();
    }
}
