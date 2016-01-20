
import static org.junit.Assert.*;

import org.junit.Test;


public class TrieTest{



	@Test
	public void testPieceStuff(){
        Trie t = new Trie();
        t.insert("hello");
        t.insert("hey");
        t.insert("goodbye");
        assertTrue(t.find("hell", false));
        assertTrue(t.find("hello", true));
        assertTrue(t.find("good", false));
        assertFalse(t.find("bye", false));
        assertFalse(t.find("heyy", false));
        assertFalse(t.find("hell", true)); 
        try{
            t.insert("");
        }
        catch (IllegalArgumentException e){
            System.out.println("it works!");
        }
        t.insert("josh");
        t.insert("joshhugs");
        t.insert("joshhubs");
        assertTrue(t.find("josh", true));    
        assertTrue(t.find("josh", false));  
        assertFalse(t.find("joshshrugs", false)); 
        assertTrue(t.find("jos", false));     
        assertTrue(t.find("joshhugs", true)); 
        assertTrue(t.find("joshhubs", true)); 
        }


	public static void main(String[] args) {
    jh61b.junit.textui.runClasses(TrieTest.class);
    }

}