package ngordnet;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public class YearlyRecord extends TreeMap<String, Number> {
    /** Constructs a new empty YearlyRecord. */
    TreeMap<Number, String> reverse_record = new TreeMap<Number, String>();

    // private class Word_object {
    // String word;
    // int counts;
    //
    // private Word_object(String word, int counts){
    // this.word = word;
    // this.counts = counts;
    // }
    // }
    //
    // public class Word_compare implements Comparator<Word_object>{
    // public int compare(Word_object e1, Word_object e2){
    // return e1.counts - e2.counts;
    // }
    //
    // }
    //
    // HashSet<Word_object> word_list = new
    // HashSet()<Word_object>(Word_compare);

    public YearlyRecord() {
        super();
    }

    /**
     * Returns the years in which this time series is valid. Doesn't really need
     * to be a NavigableSet. This is a private method and you don't have to
     * implement it if you don't want to.
     */

    /** Creates a copy of TS. */
    public YearlyRecord(HashMap<String, Integer> ts) {
        super(ts);
        // this.reverse_record = ts.reverse_record;
        Iterator<String> mike = ts.keySet().iterator();
        Iterator<Integer> sully = ts.values().iterator();
        while (mike.hasNext() & sully.hasNext()) {
            this.reverse_record.put(sully.next(), mike.next());
        }
    }

    public int count(String word) {
        return (int) this.get(word);
    }

    public void put(String word, int counts) {
        this.reverse_record.put(counts, word);
        super.put(word, counts);
        // this.word_list.add(new Word_object(word, counts));

    }

    public Collection<Number> counts() {
        return this.reverse_record.keySet();
    }

    public Collection<String> words() {
        return this.reverse_record.values();
    }

    public int rank(String word) {
        // Integer to_be_found = (Integer) this.get(word);
        // Object[] to_be_searched = this.counts().toArray();
        // for (int i = 0; i < to_be_searched.length; i += 1) {
        // System.out.println(to_be_searched[i]);
        // }
        // int number = Arrays.binarySearch(to_be_searched, to_be_found);
        // int rank = to_be_searched.length - number;
        // return rank;
        SortedMap<Number, String> fun = this.reverse_record.headMap(this.get(word));
        int rank = this.size() - fun.size();
        if (word != this.reverse_record.get(this.get(word))) {
            rank = rank - 1;
        }
        return rank;
    }
}

/**
 * Returns the quotient of this time series divided by the relevant value in ts.
 * If ts is missing a key in this time series, return an
 * IllegalArgumentException.
 */
// public YearlyRecord<Double> dividedBy(YearlyRecord<? extends Number> ts) {
// Set<Number> given_years = (Set<Number>) ts.years();
// Iterator<Number> given_checking = given_years.iterator();
// YearlyRecord<Double> returned = new YearlyRecord<Double>();
// while (given_checking.hasNext()) {
// Integer examined_year = (Integer) given_checking.next();
// if (this.containsKey(examined_year) == false) {
// throw new IllegalArgumentException("no such year in orginal YearlyRecord");
// }
// Number converting = ts.get(examined_year);
// Double new_value = converting.doubleValue();
// Double new_value2 = this.get(examined_year).doubleValue();
// returned.put(examined_year, new_value2 / new_value);
// }
// return returned;
//
// }
//
// /**
// * Returns the sum of this time series with the given ts. The result is a a
// * Double time series (for simplicity).
// */
// public YearlyRecord<Double> plus(YearlyRecord<? extends Number> ts) {
// Set<Number> given_years = (Set<Number>) ts.years();
// Iterator<Number> given_checking = given_years.iterator();
// YearlyRecord<Double> returned = new YearlyRecord<Double>();
// while (given_checking.hasNext()) {
// Integer examined_year = (Integer) given_checking.next();
// Number converting = ts.get(examined_year);
// Double new_value = converting.doubleValue();
// if (this.containsKey(examined_year) == false) {
// returned.put(examined_year, new_value);
// } else {
// Double new_value2 = this.get(examined_year).doubleValue();
// returned.put(examined_year, new_value + new_value2);
// }
// }
// return returned;
//
// }
//
// /** Returns all years for this time series (in any order). */
// public Collection<Number> years() {
// Iterator<String> year_iterator = this.keySet().iterator();
// Collection<Number> returned = new HashSet<Number>();
// while (year_iterator.hasNext()) {
// returned.add(year_iterator.next());
// }
// return returned;
//
// }
//
// /** Returns all data for this time series (in any order). */
// public Collection<Number> data() {
// Iterator<T> year_iterator = this.values().iterator();
// Collection<Number> returned = new HashSet<Number>();
// while (year_iterator.hasNext()) {
// returned.add(year_iterator.next());
// }
// return returned;
// }
// }