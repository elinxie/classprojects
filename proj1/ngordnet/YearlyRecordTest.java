package ngordnet;

/** Provides examples of using the YearlyRecord class.
 *  @author Josh Hug
 */
import java.util.Collection;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Iterator;


import ngordnet.YearlyRecord;

public class YearlyRecordTest {

    @Test
    public void YearlyRecordTest(){

        YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", 95);
        yr.put("surrogate", 340);
        yr.put("merchantman", 181);
        assertEquals(yr.count("surrogate"),340);
        assertEquals(yr.rank("surrogate"), 1); // should print 1
        assertEquals(yr.rank("quayside"),3); // should print 3
        assertEquals(yr.size(),3); // should print 3

        Collection<String> words = yr.words();

        /*
         * The code below should print:
         * 
         * quayside appeared 95 times. merchantman appeared 181 times. surrogate
         * appeared 340 times.
         */
        for (String word : words) {
            System.out.println(word + " appeared " + yr.count(word) + " times.");
        }

        /*
         * The code below should print the counts in ascending order:
         * 
         * 95 181 340
         */

        Collection<Number> counts = yr.counts();
        for (Number count : counts) {

            System.out.println(count);

        }
        Iterator<Number> count_it = counts.iterator();
        assertEquals(95, count_it.next());
        Iterator<String> word_it = words.iterator();
        assertEquals("quayside", word_it.next());
        assertEquals("merchantman", word_it.next());
        assertEquals("surrogate", word_it.next());
        assertEquals(3, words.size());

        HashMap<String, Integer> rawData = new HashMap<String, Integer>();
        rawData.put("berry", 1290);
        rawData.put("eric", 1290);
        rawData.put("auscultating", 6);
        rawData.put("temporariness", 20);
        rawData.put("puppetry", 191);
        YearlyRecord yr2 = new YearlyRecord(rawData);

        assertEquals(yr2.rank("auscultating"),5); // should print 5
        assertEquals(yr2.rank("berry"),2); // should print 2
        assertEquals(yr2.rank("eric"),1); // should print 1
        HashMap<String, Integer> testData = new HashMap<String, Integer>();
        testData.put("fried", 59);
        YearlyRecord yr5 = new YearlyRecord(testData);
        assertEquals(yr5.rank("fried"), 1);
        yr5.put("fried", 10);
        yr5.put("fried", 111);
        
        assertEquals(yr5.rank("fried"),1); //checks if when you put in word and counts it replaces previous mapping of word
        assertEquals(yr5.rank("chicken"), 0); //checks if you try rank and count for word not in list. Null cannot be 
        assertEquals(yr5.count("chicken"), 0); //returned so we return 0 instead. 



}


    public static void main(String[] args) {


        jh61b.junit.textui.runClasses(YearlyRecordTest.class);

    }
}
