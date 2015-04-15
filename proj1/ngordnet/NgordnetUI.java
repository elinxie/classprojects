/* Starter code for NgordnetUI (part 7 of the project). Rename this file and 
   remove this comment. */

package ngordnet;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.In;
import java.util.*;

/** Provides a simple user interface for exploring WordNet and NGram data.
 *  @author [yournamehere mcjones]
 */
public class NgordnetUI {
    public static void main(String[] args) {
        In in = new In("./ngordnet/ngordnetui.config");
        System.out.println("Reading ngordnetui.config...");

        String wordFile = in.readString();
        String countFile = in.readString();
        String synsetFile = in.readString();
        String hyponymFile = in.readString();
        System.out.println("\nBased on ngordnetui.config, using the following: "
                           + wordFile + ", " + countFile + ", " + synsetFile +
                           ", and " + hyponymFile + ".");

        System.out.println("\nFor tips on implementing NgordnetUI, see ExampleUI.java.");

        NGramMap theNGramMap = new NGramMap(wordFile, countFile);
        WordNet theWordNet = new WordNet(synsetFile, hyponymFile );
        TimeSeries allTheYears = theNGramMap.totalCountHistory();
        Object[] yearArray =  allTheYears.keySet().toArray();
        int startDate = (Integer) yearArray[0];
        int endDate = (Integer) yearArray[yearArray.length - 1];
        WordLengthProcessor wlp = new WordLengthProcessor();

        while (true) {
            System.out.print("> ");
            String line = StdIn.readLine();
            String[] rawTokens = line.split(" ");
            String command = rawTokens[0];
            String[] tokens = new String[rawTokens.length - 1];
            System.arraycopy(rawTokens, 1, tokens, 0, rawTokens.length - 1);
            String word;
            int year;
            try{
            switch (command) {
                case "quit": 
                    return;
                case "help":
                    In bin = new In("help.txt");
                    String helpStr = bin.readAll();
                    System.out.println(helpStr);
                    break;  
                case "range": 
                    startDate = Integer.parseInt(tokens[0]); 
                    endDate = Integer.parseInt(tokens[1]);
                    System.out.println("Start date: " + startDate);
                    System.out.println("End date: " + endDate);
                    break;
                case "count":
                    word = tokens[0];
                    year = Integer.parseInt(tokens[1]);
                    System.out.println(theNGramMap.countInYear(word,year));
                    break;
                case "hyponyms":
                    word = tokens[0];   //can you print Set<String>?
                    if (theWordNet.isNoun(word)){
                      System.out.println(theWordNet.hyponyms(word));
                    }
                    break;
                case "history":
                  ArrayList<String> history_words = new ArrayList<String>();

                  
                    Plotter.plotAllWords(theNGramMap, tokens, startDate, endDate );
                  
                  
                  break;
                     // }
                case "hypohist":

                  
                    Plotter.plotCategoryWeights(theNGramMap, theWordNet, tokens, startDate, endDate );
                    
                 
                  break;

                case "wordlength":

                  Plotter.plotProcessedHistory(theNGramMap, startDate, endDate, wlp);



                case "zipf":
                  year = Integer.parseInt(tokens[0]);
                  
                  Plotter.plotZipfsLaw(theNGramMap, year);
                  
                  
                  break;

                default:
                    System.out.println("Invalid command.");  
                    break;
                  }
                }
                 catch(Exception e){
                    System.out.print(e.getMessage());
                  }

    
              }
            }
          } 
