package ngordnet;

import org.junit.Test;
import static org.junit.Assert.*;


/** Provides examples of using the NGramMap class.
 *  @author Josh Hug
 */
import java.util.ArrayList;

public class WordLengthProcessorTest {

    @Test
    public void testsforProcessor() {

        YearlyRecord dC = new YearlyRecord();
        dC.put("daniel", 1);
        dC.put("hannah", 2);
        dC.put("eric", 3);
        dC.put("carl", 4);
        YearlyRecordProcessor fred = new WordLengthProcessor();
        assertEquals(fred.process(dC), 5.0);
        YearlyRecord eL = new YearlyRecord();
        assertEquals(fred.process(eL), 0.0);
        




    }
        /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(NGramMapTest.class);
    }

}
