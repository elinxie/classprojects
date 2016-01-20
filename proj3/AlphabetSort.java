import java.util.HashMap;
import java.util.LinkedList;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * AlphabetSort
 * sorts out words in order of given alphabet
 * @author Eric Linxie
 */
public class AlphabetSort {
    private static String alphabetString;
    private static String str;
    private static LinkedList<String> unsortedWords;
    private static Trie usedTrie;
    private static Integer numberOfWords = 0;
    private static Integer wordsPrinted = 0;
    private static Stopwatch testTime;


    /**
     * recursiveAlphabetSorting: Is the recursive function that traverses the nodes of the Trie
     * and prints whenever it hits an ending node
     * @param trie trie examined
     * @param printed the string that is printed if the trie hits the end of a word
     * @param alphabet the alphabet that orders the words given in the input file
     */
    public static void recursiveAlphabetSorting(Trie trie, String printed, String alphabet) {
        String[] splitAlphabet = alphabet.split("");
        HashMap<String, Trie> theTrieTree = trie.trieTree();
        int i = 0;
        String currentCharacter;
        Trie charactersTrie;
        if (theTrieTree.containsKey("ending")) {
            //gives out word
            wordsPrinted += 1;
            System.out.println(printed);
        }
        while (i < splitAlphabet.length && wordsPrinted < numberOfWords) {
            currentCharacter = splitAlphabet[i];
            // System.out.println(currentCharacter);
            if (theTrieTree.containsKey(currentCharacter)) {
                charactersTrie = theTrieTree.get(currentCharacter);
                recursiveAlphabetSorting(charactersTrie, printed + currentCharacter, alphabet);
            }
            i += 1;

        }
        // wordsPrinted = 0;

    }
    /**
     * Test client. Reads the data from the file, 
     * then runs recursiveAlphabetSorting, which prints out the words given in order
     * @param args takes the name of an input file as as a command-line argument
     */
    public static void main(String[] args) {
        // testTime = new Stopwatch();


        //reading the .in file
        usedTrie = new Trie();
        try {
            InputStreamReader in = new InputStreamReader(System.in);
            BufferedReader input = new BufferedReader(in);
            alphabetString = input.readLine();
            if (alphabetString == null || alphabetString.equals("")) {
                throw new IllegalArgumentException();
            }
            
            str = input.readLine();
            if (str == null || str.equals("")) {
                throw new IllegalArgumentException();
            }
            while (str != null) {
                usedTrie.insert(str);
                str = input.readLine();
                numberOfWords += 1;
            }
        } catch (IOException io) {
            System.out.println("There are no words given");
            throw new IllegalArgumentException();

        }
        int j = 0;
        String letter;
        String[] alphabetLetters = alphabetString.split("");
        while (j < alphabetLetters.length) {
            letter = alphabetLetters[j];
            if (alphabetString.length() - alphabetString.replace(letter, "").length() > 1) {
                System.out.println(letter);
                throw new IllegalArgumentException();
            }
            j += 1;
        }

        //printing out the order
        wordsPrinted = 0;
        recursiveAlphabetSorting(usedTrie, "", alphabetString);
        // System.out.println(testTime.elapsedTime());


    }

    
}
