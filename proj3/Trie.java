import java.util.HashMap;


/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Eric Linxie
 */
public class Trie {
    private HashMap<String, Trie> trieTree;
    /**
     *trieTree(), returns private field TrieTree
     *@return HashMap<String, Trie> returns the TrieTree of the Trie
     */
    public HashMap<String, Trie> trieTree() {
        return this.trieTree;
    }

    /**
     * checks if there s is empty or null or not
     * @param s checks if this string is empty or not
     */
    private void thereString(String s) {
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * initializes tree
     */

    public Trie() {
        trieTree = new HashMap<String, Trie>();
    }
    /**
     * Returns true if Trie is found, sees if word is in Trie
     * @param s word to be found
     * @param isFullWord indicates whether s is a prefix or full word
     * @return true or false, depending on whether the string is in Trie
     */
    public boolean find(String s, boolean isFullWord) {
        thereString(s);
        HashMap<String, Trie> loopedTrieTree = trieTree;

        String[] splitString = s.split("");
        int i = 0;
        String currentLetter;
        while (i < splitString.length) {
            currentLetter = splitString[i];
            if (!loopedTrieTree.containsKey(currentLetter)) {
                return false;
            }
            //gets into the map of the next part of the trie
            loopedTrieTree = loopedTrieTree.get(currentLetter).trieTree();
            i += 1;
        }
        if (isFullWord) {
            if (!loopedTrieTree.containsKey("ending")) {
                return false;
            }
        }
        return true;

    }
    /**
     * Puts a word inside of the Trie
     * @param s This is the word that is put into the Trie
     */
    public void insert(String s) {
        thereString(s);
        HashMap<String, Trie> loopedTrieTree = trieTree;

        String[] splitString = s.split("");
        int i = 0;
        String currentLetter;
        while (i < splitString.length) {
            currentLetter = splitString[i];
            if (!loopedTrieTree.containsKey(currentLetter)) {
                loopedTrieTree.put(currentLetter, new Trie());
            }
            //gets into the map of the next part of the trie
            loopedTrieTree = loopedTrieTree.get(currentLetter).trieTree;

            //marks last tree as an ending for a letter
            if (i == splitString.length - 1) {
                loopedTrieTree.put("ending", new Trie());
            }
            i += 1;
        }


    }
}
