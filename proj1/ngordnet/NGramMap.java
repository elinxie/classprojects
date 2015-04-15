package ngordnet;

import ngordnet.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class NGramMap {
    /** Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME. */
	private LinkedHashMap<Integer, YearlyRecord> big_map;
	private TimeSeries<Long> total_counts_years = new TimeSeries<Long>();
    private ArrayList<String> words = new ArrayList<String>();
    private TimeSeries<Integer> dummy = new TimeSeries<Integer>();
    private TimeSeries<Double> dummy2 = new TimeSeries<Double>();
    private Integer beginningYear;
    private Integer endingYear;
	
    public NGramMap(String wordsFilename, String countsFilename){
    	big_map = new LinkedHashMap<Integer, YearlyRecord>();
        File words_file = new File(wordsFilename);
        Scanner words_file_Scanner = null;
        try {
            words_file_Scanner = new Scanner(words_file);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        boolean beginning = true;


        while (words_file_Scanner.hasNextLine()) {
            String[] split_line = words_file_Scanner.nextLine().split("\\s+");          
            String word = split_line[0];

            Integer year = Integer.parseInt(split_line[1]);
            Integer counts = Integer.parseInt(split_line[2]);
            if (beginning){
                beginningYear = year;
                beginning = false;
            }
            endingYear = year;
            if (big_map.containsKey(year) == false){
            	big_map.put(year, new YearlyRecord());
            }
            if (words.contains(word) == false){
                words.add(word);
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
            this.dummy.put(year, 0 );
            this.dummy2.put(year, 0.0);
        }


    }
    
    /** Returns the absolute count of WORD in the given YEAR. If the word
      * did not appear in the given year, return 0. */
    public int countInYear(String word, int year){
    	return this.big_map.get(year).count(word);
    }

    /** Returns a defensive copy of the YearlyRecord of WORD. */
    public YearlyRecord getRecord(int year){
        // if (big_map.containsKey(year) == false){
        //     return new YearlyRecord();
        // }
    	YearlyRecord returned = new YearlyRecord();
        YearlyRecord offensiveCopy = this.big_map.get(year);
        Iterator<String> offenseSet = offensiveCopy.words().iterator();
        while (offenseSet.hasNext()){
            String fense = offenseSet.next();
            Integer white = offensiveCopy.count(fense);
            returned.put(fense, white);
        }
        return returned;

    }

    /** Returns the total number of words recorded in all volumes. */
    public TimeSeries<Long> totalCountHistory(){
    	return total_counts_years;
    }

    /** Provides the history of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Integer> countHistory(String word, int startYear, int endYear){
        if (startYear > endYear){
            int shame = startYear;
            int onYou = endYear;
            startYear = onYou;
            endYear = shame;
        }
        if (startYear < beginningYear){
            startYear = beginningYear;
        }

        if (endingYear < endYear){
            endYear = endingYear;
        }
        if (!words.contains(word)){
            return new TimeSeries<Integer>(dummy, startYear, endYear);
        }
    	TimeSeries<Integer> returned = new TimeSeries<Integer>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	int a = 0;
        int bob_number = 0;
        Integer bob2Child = bob.next();
        while (bob.hasNext() && frank.hasNext() && bob2Child != startYear){
            frank.next();
            bob2Child = bob.next();
        }

    	while (frank.hasNext() && bob.hasNext() && a != 1){
            Integer bobChild;
            if (bob_number == 0){
                bobChild = bob2Child;
                bob_number = 1;
            }
            else{
    		  bobChild = bob.next();
            }
    		if (bobChild == endYear){
    			a = 1;
    		}
    		YearlyRecord frankChild = frank.next();
    		if (frankChild.count(word) != 0){
    		returned.put(bobChild, (Integer) frankChild.count(word));
    		}
    	}
    	return returned;
    }

    /** Provides a defensive copy of the history of WORD. */
    public TimeSeries<Integer> countHistory(String word){
        if (!words.contains(word)){
            return dummy;
        }
    	TimeSeries<Integer> returned = new TimeSeries<Integer>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	while (frank.hasNext() && bob.hasNext()){
    		Integer bobChild = bob.next();
    		YearlyRecord frankChild = frank.next();
    		if (frankChild.count(word) != 0){
    		returned.put(bobChild, (Integer) frankChild.count(word));
    		}
    	}
    	return returned;
    }

    /** Provides the relative frequency of WORD between STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> weightHistory(String word, int startYear, int endYear){
        if (startYear > endYear){
            int shame = startYear;
            int onYou = endYear;
            startYear = onYou;
            endYear = shame;
        }
        if (startYear < beginningYear){
            startYear = beginningYear;
        }

        if (endingYear < endYear){
            endYear = endingYear;
        }
        if (!words.contains(word)){
            return new TimeSeries<Double>(dummy2, startYear, endYear);
        }
    	TimeSeries<Double> returned = new TimeSeries<Double>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	int a = 0;
        int bob_number = 0;
        Integer bob2Child = bob.next();
    	while (bob.hasNext() && frank.hasNext() && bob2Child != startYear){
    		frank.next();
            bob2Child = bob.next();
    	}

    	while (frank.hasNext() && bob.hasNext() && a != 1){
            Integer bobChild;
            if (bob_number == 0){
                bobChild = bob2Child;
                bob_number = 1;
            }
            else{
        		bobChild = bob.next();
            }
    		YearlyRecord frankChild = frank.next();
    		Long meekChild = total_counts_years.get(bobChild);
    		if (bobChild == endYear){
    			a = 1;
    		}
    		if (frankChild.count(word) != 0){
    		Double comboChild = ((Integer) frankChild.count(word)).doubleValue() / meekChild.doubleValue();
    		returned.put(bobChild,  comboChild);
    		}
    	}
    	return returned;
    }
    

    /** Provides the relative frequency of WORD. */
    public TimeSeries<Double> weightHistory(String word){
        if (!words.contains(word)){
            return dummy2;
        }
    	TimeSeries<Double> returned = new TimeSeries<Double>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
 
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	while (frank.hasNext() && bob.hasNext()){
    		Integer bobChild = bob.next();
    		YearlyRecord frankChild = frank.next();
    		Long meekChild = total_counts_years.get(bobChild);
    		if (frankChild.count(word) != 0){
    		Double comboChild = ((Integer) frankChild.count(word)).doubleValue() / meekChild.doubleValue();
    		returned.put(bobChild,  comboChild);
    		}
    	}
    	return returned;
    }

    /** Provides the summed relative frequency of all WORDS between
      * STARTYEAR and ENDYEAR. */
    public TimeSeries<Double> summedWeightHistory(Collection<String> words, 
                              int startYear, int endYear){

        // if (!words.contains(word)){
        //     return null;
        // }
        if (startYear > endYear){
            int shame = startYear;
            int onYou = endYear;
            startYear = onYou;
            endYear = shame;
        }
        if (startYear < beginningYear){
            startYear = beginningYear;
        }

        if (endingYear < endYear){
            endYear = endingYear;
        }
    	TimeSeries<Double> returned = new TimeSeries<Double>();
    	Iterator<YearlyRecord> frank = this.big_map.values().iterator();
    	Iterator<Integer> bob = this.big_map.keySet().iterator();
    	int a = 0;
    	Double sum = 0.0;
        int bob_number = 0;
        Integer bob2Child = bob.next();
        while (bob.hasNext() && frank.hasNext() && bob2Child != startYear){
            frank.next();
            bob2Child = bob.next();
        }

        while (frank.hasNext() && bob.hasNext() && a != 1){
            Integer bobChild;
            if (bob_number == 0){
                bobChild = bob2Child;
                bob_number = 1;
            }
            else{
                bobChild = bob.next();
            }
    		Iterator<String> word_iterator = words.iterator();
    		YearlyRecord frankChild = frank.next();
    		Long meekChild = total_counts_years.get(bobChild);
    		if (bobChild == endYear){
    			a = 1;
    		}
    		while (word_iterator.hasNext()){
    		String word = word_iterator.next();
    		if (frankChild.count(word) != 0){
    		Double comboChild = ((Integer) frankChild.count(word)).doubleValue() / meekChild.doubleValue();
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
        // if (!words.contains(word)){
        //     return null;
        // }
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
	    		if (frankChild.count(word) != 0){
	    		Double comboChild = ((Integer) frankChild.count(word)).doubleValue() / meekChild.doubleValue();
	    		sum += comboChild;
	    		}
    		
    	}
            if (sum != 0){
    		  returned.put(bobChild,  sum);
                }
    		sum = 0.0;
    	
    }
    	return returned;
    }

    /** Provides processed history of all words between STARTYEAR and ENDYEAR as processed
      * by YRP. */
    public TimeSeries<Double> processedHistory(int startYear, int endYear,
                                               YearlyRecordProcessor yrp){
        if (startYear > endYear){
            int shame = startYear;
            int onYou = endYear;
            startYear = onYou;
            endYear = shame;
        }
        if (startYear < beginningYear){
            startYear = beginningYear;
        }

        if (endingYear < endYear){
            endYear = endingYear;
        }
            TimeSeries<Double> returned = new TimeSeries<Double>();
            Iterator<Integer> allYears = totalCountHistory().keySet().iterator();
            ;
            int a = 0;
       
            int bob_number = 0;
            Integer bob2Child = allYears.next();
            while (allYears.hasNext() &&  bob2Child != startYear){
                
                bob2Child = allYears.next();
            }

        while (allYears.hasNext() && a != 1){
            Integer bobChild;
            if (bob_number == 0){
                bobChild = bob2Child;
                bob_number = 1;
            }
            else{
                bobChild = allYears.next();
            }

            if (bobChild == endYear){
                a = 1;
            }

            YearlyRecord frud = this.getRecord(bobChild);

            returned.put(bobChild, yrp.process(frud));
            }
            return returned;
            
        }
           
        

        
    

            // yearChecked = yearChecked.next()
            // while(allYears.hasNext() && yearChecked != startYear){
            //     yearChecked = allYears.next();
            // }
            // }

    /** Provides processed history of all words ever as processed by YRP. */
    public TimeSeries<Double> processedHistory(YearlyRecordProcessor yrp) {
        TimeSeries<Double> returned = new TimeSeries<Double>();
            Iterator<Integer> allYears = totalCountHistory().keySet().iterator();
            Integer yearChecked;
            while(allYears.hasNext()){
                yearChecked = allYears.next();

                returned.put(yearChecked, yrp.process(this.getRecord(yearChecked)));

            }
            return returned;

    }
}
