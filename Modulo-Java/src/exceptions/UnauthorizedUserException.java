package exceptions;

/**
 * Eccezione che segnala che un utente non può effettuare una certa operazione.
 * Tipicamente viene sollevata quando si cerca di pubblicare un messaggio di un
 * utente non registrato.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class UnauthorizedUserException extends RuntimeException {
    public UnauthorizedUserException() {
        super();
    }
}
