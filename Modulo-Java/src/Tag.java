import java.util.ArrayList;
import java.util.List;

/**
 * Un Tag è un oggetto immutabile che rappresenta una lista di una o più
 * etichette.
 * Un valore tipico è: #tag_1, ..., #tag_n.
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class Tag {
    private String name;
    private Tag next;

    /**
     * Costruisce un oggetto Tag composto da una sola etichetta a partire dalla
     * stringa fornita.
     *
     * @param  n la stringa che rappresenta l'etichetta
     * @throws NullPointerException se n è null
     */
    public Tag(String n) throws NullPointerException {
        if (n == null)
            throw new NullPointerException();
        name = n;
    }

    /**
     * Costruisce un nuovo Tag composto dall'etichetta specificata, seguita
     * dalle etichette in nextT.
     *
     * @param n la stringa che rappresenta l'etichetta
     * @param nextT le etichette che seguiranno n
     */
    public Tag(String n, Tag nextT) {
        this(n);
        next = nextT;
    }

    /**
     * Restituisce il nome della prima etichetta nella lista.
     *
     * @return la stringa che rappresenta la prima etichetta
     */
    public String getName() {
        return name;
    }

    /**
     * Ritorna true se questo Tag contiene l'etichetta specificata.
     * Altrimenti restituisce false.
     * Più formalmente ritorna true se e solo se esiste un elemento e in this
     * tale che e.getName().equals(t).
     *
     * @param  name il tag da cercare
     * @return true se e solo se t appartiene a this
     */
    public boolean containsTag(String name) {
        if (name.equals(name))
            return true;
        if (next == null)
            return false;
        return next.containsTag(name);
    }

    /**
     * Ritorna true se e solo se this contiene tutte le etichette di t.
     *
     * @param  t il Tag da cercare
     * @return true se e solo se this contiene tutte le etichette di t.
     */
    public boolean containsTag(Tag t) {
        return getTagNamesList().containsAll(t.getTagNamesList());
    }

    /**
     * Restituisce la lista di etichette che seguono l'etichetta in testa.
     *
     * @return il Tag che segue this
     */
    public Tag getNext() {
        return next;
    }

    /**
     * Restituisce tutte le etichette di questo Tag.
     *
     * @return lista di stringhe che rappresentano le etichette di this
     */
    public List<String> getTagNamesList() {
        ArrayList<String> result = new ArrayList<>();
        result.add(name);

        Tag current = next;
        while (current != null) {
            result.add(current.getName());
            current = current.getNext();
        }
        return result;
    }

    @Override
    public String toString() {
        if (next != null)
            return "#" + name + " " + next.toString();
        return "#" + name;
    }
}
