import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

/**
 * Class that provides JUnit tests for Gitlet, as well as a couple of utility
 * methods.
 * 
 * @author Joseph Moghadam
 * 
 *         Some code adapted from StackOverflow:
 * 
 *         http://stackoverflow.com/questions
 *         /779519/delete-files-recursively-in-java
 * 
 *         http://stackoverflow.com/questions/326390/how-to-create-a-java-string
 *         -from-the-contents-of-a-file
 * 
 *         http://stackoverflow.com/questions/1119385/junit-test-for-system-out-
 *         println
 * 
 */
public class GitletSmallTest {
    private static final String GITLET_DIR = ".gitlet/";
    private static final String TESTING_DIR = "test_files/";

    /* matches either unix/mac or windows line separators */
    private static final String LINE_SEPARATOR = "\r\n|[\r\n]";

    /**
     * Deletes existing gitlet system, resets the folder that stores files used
     * in testing.
     * 
     * This method runs before every @Test method. This is important to enforce
     * that all tests are independent and do not interact with one another.
     */
    @Before
    public void setUp() {
        File f = new File(GITLET_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f = new File(TESTING_DIR);
        if (f.exists()) {
            recursiveDelete(f);
        }
        f.mkdirs();
    }

    /**
     * Tests that init creates a .gitlet directory. Does NOT test that init
     * creates an initial commit, which is the other functionality of init.
     */
    @Test
    public void testBasicInitialize() {
        gitlet("init");
        File f = new File(GITLET_DIR);
        assertTrue(f.exists());

        //extra text tests
        String frank = " ";
        frank = frank + "frank";
        boolean isFrank = frank.equals("frank");
        System.out.println(isFrank);
        String[] splitFrank = "frank".split("/");
        System.out.println(splitFrank.length);
        System.out.println(splitFrank[0]);
        createFile(":gits:gits:gots:", "does this message make it in?");

        //random map test
        HashMap<String, Integer> ronaldo = new HashMap<String, Integer>();
        ronaldo.put("two", 2);
        ronaldo.put("one", 1);
        ronaldo.put("four", 4);
        Iterator<String> ronaldoStrings = ronaldo.keySet().iterator();
        Iterator<Integer> ronaldoIntegers = ronaldo.values().iterator();
        while (ronaldoStrings.hasNext()){
            System.out.println(ronaldoStrings.next());
            System.out.println(ronaldoIntegers.next());
        }
        Commit[] donald = new Commit[5];
        if (donald[1] == null){
            System.out.println("norman");
        }
        String frogs = "frogs";
        String froags = "frogs";
        System.out.println(frogs.equals(froags));
    }
    /**
     * Tests that checking out a file name will restore the version of the file
     * from the previous commit. Involves init, add, commit, and checkout.
     */
    @Test
    public void testBasicCheckout() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        assertTrue(checkCurrentBranch(GITLET_DIR + "master.ser"));
        // assertTrue(checkCommit("added wug"));
        writeFile(wugFileName, "This is not a wug.");
        gitlet("checkout", wugFileName);
        assertEquals(wugText, getText(wugFileName));
    }

    @Test
    public void testBranchCheckout() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        assertTrue(checkCurrentBranch(GITLET_DIR + "master.ser"));
        // assertTrue(checkCommit("added wug"));
        writeFile(wugFileName, "This is not a wug.");
        gitlet("checkout", wugFileName);
        assertEquals(wugText, getText(wugFileName));
        gitlet("branch", "frank");
        createFile(TESTING_DIR + "funk.txt", "funk");
        gitlet("add", TESTING_DIR + "funk.txt" );
        gitlet("commit", "march");
        gitlet("checkout", "frank");
        String log = gitlet("checkout", TESTING_DIR + "funk.txt");
        // System.out.println(log);

        String errorMessage = "File does not exist in the most recent commit, or no such branch exists.";
        // System.out.println("File does not exist in the most recent commit, or no such branch exists.");


        log = gitlet("merge", "master");
        // System.out.println(log);

        log = gitlet("checkout" , TESTING_DIR + "funk.txt"); 
        // System.out.println(log);
        gitlet("branch", "rich");
        createFile(TESTING_DIR + "crump.txt", "crump");
        gitlet("add", TESTING_DIR + "crump.txt");
        gitlet("commit", "duh krump");
        gitlet("checkout", "rich");
        createFile(TESTING_DIR + "derek.txt", "derek");
        gitlet("add", TESTING_DIR + "derek.txt");
        gitlet("commit", "duh derek");

