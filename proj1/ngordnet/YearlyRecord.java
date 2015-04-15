package ngordnet;

import java.util.Collections;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.TreeSet;

public class YearlyRecord {
    /** Constructs a new empty YearlyRecord. */
    private TreeMap<String, Integer> originalRecord;
    private TreeMap<Integer, Set<String>> reverse_record = new TreeMap<Integer, Set<String>>();
    private Boolean chached = false;
    private Collection<Integer> chachedSet;

    private HashMap<String, Integer> rankMap;



    public YearlyRecord() {
        originalRecord = new TreeMap<String, Integer>();
        
    }

    /**
     * Returns the years in which originalRecord time series is valid. Doesn't really need
     * to be a NavigableSet. originalRecord is a private method and you don't have to
     * implement it if you don't want to.
     */

    /** Creates a copy of TS. */


    public YearlyRecord(HashMap<String, Integer> ts) {
        originalRecord = new TreeMap<String, Integer>();
        
        // reverse_record = ts.reverse_record;
        Iterator<String> mike = ts.keySet().iterator();
        Iterator<Integer> sully = ts.values().iterator();
        while (mike.hasNext() & sully.hasNext()) {
            String mikeChild = mike.next();
            Integer sullyChild = sully.next();
            originalRecord.put(mikeChild, sullyChild);
            Set<String> archie = new HashSet<String>();
            archie.add(mikeChild);
            if (reverse_record.containsKey(sullyChild)){
                Iterator<String> copy_over = reverse_record.get(sullyChild).iterator();
                while (copy_over.hasNext()){
                    archie.add(copy_over.next());
                }
            }

            reverse_record.put(sullyChild, archie);
            
        }
        chached = true;
    }

    public int size(){
        return originalRecord.size();
    }

    public int count(String word) {
        if (originalRecord.containsKey(word)){
            return (int) originalRecord.get(word);
        }
        else {
            return 0;
        }
    }

    public void put(String word, int counts) {

        Integer removedValue;
        if (originalRecord.containsKey(word)){
            removedValue = originalRecord.get(word);
            Set<String> checkedSet = reverse_record.get(removedValue);
            if (checkedSet != null){
                checkedSet.remove(word);
            }
            if (checkedSet.size() == 0){
                reverse_record.remove(removedValue);
            }
        }



        //puts new word in
        Set<String> archie = new HashSet<String>();
        archie.add(word);
        if (reverse_record.containsKey(counts)){
            Iterator<String> copy_over = reverse_record.get(counts).iterator();
            while (copy_over.hasNext()){
                archie.add(copy_over.next());
            }
            
        }
        reverse_record.put(counts, archie);
        originalRecord.put(word, counts);
        chached = true;


        
    }

    public Collection<Number> counts() {
        Iterator<Integer> valIterator =  originalRecord.values().iterator();
        TreeSet<Number> returned = new TreeSet<Number>();
        while (valIterator.hasNext()){
            returned.add((Number) valIterator.next());
        }
        return returned;
    }

    public Collection<String> words() {
        // Collection<String> returning = new ArrayList<String>();
        // Collection<Set<String>> frod = reverse_record.values();
        // Iterator<Set<String>> froddish = frod.iterator();
        // while (froddish.hasNext()){
        //     Set<String> frodmom = froddish.next();
        //     Iterator<String> frodChild =  frodmom.iterator();
        //     while (frodChild.hasNext()){
        //         String frodword = frodChild.next();
        //         System.out.println(frodword);
        //         returning.add(frodword);
        //     }
        // }

        // return returning;

        Set<String> theSet = originalRecord.keySet();
        ArrayList<String> returning = new ArrayList<String>();
        returning.addAll(theSet);
        Collections.sort(returning, new StringComparator(originalRecord));
        return returning;
    }

    private class StringComparator implements Comparator<String>{

        TreeMap<String, Integer> map;
        public StringComparator(TreeMap<String, Integer> map) {
            this.map = map;
        }

        @Override
        public int compare(String a, String b) {
            if ((Integer) map.get(a) >= (Integer) map.get(b)) {
                return 1;
            } else {
                return -1;
            }
        }
    }





    public int rank(String word) {

        if (this.count(word) == 0){
            return 0;
        }

        int i = 1;

        if (chached || originalRecord.size() == 0){

            chachedSet = reverse_record.keySet();

            Iterator<Integer> funs = chachedSet.iterator();
            rankMap = new HashMap<String, Integer>();

            while (funs.hasNext()){
                Iterator<String> noFun = reverse_record.get(funs.next()).iterator();
                while (noFun.hasNext()){
                    rankMap.put(noFun.next(), i);
                    i += 1;
                }  
            }
         }

         int rank = rankMap.size() - rankMap.get(word) + 1;

        chached = false;
        return rank;
    }
}

