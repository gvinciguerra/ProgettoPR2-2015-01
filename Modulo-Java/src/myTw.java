import exceptions.*;
import java.util.*;

/**
 * myTw è una implementazione dell'interfaccia SimpleTw. Un oggetto myTw è una
 * tripla (utenti, tweets, password) dove:
 * • utenti è una lista di oggetti User ordinati per tempo di inserimento.
 * • tweets è una lista di oggetti Tweet ordinati per tempo di inserimento.
 * • password è la stringa che permette di operare sulla lista degli utenti.
 *
 * Il testo di ogni tweet può essere al massimo di 140 caratteri, escluso il
 * tag. I tweet appartengono a uno degli utenti oppure a un utente eliminato.
 *
 * Una istanza tipica è:
 * ([utente_0, ..., utente_(n–1)], [tweet_0, ..., tweet_(k–1)], "NON 123456!")
 *
 * FUNZIONE DI ASTRAZIONE
 * α(c) = (usersNotDeleted, tweetsNotDeleted, c.password)
 *   dove usersNotDeleted  = {∀u : u ∈ c.users | u notaninstanceof DeletedUsers}
 *   e    tweetsNotDeleted = {∀t : t ∈ c.tweets | t notaninstanceof DeletedTweet}
 *
 * INVARIANTE DI RAPPRESENTAZIONE
 * I(c) = c.users != null && c.tweets != null && c.password != null
 *        && c.realTweetCount = c.tweets.size - #{∀t : t ∈ c.tweets | t instanceof DeletedTweet}
 *        && (∀t . t ∈ c.tweets ⇒ t instanceof DeletedTweet
 *                                || (t instanceof Tweet
 *                                    && t.getText != null
 *                                    && t.getAuthor != null
 *                                    && 0 <= t.getText.length <= 140)
 *                                    && (t.getAuthor = DeletedUser || t.getAuthor ∈ c.users)
 *        && (∀u . u ∈ c.users ⇒ u instance of DeletedUser || u.getName != null)
 *
 * @author Giorgio Vinciguerra
 * @since 31/05/15
 */
public class myTw implements SimpleTw {
    private String password;
    private ArrayList<User> users;
    private ArrayList<Tweet> tweets;
    private int realTweetCount;
    private int maxTweetLength = 140;

    /**
     * Costruisce un oggetto myTw con la password specificata.
     *
     * @param pwd la password
     */
    myTw(String pwd) {
        this();
        password = pwd;
    }
    /**
     * Costruisce un oggetto myTw con password vuota.
     */
    myTw() {
        realTweetCount = 0;
        password = "";
        users = new ArrayList<>();
        tweets = new ArrayList<>();
    }

    /**
     * Aggiunge un nuovo utente all'insieme degli utenti.
     *
     * @param  bob l'utente da inserire
     * @param  pass la password per effettuare l'operazione
     * @throws UnauthorizedUserException se la password non è corretta
     * @throws NullPointerException se uno dei parametri è null
     * @throws IllegalArgumentException se bob.getName è null
     */
    public void addUser(User bob, String pass) throws UnauthorizedAccessException, NullPointerException, IllegalArgumentException {
        if (bob == null || pass == null)
            throw new NullPointerException();
        if (bob.getName() == null)
            throw  new IllegalArgumentException(bob + " non può avere nome null");
        if (!pass.equals(password))
            throw new UnauthorizedAccessException();
        if (!users.contains(bob))
            users.add(bob);
    }

    /**
     * Elimina un utente dall'insieme degli utenti. I suoi messaggi non
     * vengono rimossi.
     *
     * @param  bob l'utente da rimuovere
     * @param  pass la password per effettuare l'operazione
     * @throws UnauthorizedUserException se la password non è corretta
     * @throws NullPointerException se uno dei parametri è null
     */
    public void deleteUser(User bob, String pass) throws UnauthorizedAccessException, NullPointerException {
        deleteUser(bob, false, pass);
    }

