/**
 * Un DeletedUser è uno User che prima era presente nel sistema, ma adesso è
 * stato eliminato.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class DeletedUser extends User {
    private static DeletedUser singleInstance = new DeletedUser();

    /**
     * Si usa per ottenere la singola istanza di DeletedUser.
     *
     * @return istanza di DeletedUser
     */
    public static DeletedUser getInstance() {
        return singleInstance;
    }

    private DeletedUser() {
        super(null);
    }

    @Override
    public String toString() {
        return "‹Deleted user›";
    }
}
