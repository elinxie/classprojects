package ngordnet;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;



/**
 * Class that demonstrates the use of a TimeSeries. This file is not intended to
 * be compiled or executed. Though you're welcome to do so after you finish the
 * TimeSeries class.
 *
 * @author Josh Hug
 */

public class TimeSeriesTest {
    @Test
    public void TimeToTest() {
        // Create a new time series that maps to Double
        TimeSeries<Double> ts = new TimeSeries<Double>();

        /*
         * You will not need to implement the put method, since your TimeSeries
         * class should extend the TreeMap class.
         */
        ts.put(1992, 3.6);
        ts.put(1993, 9.2);
        ts.put(1994, 15.2);
        ts.put(1995, 16.1);
        ts.put(1996, -15.7);

        TimeSeries<Double> tsCopy = new TimeSeries<Double>(ts, 1993, 1995);

        TimeSeries<Double> ts6 = new TimeSeries<Double>();
        TimeSeries<Double> ts4 = new TimeSeries<Double>();
        ts4.put(1996, 10.0);
        TimeSeries<Double> ts5 = ts6.plus(ts4);
        Collection<Number> ts5years = ts5.years();
        for (Number year5Number : ts5years) {
            /*
             * This awkward conversion is necessary since you cannot do
             * yearNumber.get(yearNumber), since get expects as Integer since
             * TimeSeries always require an integer key.
             * 
             * Your output may be in any order.
             */

            System.out.println(year5Number + "and" + ts5.get(year5Number));
        }

        /*
         * Gets the years and data of this TimeSeries. Note, you should never
         * cast these to another type, even if you happen to know how the
         * Collection<Number> is implemented.
         */
        Collection<Number> years = ts.years();
        Collection<Number> data = ts.data();

        for (Number yearNumber : years) {
            /*
             * This awkward conversion is necessary since you cannot do
             * yearNumber.get(yearNumber), since get expects as Integer since
             * TimeSeries always require an integer key.
             * 
             * Your output may be in any order.
             */
            int year = yearNumber.intValue();
            double value = ts.get(year);
            System.out.println("In the year " + year + " the value was " + value);
        }

        Collection<Number> copyyears = tsCopy.years();
        Collection<Number> copydata = tsCopy.data();

        for (Number yearNumber : copyyears) {
            /*
             * This awkward conversion is necessary since you cannot do
             * yearNumber.get(yearNumber), since get expects as Integer since
             * TimeSeries always require an integer key.
             * 
             * Your output may be in any order.
             */
            int year = yearNumber.intValue();
            double value = ts.get(year);
            System.out.println("In the year " + year + " the value was " + value);
        }

        TimeSeries<Integer> ts2 = new TimeSeries<Integer>();
        ts2.put(1991, 10);
        ts2.put(1992, -5);
        ts2.put(1993, 1);

        TimeSeries<Double> tSum = ts.plus(ts2);
        assertEquals((double) tSum.get(1991), (double) 10.0, 0.01); // should print 10
        assertEquals((double) tSum.get(1992), (double) -1.4, 0.01); // should print -1.4

        TimeSeries<Double> ts3 = new TimeSeries<Double>();
        ts3.put(1991, 5.0);
        ts3.put(1992, 1.0);
        ts3.put(1993, 100.0);

        TimeSeries<Double> tQuotient = ts2.dividedBy(ts3);

        assertEquals((double) tQuotient.get(1991), (double) 2.0, 0.01); // should print 2.0
        try {
            TimeSeries<Double> quotient = ts.dividedBy(ts2);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        /*
         * The following would cause an IllegalArgumentException since ts2 does
         * not include all years from ts, which is tantamount to a divide by
         * zero error.
         * 
         * TimeSeries<Double> quotient = ts.dividedBy(ts2);
         */

        /*
         * The following code would fail at compile time, because a TimeSeries's
         * formal type parameter is defined as having a type upper bound of
         * Number, and String is-not-a Number.
         * 
         * TimeSeries<String> tsS = new TimeSeries<String>();
         */

        TimeSeries<Double> tsfrank = new TimeSeries<Double>();
        TimeSeries<Double> tsbob = new TimeSeries<Double>();
        tsbob.put(1997, 19.0);
        TimeSeries<Double> tsfrankchild = tsfrank.plus(tsbob);
        Double frankmom = tsfrankchild.get(1997);
        assertEquals(frankmom, 19.0);
        TimeSeries<Double> tsbobchild = tsbob.plus(tsfrank);
        Double bobmom = tsbobchild.get(1997);
        assertEquals(bobmom, 19.0);

    }
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TimeSeriesTest.class);
    }
}
