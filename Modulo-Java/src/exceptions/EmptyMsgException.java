package exceptions;

/**
 * Eccezione utilizzata per indicare che un messaggio non è presente.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class EmptyMsgException extends RuntimeException {
    public EmptyMsgException() {
        super();
    }

    public EmptyMsgException(String s) {
        super(s);
    }
}
