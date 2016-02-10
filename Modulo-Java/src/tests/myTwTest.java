import exceptions.*;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Created by Giorgio Vinciguerra on 01/06/15.
 */
public class myTwTest {
    private myTw emptyTw; // Senza messaggi, senza utenti
    private myTw fullTw;
    private User user0;   // User registrato in fullTw con 2 messaggi
    private User user1;   // User registrato in fullTw senza messaggi
    private User user2;   // User registrato in fullTw con un messaggio
    private Tag tag0;     // Tag utilizzato nel primo messaggio in fullTw

    @Before // Eseguito prima di ogni @Test
    public void setUp() throws Exception {
        emptyTw = new myTw("");
        user0 = new User("steve");
        user1 = new User("tim");
        user2 = new User("pippo");
        tag0 = new Tag("pr2");
        fullTw = new myTw("");
        fullTw.addUser(user0, "");
        fullTw.addUser(user1, "");
        fullTw.addUser(user2, "");
        fullTw.tweet("primo tweet!", tag0, user0);
        fullTw.tweet("che bel tempo oggi", null, user0);
        fullTw.tweet("già", null, user2);
    }

    @After // Eseguito dopo ogni @Test
    public void tearDown() throws Exception {
        assertTrue(emptyTw.repOk());
        assertTrue(fullTw.repOk());
    }

