import exceptions.*;
import java.util.List;

interface SimpleTw {
    void addUser(User bob, String pass) throws UnauthorizedAccessException, NullPointerException;
    // Aggiunge l’utente bob all’insieme degli utenti. Solleva una eccezione se pass non è corretta

    void deleteUser(User bob, String pass) throws UnauthorizedAccessException;
    // Elimina l’utente bob dall’insieme degli utenti. Solleva una eccezione se pass non è corretta.

    int tweet(String message, Tag t, User bob) throws UnauthorizedUserException, MsgException;
    // Inserisce un messaggio di bob con intestazione t. Restituisce un codice numerico del messaggio.
    // Lancia un’eccezione se bob non è un utente registrato o se il messaggio supera la dimensione massima.

    String readLast(User bob) throws EmptyMsgException;
    // Restituisce l’ultimo messaggio inserito da bob. Solleva un’eccezione se non ci sono messaggi di bob.

    String readLast() throws EmptyMsgException;
    // Restituisce l’ultimo messaggio inserito. Solleva un’eccezione se non sono presenti messaggi.

    List<String> readAll(Tag t);
    // Restituisce tutti i messaggi inseriti con Tag t, nell’ordine di inserimento.

    List<String> readAll();
    // Restituisce tutti i messaggi inseriti, nell’ordine di inserimento.

    void delete(int code) throws WrongCodeException;
    // Cancella il messaggio identificato da code.
    // Solleva un’eccezione se non esiste un messaggio con quel codice.

    boolean empty();
    // Restituisce true se e solo se non sono presenti messaggi
}
