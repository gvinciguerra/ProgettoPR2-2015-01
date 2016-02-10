/**
 * Created by Giorgio Vinciguerra on 01/06/15.
 * Questa classe contiene esempi d'uso delle classi definite in questa
 * cartella. Per i test, vedere la sottocartella "tests".
 */

public class Main {

    public static void main(String[] args) {
        // --- Tag class ---
        Tag t33 = new Tag("3");
        Tag t22 = new Tag("2", t33);
        Tag t11 = new Tag("1", t22);
        Tag t3 = new Tag("third", t11);
        Tag t2 = new Tag("second", t3);
        Tag t1 = new Tag("first", t2);
        assert t1.containsTag("2") && t1.containsTag("third");
        assert t1.containsTag(new Tag("1", new Tag("second")));
        System.out.println(t1); // #first #second #third #1 #2 #3

        // --- Tweet & User class ---
        User usr1 = new User("Phil");
        User usr2 = new User("Bob");
        User usr3 = new User("Angela");
        Tweet tw1 = new Tweet("Hey! Are you ok?", t22, usr1);
        Tweet tw2 = new Tweet("Yep", null, usr2);
        Tweet tw3 = tw2.retweet(usr3);
        Tweet tw4 = tw3.retweet(DeletedUser.getInstance());
        System.out.println(tw1); // Phil: Hey! Are you ok? #2 #3
        System.out.println(tw2); // Bob: Yep
        System.out.println(tw3); // Angela: Yep
        System.out.println(tw4); // ‹Deleted user›: Yep

        // --- myTw class ---
        String pwd = "2015";
        myTw sys = new myTw(pwd);
        sys.addUser(usr1, pwd);
        sys.addUser(usr2, pwd);
        sys.addUser(usr3, pwd);
        sys.tweet(tw1);
        sys.tweet(tw2);
        sys.tweet(tw3);
        System.out.println(sys.allUsers()); // [Phil, Bob, Angela]
        int id = sys.tweet("What?", t33, usr2);
        sys.deleteUser(usr2, false, pwd);
        sys.delete(id);
        System.out.println(sys);
        /* (
	            [Phil, Angela],
	            [Phil: Hey! Are you ok? #2 #3, ‹Deleted user›: Yep, Angela: Yep],
	            •••••••
            )
         */
    }
}
