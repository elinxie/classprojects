package demos;

/** Provides examples of using the YearlyRecord class.
 *  @author Josh Hug
 */
import java.util.Collection;
import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;

import ngordnet.YearlyRecord;

@Test
public class YearlyRecordTest(){
            YearlyRecord yr = new YearlyRecord();
        yr.put("quayside", 95);
        yr.put("surrogate", 340);
        yr.put("merchantman", 181);
        assertEquals(yr.rank("surrogate")); // should print 1
        assertEquals(yr.rank("quayside")); // should print 3
        assertEquals(yr.size()); // should print 3

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

        HashMap<String, Integer> rawData = new HashMap<String, Integer>();
        rawData.put("berry", 1290);
        rawData.put("eric", 1290);
        rawData.put("auscultating", 6);
        rawData.put("temporariness", 20);
        rawData.put("puppetry", 191);
        YearlyRecord yr2 = new YearlyRecord(rawData);

        System.out.println(yr2.rank("auscultating")); // should print 4
        System.out.println(yr2.rank("berry")); // should print 4
        System.out.println(yr2.rank("eric")); // should print 4
}

public class YearlyRecordDemo {
    public static void main(String[] args) {


        jh61b.junit.textui.runClasses(ArrayList61BTest.class);

    }
}
