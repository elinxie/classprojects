package ngordnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class TimeSeries<T extends Number> extends TreeMap<Integer, T> {
    /** Constructs a new empty TimeSeries. */
    private int ngord;
    public TimeSeries() {
        super();


    }

    /**
     * Returns the years in which this time series is valid. Doesn't really need
     * to be a NavigableSet. This is a private method and you don't have to
     * implement it if you don't want to.
     */
    // private NavigableSet<Integer> validYears(int startYear, int endYear) {
    //
    // }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR. inclusive
     * of both end points.
     */
    public TimeSeries(TimeSeries<T> ts, int startYear, int endYear) {
        super(ts);
        while (this.firstKey() != startYear) {
            this.remove(this.firstKey());
        }
        while (this.lastKey() != endYear) {
            this.remove(this.lastKey());
        }

    }

    /** Creates a copy of TS. */
    public TimeSeries(TimeSeries<T> ts) {
        super(ts);
    }

    /**
     * Returns the quotient of this time series divided by the relevant value in
     * ts. If ts is missing a key in this time series, return an
     * IllegalArgumentException.
     */
    public TimeSeries<Double> dividedBy(TimeSeries<? extends Number> ts) {
        Collection<Number> given_years = (Collection<Number>) ts.years();
        Iterator<Number> given_checking = given_years.iterator();
        TimeSeries<Double> returned = new TimeSeries<Double>();
        
        while (given_checking.hasNext()) {
            Integer examined_year = (Integer) given_checking.next();
            if (this.containsKey(examined_year) == false) {
                throw new IllegalArgumentException("no such year in orginal TimeSeries");
            }
            Number converting = ts.get(examined_year);
            Double new_value = converting.doubleValue();
            Double new_value2 = this.get(examined_year).doubleValue();
            returned.put(examined_year, new_value2 / new_value);
        }
        return returned;

    }

    /**
     * Returns the sum of this time series with the given ts. The result is a a
     * Double time series (for simplicity).
     */
    public TimeSeries<Double> plus(TimeSeries<? extends Number> ts) {
        Collection<Number> given_years = (Collection<Number>) ts.years();
        Iterator<Number> given_checking = given_years.iterator();
        TimeSeries<Double> returned = new TimeSeries<Double>();
        while (given_checking.hasNext()) {
            Integer examined_year = (Integer) given_checking.next();
            Number converting = ts.get(examined_year);
            Double new_value = converting.doubleValue();
            if (this.containsKey(examined_year) == false) {
                returned.put(examined_year, new_value);
            } else {
                Double new_value2 = this.get(examined_year).doubleValue();
                returned.put(examined_year, new_value + new_value2);
            }
        }
        return returned;

    }

    /** Returns all years for this time series (in any order). */
    public Collection<Number> years() {
        Iterator<Integer> year_iterator = this.keySet().iterator();
        Collection<Number> returned = new ArrayList<Number>();
        while (year_iterator.hasNext()) {
            returned.add(year_iterator.next());
        }
        return returned;

    }

    /** Returns all data for this time series (in any order). */
    public Collection<Number> data() {
        Iterator<T> year_iterator = this.values().iterator();
        Collection<Number> returned = new ArrayList<Number>();
        while (year_iterator.hasNext()) {
            returned.add(year_iterator.next());
        }
        return returned;
    }
}