        recursiveDelete(new File(TESTING_DIR));
        theGitlet("merge", "frank");
        log = gitlet("checkout", TESTING_DIR + "derek.txt");
        System.out.println(log);
        assertEquals(getText(TESTING_DIR + "derek.txt"), "derek");
        assertEquals(getText(TESTING_DIR + "crump.txt"), "crump");



    }

    @Test
    public void testBasicRemove() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        gitlet("init");
        gitlet("add", wugFileName);
        gitlet("commit", "added wug");
        assertTrue(checkCurrentBranch(GITLET_DIR + "master.ser"));
        writeFile(wugFileName, "This is not a wug.");
        String removedLog = gitlet("rm", wugFileName);
        // System.out.println(removedLog);
        String storedLog = gitlet("commit", "removed File");
        // System.out.println(storedLog);
        Commit currentCommit = (Commit) tryLoadingObject(GITLET_DIR + "master.ser");
        assertTrue(!currentCommit.fileMap.containsKey(wugFileName));


    }

    @Test
    public void testBasicCheckID() {
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);
        String dugFileName = TESTING_DIR + "dug.txt";
        String dugText = "This is a dug.";
        createFile(dugFileName, dugText);
        String rugFileName = "rug.txt";
        String rugText = "This is a rug.";
        createFile(rugFileName, rugText);
        assertTrue(existenceTest(wugFileName));
        assertTrue(existenceTest(dugFileName));
        gitlet("init");
        gitlet("add", wugFileName);
        String log = gitlet("commit", "wug");
        // System.out.println(log);
        assertTrue(checkCurrentBranch(GITLET_DIR + "master.ser"));
        writeFile(wugFileName, "This is not a wug.");
        gitlet("rm", wugFileName);
        log = gitlet("commit", "wug");
        // System.out.println(log);
        log = gitlet("add", wugFileName);
        // System.out.println(log);
        gitlet("add", dugFileName);
        log = gitlet("commit", "dog");
        // System.out.println(log);
        String logContent = gitlet("find", "dog");
        // System.out.println(logContent);
        writeFile(dugFileName, "frank");
        // gitlet(add, )
    }



    /**
     * Tests that log prints out commit messages in the right order. Involves
     * init, add, commit, and log.
     */
    public boolean existenceTest(String fileName) {
        File testedFile = new File(fileName);
        if (testedFile.exists()){
            return true;
        }
        return false;
    }

    @Test
    public void testBasicLog() {
        gitlet("init");
        assertTrue(existenceTest(GITLET_DIR + "0.ser"));
        assertTrue(existenceTest(GITLET_DIR + "0"));
        assertTrue(existenceTest(GITLET_DIR + "currentBranch.ser"));
        assertTrue(existenceTest(GITLET_DIR + "master.ser"));
        assertTrue(existenceTest(GITLET_DIR + "currentID.ser"));
        String commitMessage1 = "initial commit";
        String wugFileName = TESTING_DIR + "wug.txt";
        String wugText = "This is a wug.";
        createFile(wugFileName, wugText);

        gitlet("add", wugFileName);
        assertTrue(existenceTest(GITLET_DIR + "stagedFiles.ser"));
        // HashSet<String> stagingFiles = (HashSet<String>) tryLoadingObject(GITLET_DIR + "stagedFiles.ser");
        // Iterator<String> stgFiles = stagingFiles.iterator();
        // while (stgFiles.hasNext()){
        //     System.out.println(stgFiles.next());
        // }

        String commitMessage2 = "added wug";
        String stringContent = gitlet("commit", commitMessage2);
        // System.out.println(stringContent);
        Commit commit1 = (Commit) tryLoadingObject(GITLET_DIR + "master.ser");
        assertEquals(commit1.id, 1);
        // assertEquals(commit1.previous.id, 0);
        theGitlet("log");
        String logContent = gitlet("log");
        // System.out.println(logContent);
        assertArrayEquals(new String[] { commitMessage2, commitMessage1 },
                extractCommitMessages(logContent));
    }

    /**
     * Convenience method for calling Gitlet's main. Anything that is printed
     * out during this call to main will NOT actually be printed out, but will
     * instead be returned as a string from this method.
     * 
     * Prepares a 'yes' answer on System.in so as to automatically pass through
     * dangerous commands.
     * 
     * The '...' syntax allows you to pass in an arbitrary number of String
     * arguments, which are packaged into a String[].
     */
    private static String gitlet(String... args) {
        PrintStream originalOut = System.out;
        InputStream originalIn = System.in;
        ByteArrayOutputStream printingResults = new ByteArrayOutputStream();
        try {
            /*
             * Below we change System.out, so that when you call
             * System.out.println(), it won't print to the screen, but will
             * instead be added to the printingResults object.
             */
            System.setOut(new PrintStream(printingResults));

            /*
             * Prepares the answer "yes" on System.In, to pretend as if a user
             * will type "yes". You won't be able to take user input during this
             * time.
             */
            String answer = "yes";
            InputStream is = new ByteArrayInputStream(answer.getBytes());
            System.setIn(is);

            /* Calls the main method using the input arguments. */
            Gitlet.main(args);

        } finally {
            /*
             * Restores System.out and System.in (So you can print normally and
             * take user input normally again).
             */
            System.setOut(originalOut);
            System.setIn(originalIn);
        }
        return printingResults.toString();
    }
    private static void theGitlet(String... args){
        Gitlet.main(args);
    }
    //loads whatever is in the serial
    public static Object tryLoadingObject(String serialName) {
    Object myObject = null;
    File myCommitFile = new File(serialName);

    if (myCommitFile.exists()) {
        try {
            FileInputStream fileIn = new FileInputStream(myCommitFile);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            myObject = objectIn.readObject();
        } catch (IOException e) {
            String msg = "IOException while loading Serializable.";
            System.out.println(msg);
        } catch (ClassNotFoundException e) {
            String msg = "ClassNotFoundException while loading Serializable.";
            System.out.println(msg);
        }
    }
    return myObject;
    }
    private static boolean checkStagedFiles(String fileName){
        HashSet<String> stagedFiles = (HashSet<String>) tryLoadingObject(GITLET_DIR + "stagedFiles.ser");

        if (stagedFiles != null) return stagedFiles.contains(fileName);
        return false;

    }
    private static boolean checkCurrentBranch(String branchName){
        String currentBranch = (String) tryLoadingObject(GITLET_DIR + "currentBranch.ser");

        if (currentBranch != null) return currentBranch.equals(branchName);
        return false;

    }

    // private static boolean checkCommit(String message){
    //     String currentBranch = (String) tryLoadingObject(GITLET_DIR + "currentBranch.ser");
    //     Commit checkedCommit = (Commit) tryLoadingObject(currentBranch);

    //     if (checkedCommit != null) return checkedCommit.msg.equals(message);
    //     return false;

    // }





    /**
     * Returns the text from a standard text file (won't work with special
     * characters).
     */
    private static String getText(String fileName) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(fileName));
            return new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Creates a new file with the given fileName and gives it the text
     * fileText.
     */
    private static void createFile(String fileName, String fileText) {
        File f = new File(fileName);
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile(fileName, fileText);
    }

    /**
     * Replaces all text in the existing file with the given text.
     */
    private static void writeFile(String fileName, String fileText) {
        FileWriter fw = null;
        try {
            File f = new File(fileName);
            fw = new FileWriter(f, false);
            fw.write(fileText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes the file and all files inside it, if it is a directory.
     */
    private static void recursiveDelete(File d) {
        if (d.isDirectory()) {
            for (File f : d.listFiles()) {
                recursiveDelete(f);
            }
        }
        d.delete();
    }

    /**
     * Returns an array of commit messages associated with what log has printed
     * out.
     */
    private static String[] extractCommitMessages(String logOutput) {
        String[] logChunks = logOutput.split("====");
        int numMessages = logChunks.length - 1;
        String[] messages = new String[numMessages];
        for (int i = 0; i < numMessages; i++) {
            System.out.println(logChunks[i + 1]);
            String[] logLines = logChunks[i + 1].split(LINE_SEPARATOR);
            messages[i] = logLines[3];
        }
        return messages;
    }

    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(GitletSmallTest.class);
    }


}
