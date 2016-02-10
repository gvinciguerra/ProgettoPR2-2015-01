/**
 * Tweet è una classe mutabile che rappresenta un messaggio, scritto da un
 * User, ed eventualmente etichettato con un Tag. Un oggetto Tweet è dunque
 * una tripla (autore, testo, tag) dove l'autore è un oggetto User, testo è
 * una stringa e tag un oggetto Tag.
 */
public class Tweet {
    private String text;
    private User author;
    private Tag tag;

    /**
     * Costruisce un nuovo oggetto Tweet con gli attributi specificati.
     *
     * @param text il testo del messaggio
     * @param tag il tag del messaggio
     * @param author l'autore del messaggio
     */
    Tweet(String text, Tag tag, User author) {
        this.text = text;
        this.tag = tag;
        this.author = author;
    }

    /**
     * Ritorna un oggetto tag di questo messaggio.
     *
     * @return il tag del messaggio
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Modifica il tag di questo messaggio.
     *
     * @param tag il nuovo tag
     */
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * Ritorna una stringa che rappresenta il testo di questo messaggio.
     *
     * @return il testo del messaggio
     */
    public String getText() {
        return text;
    }

    /**
     * Modifica il testo di questo messaggio.
     *
     * @param text il nuovo testo del messaggio
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Ritorna l'autore di questo messaggio.
     *
     * @return l'autore del messaggio
     */
    public User getAuthor() {
        return author;
    }

    /**
     * Crea un nuovo tweet con lo stesso testo e lo stesso tag, ma con autore differente.
     *
     * @param  newAuthor l'autore del nuovo tweet
     * @return Un nuovo oggetto Tweet con l'autore specificato
     * @throws NullPointerException se newAuthor è null
     */
    public Tweet retweet(User newAuthor) throws IllegalArgumentException {
        if (newAuthor == null)
            throw new NullPointerException();

        return new Tweet(text, tag, newAuthor);
    }

    public String toString() {
        if (tag != null)
            return author + ": " + text + " " + tag;
        return author + ": " + text;
    }
}
