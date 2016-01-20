import java.util.HashMap;
import java.util.LinkedList;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;


public class AlphabetSort2{
	private static String alphabetString;
	private static String str;
	private static ArrayList<String> unsortedWords;
	private static Trie usedTrie;
    private static Integer numberOfWords = 0;
    private static Integer wordsPrinted = 0;
    private static HashMap<String, Integer> alphabetMap;
    private static Integer lastArrayIndex;
    private static ArrayList<String> sortedWords;



    // public static void recursiveAlphabetSorting(Trie trie, String printed, String alphabet){
    // 	String[] splitAlphabet = alphabet.split("");
    // 	HashMap<String, Trie> theTrieTree = trie.trieTree;
    // 	int i = 0;
    // 	String currentCharacter;
    // 	Trie charactersTrie;
    //     if (theTrieTree.containsKey("ending") == true){
    //     	//gives out word
    //         wordsPrinted += 1;
    //     	System.out.println(printed);
    //     }
    // 	while (i < splitAlphabet.length && wordsPrinted < numberOfWords){
    // 		currentCharacter = splitAlphabet[i];
    // 		if (theTrieTree.containsKey(currentCharacter)){
    //             charactersTrie = theTrieTree.get(currentCharacter);
    // 			recursiveAlphabetSorting(charactersTrie, printed + currentCharacter, alphabet);
    // 		}
    // 		i += 1;

    // 	}

    // }
    // public static Trie makeATrie(LinkedList<String> wordList){
    // 	Trie madeTrie = new Trie();
    // 	while (wordList.size() > 0){
    // 		str = wordList.remove();
    // 		madeTrie.insert(str);
    // 	}
    // 	return madeTrie;
    // }

    public static void switching(ArrayList<String> array, int first, int second ){
        String firstObj = array.get(first);
        String secondObj = array.get(second);
        // array.remove(first);
        // array.remove(second);
        // System.out.println("switching: " + array.get(first));
        array.add(first, secondObj);
        array.add(second, firstObj);
        // System.out.println("switching: " + array.get(first));
        array.remove(first + 1 );
        array.remove(second + 1);

        int i = 0;
        while (i < array.size()){
            // System.out.println("tracking: " + array.get(i));
            i += 1;
        }

    }

    public static Boolean greaterThan(String[] wordLetters, String[] grabbedWordLetters){
        int i = 0;
        String wordSingleLetter;
        String grabbedWordSingleLetter;
        int wordLetterNumber;
        int grabbedWordLetterNumber;
        while(i < wordLetters.length && i < grabbedWordLetters.length){
            wordSingleLetter = wordLetters[i];
            grabbedWordSingleLetter = grabbedWordLetters[i];
            if (!alphabetMap.containsKey(wordSingleLetter)){
                throw new IllegalArgumentException();
            }
            wordLetterNumber = alphabetMap.get(wordSingleLetter);
            grabbedWordLetterNumber = alphabetMap.get(grabbedWordSingleLetter);
            if (wordLetterNumber < grabbedWordLetterNumber){
                return false;
            }
            if (wordLetterNumber > grabbedWordLetterNumber){
                return true;
            }
            else{
                i += 1;
            }

        }
        if (wordLetters.length < grabbedWordLetters.length){
            return false;
        }
        else{
            return true;
        }

    }

    public static void sorting(String word, ArrayList<String> array ){
        int addedWordNumber = array.size();
        array.add(addedWordNumber, word);
        // System.out.println(addedWordNumber);
        // lastArrayIndex += 1;
        String grabbedWord;
        String[] splitWord = word.split("");
        String[] splitGrabbedWord;
        while (addedWordNumber > 0){
            grabbedWord = array.get(addedWordNumber - 1);
            splitGrabbedWord = grabbedWord.split("");
            if (greaterThan(splitWord, splitGrabbedWord)){
                return;
            }
            else{
                switching(array, addedWordNumber - 1, addedWordNumber);
                addedWordNumber -= 1;
            }

        }



    }

    public static void main(String[] args){

    	//reading the .in file
    	unsortedWords = new ArrayList<String>();
        sortedWords = new ArrayList<String>();
    	try{
    		InputStreamReader in = new InputStreamReader(System.in);
    		BufferedReader input = new BufferedReader(in);
    		alphabetString = input.readLine();
    		if (alphabetString == null || alphabetString == ""){
    			throw new IllegalArgumentException();
    		}

    		str = input.readLine();
            if (str == null || str == ""){
                throw new IllegalArgumentException();
            }
            
    		while (str != null){
    			unsortedWords.add(str);
    			str = input.readLine();
                numberOfWords += 1;
    		}



    	}
    	catch (IOException io){
    		System.out.println("There are no words given");
    		throw new IllegalArgumentException();

    	}

        alphabetMap = new HashMap<String, Integer>();
        String[] alphabetLetters = alphabetString.split("");
    	int j = 0;
    	String letter;
    	// String[] alphabetLetters = alphabetString.split("");
    	while (j < alphabetLetters.length){
            letter = alphabetLetters[j];
            if (alphabetString.length() - alphabetString.replace(letter, "").length() > 1) {
                System.out.println(letter);
                throw new IllegalArgumentException();
            }
            alphabetMap.put(alphabetLetters[j], j);
            j += 1;
        }
        //for checking the last value something is in the outputed list. 
        lastArrayIndex = 0;

        //sorts words
        int k = 0;
        String chosenWord;
        while (k < unsortedWords.size()){
            chosenWord = unsortedWords.get(k);
            sorting(chosenWord, sortedWords);
            k += 1; 
        }

        //prints words
        int l = 0;
        String printedWord;
        while (l < sortedWords.size()){
            printedWord = sortedWords.get(l);
            System.out.println(printedWord);
            l += 1;
        }


    	//making the trie
        // usedTrie = makeATrie(unsortedWords);

        //printing out the order
        // recursiveAlphabetSorting(usedTrie, "", alphabetString);


    }

    
}