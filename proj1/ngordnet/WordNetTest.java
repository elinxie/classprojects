package ngordnet;

import ngordnet.WordNet;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Class that demonstrates basic WordNet functionality.
 *
 * @author Josh Hug
 */
public class WordNetTest{

    @Test
    public void nounsTest(){
        WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");
        assertTrue(wn.isNoun("demotion"));
        assertTrue(wn.isNoun("descent"));
        assertTrue(wn.isNoun("augmentation"));
        assertTrue(wn.isNoun("jump"));
        assertTrue(wn.isNoun("leap"));
        assertTrue(wn.isNoun("nasal_decongestant"));
        assertTrue(wn.isNoun("change"));
        assertTrue(wn.isNoun("action"));
        assertTrue(wn.isNoun("actifed"));
        assertTrue(wn.isNoun("increase"));
        assertTrue(wn.isNoun("antihistamine"));
        assertTrue(wn.isNoun("parachuting"));
        //means all words in the List

        assertFalse(wn.isNoun("Fred"));
        assertFalse(wn.isNoun("2"));
        assertFalse(wn.isNoun("eighty_five"));
        assertFalse(wn.isNoun("prohistamine"));
        assertFalse(wn.isNoun("leep")); //similarly spelled
        //words not in list


    }

    public void hyponymTests(){

    }




    public static void main(String[] args) {
        WordNet wn = new WordNet("./wordnet/synsets11.txt", "./wordnet/hyponyms11.txt");

        /* These should all print true. */
        System.out.println(wn.isNoun("jump"));
        System.out.println(wn.isNoun("leap"));
        System.out.println(wn.isNoun("nasal_decongestant"));

        /*
         * The code below should print the following (maybe not in this order):
         * All nouns: augmentation nasal_decongestant change action actifed
         * antihistamine increase descent parachuting leap demotion jump
         */

        System.out.println("All nouns:");
        for (String noun : wn.nouns()) {
            System.out.println(noun);
        }

        /*
         * The code below should print the following (maybe not in this order):
         * Hypnoyms of increase: augmentation increase leap jump
         */

        System.out.println("Hypnoyms of increase:");
        for (String noun : wn.hyponyms("increase")) {
            System.out.println(noun);
        }

        /*
         * The code below should print the following (maybe not in this order):
         * Hypnoyms of jump: parachuting leap jump
         */

        System.out.println("Hypnoyms of jump:");
        for (String noun : wn.hyponyms("jump")) {
            System.out.println(noun);
        }

        /*
         * The code below should print the following (maybe not in this order):
         * Hypnoyms of change: alteration saltation modification change
         * variation increase transition demotion leap jump
         */

        /** From: http://goo.gl/EGLoys */
        System.out.println("Hypnoyms of change:");

        WordNet wn2 = new WordNet("./wordnet/synsets14.txt", "./wordnet/hyponyms14.txt");
        for (String noun : wn2.hyponyms("change")) {
            System.out.println(noun);

        jh61b.junit.textui.runClasses(WordNetTest.class);
        }
    }




}
