package exceptions;

/**
 * Eccezione utilizzata per indicare che un messaggio non è nel formato
 * corretto per essere pubblicato.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class MsgException extends RuntimeException {
    public MsgException() {
        super();
    }

    public MsgException(String s) {
        super(s);
    }
}