    /**
     * Elimina l'utente bob dall'insieme degli utenti e, se il flag specificato
     * è true, anche tutti i suoi messaggi.
     *
     * @param  bob l'utente da rimuovere
     * @param  deleteTweets se true rimuove anche tutti i tweet dell'utente
     * @param  pass la password per effettuare l'operazione
     * @throws UnauthorizedUserException se la password non è corretta
     * @throws NullPointerException se uno dei parametri è null
     */
    public void deleteUser(User bob, boolean deleteTweets, String pass) throws UnauthorizedUserException, NullPointerException {
        if (bob == null || pass == null)
            throw new NullPointerException();
        if (!pass.equals(password))
            throw new UnauthorizedAccessException();
        if (bob instanceof DeletedUser)
            return;

        int index = users.indexOf(bob);
        if (index == -1)
            return;
        users.set(index, DeletedUser.getInstance());

        ListIterator<Tweet> li = tweets.listIterator();
        while (li.hasNext()) {
            Tweet tweet = li.next();
            if (tweet.getAuthor() == bob) {
                if (deleteTweets) {
                    li.set(DeletedTweet.getInstance());
                    realTweetCount--;
                }
                else // Sostituisce il tweet con uno identico ma con autore eliminato
                    li.set(tweet.retweet(DeletedUser.getInstance()));
            }
        }
    }

    /**
     * Inserisce un messaggio in this con il testo, tag e autore specificati.
     *
     * @param  message il testo del messaggio
     * @param  t il tag del messaggio
     * @param  bob l'autore del messaggio
     * @return un intero che rappresenta il codice univoco del messaggio appena
     *         inserito in this
     * @throws NullPointerException se message o t sono null
     * @throws UnauthorizedUserException se la password non è corretta
     * @throws MsgException se bob non è registrato come utente
     * @throws MsgException se message supera la lunghezza massima consentita
     */
    public int tweet(String message, Tag t, User bob) throws UnauthorizedUserException, MsgException, NullPointerException {
        if (message == null || bob == null)
            throw new NullPointerException();
        return tweet(new Tweet(message, t, bob));
    }

    /**
     * Inserisce un messaggio in this.
     *
     * @param  theTweet il messaggio da inserire
     * @return un intero che rappresenta il codice univoco del messaggio appena
     *         inserito in this
     * @throws NullPointerException se theTweet è null
     * @throws IllegalArgumentException se theTweet.getText o
     *                                  theTweet.getAuthor è null
     * @throws UnauthorizedUserException se la password non è corretta
     * @throws MsgException se l'autore di theTweet non è registrato come utente
     * @throws MsgException se il testo di theTweet supera la lunghezza massima
     *         consentita
     */
    public int tweet(Tweet theTweet) throws UnauthorizedUserException, MsgException, NullPointerException, IllegalArgumentException {
        if (theTweet == null)
            throw new NullPointerException();
        if (theTweet.getText() == null)
            throw new IllegalArgumentException(theTweet + " non può avere testo null");
        if (theTweet.getAuthor() == null)
            throw new IllegalArgumentException(theTweet + " non può avere autore null");
        if (theTweet.getText().length() > maxTweetLength)
            throw new MsgException("Il messaggio supera la lunghezza massima consentita (" + maxTweetLength + ")");
        if (!users.contains(theTweet.getAuthor()))
            throw new MsgException(theTweet.getAuthor() + " non è registrato come utente.");

        // Prima di inserire, effettua una copia per proteggere la rappresentazione interna (visto che Tweet è modificabile)
        tweets.add(new Tweet(theTweet.getText(), theTweet.getTag(), theTweet.getAuthor()));
        realTweetCount++;
        return tweets.size() - 1;
    }

    /**
     * L'ultimo messaggio che l'autore specificato ha inserito.
     *
     * @param  bob l'autore del messaggio
     * @return l'ultimo messaggio di bob
     * @throws NullPointerException se bob è null
     * @throws EmptyMsgException se non ci sono messaggi di bob o bob non è
     *         nell'insieme degli utenti
     */
    public String readLast(User bob) throws EmptyMsgException, NullPointerException {
        if (bob == null)
            throw new NullPointerException();
        if (!users.contains(bob))
            throw new EmptyMsgException(bob + " non è registrato come utente.");

        // Scorre la collezione dei tweet al contrario
        for (ListIterator<Tweet> li = tweets.listIterator(tweets.size()); li.hasPrevious();) {
            Tweet tweet = li.previous();
            if (tweet.getAuthor() == bob)
                return tweet.getText();
        }
        throw new EmptyMsgException(bob + " non ha scritto messaggi.");
    }

    /**
     * L'ultimo messaggio inserito in this.
     *
     * @return l'ultimo messaggio inserito
     * @throws EmptyMsgException se non ci sono messaggi
     */
    public String readLast() throws EmptyMsgException {
        // Scorre la collezione dei tweet al contrario per trovare un messaggio non eliminato
        for (ListIterator<Tweet> li = tweets.listIterator(tweets.size()); li.hasPrevious();) {
            Tweet tweet = li.previous();
            if (!(tweet instanceof DeletedTweet))
                return tweet.getText();
        }
        throw new EmptyMsgException(); // Eseguito solo se this.empty()
    }

