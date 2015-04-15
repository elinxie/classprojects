package ngordnet;

import org.junit.Test;
import static org.junit.Assert.*;


/** Provides examples of using the NGramMap class.
 *  @author Josh Hug
 */
import java.util.ArrayList;

public class NGramMapTest {

    @Test
    public void testsforNGramMap() {
        NGramMap ngm = new NGramMap("./p1data/ngrams/words_that_start_with_q.csv",
                "./p1data/ngrams/total_counts.csv");

        assertEquals(ngm.countInYear("quantity", 1736), 139); // should print
                                                               // 139
        YearlyRecord yr = ngm.getRecord(1736);
        assertEquals(yr.count("quantity"),139); // should print 139

        TimeSeries<Integer> countHistory = ngm.countHistory("quantity");
        assertEquals((long) countHistory.get(1736), (long) 139); // should print 139
        // assertEquals(countHistory.firstKey(),)

        TimeSeries<Long> totalCountHistory = ngm.totalCountHistory();
        assertEquals((long) totalCountHistory.get(1736), (long) 8049773); // should print 8049773

        TimeSeries<Double> weightHistory = ngm.weightHistory("quantity");
        assertEquals(weightHistory.get(1736),1.7267E-5, 1000); // should print roughly
                                                     // 1.7267E-5

        System.out.println((double) countHistory.get(1736)
                / (double) totalCountHistory.get(1736));

        ArrayList<String> words = new ArrayList<String>();
        words.add("quantity");
        words.add("quality");

        TimeSeries<Double> sum = ngm.summedWeightHistory(words);
        assertEquals(sum.get(1736),3.875E-5, 1000); // should print roughly 3.875E-5


    }
        /** Runs tests. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(NGramMapTest.class);
    }

}
