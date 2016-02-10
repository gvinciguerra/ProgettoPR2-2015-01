/**
 * Un DeletedTweet è un oggetto immutabile che rappresenta un messaggio
 * eliminato. Questa classe non supporta i metodi set e retweet poiché
 * immutabile.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class DeletedTweet extends Tweet {
    private static DeletedTweet singleInstance = new DeletedTweet();

    private DeletedTweet() {
        super(null, null, null);
    }

    /**
     * Si usa per ottenere la singola istanza di DeletedTweet.
     * @return istanza di DeletedTweet
     */
    public static DeletedTweet getInstance() {
        return singleInstance;
    }

    /**
     * Questa operazione non è supportata poiché la classe è immutabile.
     *
     * @param tag il nuovo tag
     * @throws UnsupportedOperationException sempre
     */
    @Override
    public void setTag(Tag tag) {
        throw new UnsupportedOperationException();
    }

    /**
     * Questa operazione non è supportata poiché la classe è immutabile.
     *
     * @param text il nuovo testo del messaggio
     * @throws UnsupportedOperationException sempre
     */
    @Override
    public void setText(String text) {
        throw new UnsupportedOperationException();
    }

    /**
     * Questa operazione non è supportata.
     *
     * @param  newAuthor l'autore del nuovo tweet
     * @return non ritorna
     * @throws UnsupportedOperationException sempre
     */
    @Override
    public Tweet retweet(User newAuthor) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "‹Deleted message›";
    }
}