    /**
     * Restituisce tutti i messaggi col tag specificato presenti in this.
     * L'ordine è quello di inserimento. Se Tag è una lista di etichette,
     * vengono restituiti tutti e soli i messaggi che contengono le etichette
     * di t.
     *
     * @return la lista dei messaggi
     */
    public List<String> readAll(Tag t) {
        ArrayList<String> result = new ArrayList<>();
        for (Tweet tw : tweets)
            if (!(tw instanceof DeletedTweet) && tw.getTag() != null
                    && tw.getTag().containsTag(t))
                result.add(tw.getText());
        return result;
    }

    /**
     * Tutti i messaggi inseriti in this, nell’ordine di inserimento.
     *
     * @return la lista dei messaggi
     */
    public List<String> readAll() {
        ArrayList<String> result = new ArrayList<>(realTweetCount);
        for (Tweet tw : tweets)
            if (!(tw instanceof DeletedTweet))
                result.add(tw.getText());
        return result;
    }

    /**
     * Cancella un messaggio da this.
     *
     * @param  code il codice univoco del messaggio da cancellare
     * @throws WrongCodeException se non ci sono messaggi col codice specificato
     */
    public void delete(int code) throws WrongCodeException {
        if (code < 0 || code >= tweets.size() || tweets.get(code) instanceof DeletedTweet)
            throw new WrongCodeException();
        tweets.set(code, DeletedTweet.getInstance());
        realTweetCount--;
    }

    /**
     * Un flag che indica se non ci sono messaggi in this.
     *
     * @return true se e solo se non sono presenti messaggi
     */
    public boolean empty() {
        return realTweetCount == 0;
    }

    /**
     * Cambia la password utilizzata per gestire l'insieme degli utenti.
     *
     * @param  oldPwd la vecchia password
     * @param  newPwd la nuova password
     * @throws UnauthorizedUserException se oldPwd non è corretta
     * @throws NullPointerException se uno dei parametri è null
     */
    public void setPassword(String oldPwd, String newPwd) throws UnauthorizedUserException, NullPointerException {
        if (oldPwd == null || newPwd == null)
            throw new NullPointerException();
        if (password.equals(oldPwd)) {
            password = newPwd;
        }
        else
            throw new UnauthorizedUserException();
    }

    /**
     * Il numero totale di messaggi presenti in this.
     *
     * @return il numero di messaggi.
     */
    public int getTweetCount() {
        return realTweetCount;
    }

    /**
     * Ritorna la lista degli utenti, ordinati per tempo di aggiunta.
     *
     * @return lista di utenti
     */
    public List<User> allUsers() {
        ArrayList<User> result = new ArrayList<>();
        for (User u : users)
            if (!(u instanceof DeletedUser))
                result.add(u);
        return result;
    }

    /**
     * Ritorna true se l'invariante di rappresentazione è valida per lo stato
     * corrente di this. Altrimenti ritorna false.
     *
     * @return true se e solo se l'oggetto è in uno stato che soddisfa I
     */
    public boolean repOk() {
        // c.users != null && c.tweets != null && c.password != null
        if (users == null || users == null || password == null)
            return false;

        // && (∀t . t ∈ c.tweets ⇒ t instanceof DeletedTweet || ...
        int deletedTweetCount = 0;
        for (Tweet t : tweets)
            if (t instanceof DeletedTweet)
                deletedTweetCount++;
            else if (t.getText() == null || t.getText().length() > 140 || t.getText().length() < 0
                    || !(t.getAuthor() instanceof DeletedUser) && !users.contains(t.getAuthor()))
                return false;

        // c.realTweetCount = c.tweets.count - #{∀t : t ∈ c.tweets | t instanceof DeletedTweet}
        if (realTweetCount != tweets.size() - deletedTweetCount)
            return  false;

        // && (∀u . u ∈ c.users ⇒ u instance of DeletedUser || u.getName != null)
        for (User u : users)
            if (!(u instanceof DeletedUser) && u.getName() == null)
                return false;

        return true;
    }

    @Override
    public String toString() {
        ArrayList<String> messages = new ArrayList<>(realTweetCount);
        for (Tweet tw : tweets)
            if (!(tw instanceof DeletedTweet)) {
                messages.add(tw.toString());
            }
        return "(\n\t" + allUsers() + ",\n\t" + messages + ",\n" + "\t•••••••\n)"; // La password è sempre nascosta
    }
}
