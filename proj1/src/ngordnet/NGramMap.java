package ngordnet;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NGramMap {
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
	LinkedHashMap<Integer, YearlyRecord> big_map;
	TimeSeries<Long> total_counts_years = new TimeSeries<Long>();
	
    public NGramMap(String wordsFilename, String countsFilename){
    	big_map = new LinkedHashMap<Integer, YearlyRecord>();
        File words_file = new File(wordsFilename);
        Scanner words_file_Scanner = null;
        try {
            words_file_Scanner = new Scanner(words_file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        while (words_file_Scanner.hasNextLine()) {
            String[] split_line = words_file_Scanner.nextLine().split(" ");          
            String word = split_line[0];
            Integer year = Integer.parseInt(split_line[1]);
            Integer counts = Integer.parseInt(split_line[2]);
            if (big_map.containsKey(year) == false){
            	big_map.put(year, new YearlyRecord());
            }
            this.big_map.get(year).put(word, counts);

        }


        File counts_file = new File(countsFilename);
        Scanner counts_file_Scanner = null;
        try {
            counts_file_Scanner = new Scanner(counts_file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (counts_file_Scanner.hasNextLine()) {
            String[] split_line = counts_file_Scanner.nextLine().split(",");
//            ArrayList<String> string_line = new ArrayList<String>();
//            for (Integer i = 0; i < 2; i += 1) {
//                string_line.add(split_line[i]);
//            }
            Integer year = Integer.parseInt(split_line[0]); // changes
            Long totalCounts = Long.parseLong(split_line[1]);

            this.total_counts_years.put(year, totalCounts);
        }

    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year){
    	return this.big_map.get(year).count(word);
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year){
    	return this.big_map.get(year);
    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory(){
    	return total_counts_years;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear){
    	TimeSeries<Integer> returned = new TimeSeries<Integer>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	int a = 0;
    	while (bob.hasNext() && frank.hasNext() && bob.next() != startYear){
    		frank.next();
    	}
    	while (frank.hasNext() && bob.hasNext() && a != 1){
    		Integer bobChild = bob.next();
    		if (bobChild == endYear){
    			a = 1;
    		}
    		YearlyRecord frankChild = frank.next();
    		if (frankChild.containsKey(word)){
    		returned.put(bobChild, (Integer) frankChild.get(word));
    		}
    	}
    	return returned;
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word){
    	TimeSeries<Integer> returned = new TimeSeries<Integer>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	while (frank.hasNext() && bob.hasNext()){
    		Integer bobChild = bob.next();
    		YearlyRecord frankChild = frank.next();
    		if (frankChild.containsKey(word)){
    		returned.put(bobChild, (Integer) frankChild.get(word));
    		}
    	}
    	return returned;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear){
    	TimeSeries<Double> returned = new TimeSeries<Double>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	int a = 0;
    	while (bob.hasNext() && frank.hasNext() && bob.next() != startYear){
    		frank.next();
    	}
    	while (frank.hasNext() && bob.hasNext() && a != 1){
    		Integer bobChild = bob.next();
    		YearlyRecord frankChild = frank.next();
    		Long meekChild = total_counts_years.get(bobChild);
    		if (bobChild == endYear){
    			a = 1;
    		}
    		if (frankChild.containsKey(word)){
    		Double comboChild = ((Integer) frankChild.get(word)).doubleValue() / meekChild.doubleValue();
    		returned.put(bobChild,  comboChild);
    		}
    	}
    	return returned;
    }
    

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word){
    	TimeSeries<Double> returned = new TimeSeries<Double>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
 
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	while (frank.hasNext() && bob.hasNext()){
    		Integer bobChild = bob.next();
    		YearlyRecord frankChild = frank.next();
    		Long meekChild = total_counts_years.get(bobChild);
    		if (frankChild.containsKey(word)){
    		Double comboChild = ((Integer) frankChild.get(word)).doubleValue() / meekChild.doubleValue();
    		returned.put(bobChild,  comboChild);
    		}
    	}
    	return returned;
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear){
    	TimeSeries<Double> returned = new TimeSeries<Double>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	int a = 0;
    	Double sum = 0.0;
    	while (bob.hasNext() && frank.hasNext() && bob.next() != startYear){
    		frank.next();
    	}
    	while (frank.hasNext() && bob.hasNext() && a != 1){
    		Iterator<String> word_iterator = words.iterator();
    		Integer bobChild = bob.next();
    		YearlyRecord frankChild = frank.next();
    		Long meekChild = total_counts_years.get(bobChild);
    		if (bobChild == endYear){
    			a = 1;
    		}
    		while (word_iterator.hasNext()){
    		String word = word_iterator.next();
    		if (frankChild.containsKey(word)){
    		Double comboChild = ((Integer) frankChild.get(word)).doubleValue() / meekChild.doubleValue();
    		sum += comboChild;
    		}
    		
    	}
    		returned.put(bobChild,  sum);
    		sum = 0.0;
    	
    }
    	return returned;
    }

    /** Returns the summed relative frequency of all WORDS. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words){
    	TimeSeries<Double> returned = new TimeSeries<Double>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();

    	Double sum = 0.0;

    	while (frank.hasNext() && bob.hasNext()){
    		Iterator<String> word_iterator = words.iterator();
    		Integer bobChild = bob.next();
    		YearlyRecord frankChild = frank.next();
    		Long meekChild = total_counts_years.get(bobChild);
    		
    		while (word_iterator.hasNext()){
	    		String word = word_iterator.next();
	    		if (frankChild.containsKey(word)){
	    		Double comboChild = ((Integer) frankChild.get(word)).doubleValue() / meekChild.doubleValue();
	    		sum += comboChild;
	    		}
    		
    	}
    		returned.put(bobChild,  sum);
    		sum = 0.0;
    	
    }
    	return returned;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp){return new TimeSeries<Double>();}

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {return new TimeSeries<Double>();}
}