    @Test
    public void testAddUser() throws Exception {
        emptyTw.addUser(new User("luke"), "");

        // Test di robustezza su argomenti non corretti
        try {
            emptyTw.addUser(DeletedUser.getInstance(), "");
            fail("È stato accettato un utente senza nome");
        }
        catch (IllegalArgumentException e) {}
        try {
            emptyTw.addUser(user0, "blabla");
            fail("È stata eseguita un'operazione con una password errata");
        }
        catch (UnauthorizedAccessException e) {}
        try {
            emptyTw.addUser(null, null);
            fail("Sono stati accettati argomenti nulli");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Rimozione utente non presente
        emptyTw.deleteUser(new User("alan"), "");
        assertEquals(0, emptyTw.allUsers().size());

        // Rimozione utente non valido, non ha nessun effetto
        fullTw.deleteUser(DeletedUser.getInstance(), "");
        assertEquals(3, fullTw.allUsers().size());

        // Rimozione di un utente, con tutti i suoi messaggi
        fullTw.deleteUser(user0, true, "");
        assertEquals(2, fullTw.allUsers().size());
        assertEquals(1, fullTw.readAll().size());

        // Rimozione di un utente, ma senza i suoi messaggi
        fullTw.deleteUser(user2, false, "");
        assertEquals(1, fullTw.allUsers().size());
        assertEquals(1, fullTw.readAll().size());

        // Controllo che siano stati eliminati i tweet
        try {
            fullTw.readLast(user0);
            fail();
        }
        catch (EmptyMsgException e) {}

        // Test di robustezza su argomenti non corretti
        try {
            emptyTw.deleteUser(user0, true, "blabla");
            fail("È stata eseguita un'operazione con una password errata");
        }
        catch (UnauthorizedAccessException e) {}
        try {
            emptyTw.deleteUser(null, true, null);
            fail("Sono stati accettati argomenti nulli");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void testTweet() throws Exception {
        // Test di robustezza (parametri non corretti)
        try {
            emptyTw.tweet("", null, user0);
            fail("E stato accettato un tweet da un utente non registrato");
        }
        catch (MsgException e) {}
        try {
            emptyTw.tweet("", null, DeletedUser.getInstance());
            fail("È stato accettato un tweet senza autore");
        }
        catch (MsgException e) {}
        try {
            emptyTw.tweet("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed tortor magna, interdum quis vehicula at, molestie a lectus. Duis consequat, sapien metus.", null, DeletedUser.getInstance());
            fail("È stato accettato un testo più lungo di 140 caratteri");
        }
        catch (MsgException e) {}
        try {
            emptyTw.tweet("", tag0, null);
            fail("Sono stati accettati argomenti nulli");
        }
        catch (NullPointerException e) {}
        try {
            emptyTw.tweet(null);
            fail("Sono stati accettati argomenti nulli");
        }
        catch (NullPointerException e) {}
        try {
            emptyTw.tweet(new Tweet("", null, null));
            fail("Sono stati accettati argomenti errati");
        }
        catch (IllegalArgumentException e) {}
        try {
            emptyTw.tweet(new Tweet(null, null, user0));
            fail("Sono stati accettati argomenti errati");
        }
        catch (IllegalArgumentException e) {}
    }

    @Test
    public void testTweet1() throws Exception { // con parametro utente
        emptyTw.addUser(user0, "");
        int id0 = emptyTw.tweet("qualcosa", tag0, user0);
        int id1 = emptyTw.tweet("qualcos'altro", null, user0);
        assertEquals(id0, 0);
        assertEquals(id1, 1);
        assertEquals("qualcos'altro", emptyTw.readLast(user0));
        assertEquals(emptyTw.readAll().size(), 2);
        emptyTw.delete(id1);
        assertEquals("qualcosa", emptyTw.readLast());
        assertEquals(emptyTw.readAll().size(), 1);
    }

    @Test
    public void testReadLast() throws Exception { // senza parametri
        assertEquals("già", fullTw.readLast());

        try {
            emptyTw.readLast();
            fail("Doveva sollevare EmptyMsgException perché non ci sono messaggi in " + emptyTw);
        }
        catch (EmptyMsgException e) {}
    }

    @Test
    public void testEmpty() throws Exception {
        assertTrue(emptyTw.empty());
        assertFalse(fullTw.empty());
    }

    @Test
    public void testReadLast1() throws Exception { // con parametro User
        assertEquals("che bel tempo oggi", fullTw.readLast(user0));

        try {
            emptyTw.readLast(user0);
            fail("Doveva sollevare EmptyMsgException perché l'utente non è registrato");
        }
        catch (EmptyMsgException e) {}
        try {
            emptyTw.readLast(DeletedUser.getInstance());
            fail("L'utente non ha nome");
        }
        catch (EmptyMsgException e) {}
        try {
            fullTw.readLast(user1);
            fail("L'utente non ha messaggi");
        }
        catch (EmptyMsgException e) {}
        try {
            emptyTw.readLast(null);
            fail("Sono stati accettati argomenti nulli");
        }
        catch (NullPointerException e) {}
    }

    @Test
    public void testReadAll() throws Exception { // con parametro Tag
        assertEquals(0, emptyTw.readAll(tag0).size());
        assertEquals(1, fullTw.readAll(tag0).size());
        assertEquals("primo tweet!", fullTw.readAll(tag0).get(0));
    }

    @Test
    public void testDelete() throws Exception {
        fullTw.delete(0);
        assertEquals(2, fullTw.getTweetCount());
        fullTw.delete(1);
        assertEquals(1, fullTw.getTweetCount());

        // Test di robustezza su parametri non validi
        try {
            fullTw.delete(42);
            fail("Il messaggio 42 non esiste");
        }
        catch (WrongCodeException e) {}
        try {
            fullTw.delete(0);
            fail("Il messaggio 0 è già stato eliminato");
        }
        catch (WrongCodeException e) {}
    }

    @Test
    public void testSetPassword() throws Exception {
        emptyTw.setPassword("", "qwerty");
        emptyTw.addUser(user0, "qwerty");

        // Test di robustezza su parametri non validi
        try {
            emptyTw.setPassword(null, null);
            fail("Doveva sollevare NullPointerException");
        }
        catch (NullPointerException e) {}
        try {
            emptyTw.setPassword("uiop", "dvorak");
            fail("Doveva sollevare UnauthorizedUserException");
        }
        catch (UnauthorizedUserException e) {}
    }

    @Test
    public void testToString() throws Exception {
        assertNotNull(fullTw.toString());
    }
}