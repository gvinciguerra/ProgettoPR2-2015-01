/**
 * Uno User è un oggetto immutabile che rappresenta un utente con un dato
 * nickname.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class User {
    private String nick;

    /**
     * Costruisce un oggetto User con il nome specificato.
     *
     * @param nickname il nome dell'utente
     */
    User(String nickname) {
        nick = nickname;
    }

    /**
     * Il nome di questo utente.
     *
     * @return il nome dell'utente
     */
    public String getName() {
        return nick;
    }

    @Override
    public String toString() {
        return nick;
    }
}