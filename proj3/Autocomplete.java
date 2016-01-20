import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashSet;

/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 *@author Eric Linxie
 */




public class Autocomplete {
    private TST theTST;
    private PriorityQueue<TSTNode> pQ;
    private HashSet<String> wordsIn;



    /**
     * Compares two TSTNodes.
     * @param current is one TSTNode.
     * @param other is the other TSTNode.
     */
    private class TSTNodeComparator implements Comparator<TSTNode> {
        @Override
        public int compare(TSTNode current, TSTNode other) {

            double compareInt =  current.max - other.max;
            if (compareInt < 0) {
                return 1;
            }
            if (compareInt == 0 || compareInt > 0) {
                return -1;
            }
            return 1;
        }

    }

    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {
        // if (terms.length < 1 || weights.length < 1){
        //     throw new IllegalArgumentException();
        // }
        if (terms.length != weights.length) {
            throw new IllegalArgumentException();
        }
        theTST = new TST(terms[0], weights[0]);
        wordsIn = new HashSet<String>();

        int i = 1;
        int j = 1;
        while (i < terms.length && j < weights.length) {
            theTST.insert(terms[i], weights[j]);
            if (weights[j] < 0) {
                throw new IllegalArgumentException();
            }
            boolean isNotEmptyWord = true;
            String currentWord = terms[i];
            if (currentWord.equals("") || currentWord.equals(" ")) {
                isNotEmptyWord = false;
            }
            if (isNotEmptyWord) {
                String[] breakWord = currentWord.split(" ");
                int k;
                // System.out.println("the first space: " + breakWord[0]);
                if (breakWord[0].equals("")) {
                    
                    currentWord = breakWord[1];
                    k = 2;
                    while (k < breakWord.length) {
                        currentWord = currentWord + breakWord[k];
                        k += 1;
                    }
                    // System.out.println("current: " + currentWord);
    
                }
            }
            // System.out.println("original: " + terms[i]);
            // System.out.println("copy: " + currentWord);
            if (wordsIn.contains(currentWord)) {
                // System.out.println("wow");
                throw new IllegalArgumentException();
            }
            // System.out.println("......");
            wordsIn.add(terms[i]);
            i += 1;
            j += 1;

        }


    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term whatever is examined
     * @return weight of term
     */
    public double weightOf(String term) {
        TSTNode weightNode = theTST.find(term);
        if (weightNode == null) {
            return 0.0;
        }
        if (weightNode.val == 0) {
            return 0.0;
        }
        return weightNode.val;

    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        Iterable<String> theOneMatch = topMatches(prefix, 1);
        Iterator<String> theOne = theOneMatch.iterator();
        String returned = "nothing yet";
        while (theOne.hasNext()) {
            returned = theOne.next();

        }
        return returned;


    }

    /**
     * Returns the top k matching terms (in descending order of weight) 
     * as an iterable.
     * @param prefix string to be compared
     * @param k number of items to output
     * @return collection of top k words
     */

    public Iterable<String> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException();
        }
        pQ = new PriorityQueue<TSTNode>(k, new TSTNodeComparator());
        // TSTNode placeholderNode = new TSTNode("humprey", 0.0, 0.0);
        // pQ.add(placeholderNode);
        LinkedList<String> toIterable = new LinkedList<String>();
        Boolean notDone = true;
        //finding node to start from in Trie
        TSTNode comparingNode = theTST.find(prefix);
        if (comparingNode == null) {
            System.out.println("prefix not found");
            return toIterable;
        }
        //treating thePrefix like an actual word
        //this might or might not work
        TSTNode thePrefix = comparingNode;
        // Boolean prefixNotWord = true;
        thePrefix.checkThisNode = false;
        // if (thePrefix.fullWord != null){
        //     // thePrefix.max = thePrefix.val;            
        //     // pQ.add(thePrefix);
        //     // System.out.println(thePrefix.max);
        //     // System.out.println(thePrefix.fullWord);
        // }
        // TSTNode sometest = pQ.remove();
        // System.out.println(sometest.fullWord);
        // System.out.println("original node letter: " + comparingNode.letter);
        // System.out.println(comparingNode.letter + " this is duh letter");      
        while (toIterable.size() < k && notDone) {
            if (comparingNode == null) {
                // System.out.println("frank");
                return null;
            }
            if (comparingNode.left != null  && comparingNode.checkThisNode) {
                // System.out.println("left: " + comparingNode.left.letter);
                pQ.add(comparingNode.left);
            }
            if (comparingNode.right != null  && comparingNode.checkThisNode) {
                // System.out.println("right: " + comparingNode.right.letter);
                pQ.add(comparingNode.right);
            }
            if (comparingNode.mid != null) {
                // System.out.println("mid: " + comparingNode.mid.letter);
                pQ.add(comparingNode.mid);
            }
            if (comparingNode.val != 0.0) {
                if (comparingNode.val == comparingNode.max) {
                    // System.out.println("added word: " +  comparingNode.fullWord);
                    if (!toIterable.contains(comparingNode.fullWord)) {
                        toIterable.add(comparingNode.fullWord);
                    }
                } else {
                    TSTNode copy = comparingNode.copy();
                    copy.max = copy.val;
                    if (!comparingNode.checkThisNode) {
                        copy.checkThisNode = false;
                    }
                    pQ.add(copy);
                }
            }
            if (!comparingNode.checkThisNode) {
                comparingNode.checkThisNode = true;
            }
            // System.out.println("size of pQ: " + pQ.size());
            if (pQ.size() > 0) {
                comparingNode = pQ.remove();
                // System.out.println(comparingNode.letter);
            } else {
                notDone = false;
            }
            // System.out.println("itteration");
        }
        return toIterable;
    }

    /**
     * Returns the highest weighted matches within k edit distance of the word.
     * If the word is in the dictionary, then return an empty list.
     * @param word The word to spell-check
     * @param dist Maximum edit distance to search
     * @param k    Number of results to return 
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        LinkedList<String> results = new LinkedList<String>(); 

        /* YOUR CODE HERE; LEAVE BLANK IF NOT PURSUING BONUS */
        return results;
    }
    /**
     * Test client. Reads the data from the file, 
     * then repeatedly reads autocomplete queries from standard input 
     *and prints out the top k matching terms.
     * @param args takes the name of an input file and an integer k as 
     *        command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);

        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }
    }
}
