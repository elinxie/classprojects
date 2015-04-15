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

public class FileMaker{

    private static final String LINE_SEPARATOR = "\r\n|[\r\n]";
    /**
     * Returns the text from a standard text file (won't work with special
     * characters).
     */
    public static String getText(String fileName) {
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
    public static void createFile(String fileName, String fileText) {
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
    public static void writeFile(String fileName, String fileText) {
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
    public static void recursiveDelete(File d) {
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
    public static String[] extractCommitMessages(String logOutput) {
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
        String command = args[0];
        String[] tokens = new String[args.length - 1];
        System.arraycopy(args, 1, tokens, 0, args.length - 1);
        try{
            switch(command){
                case "getText":
                    if (tokens.length > 0){
                        getText(tokens[0]);
                    }
                    break;
                
                case "createFile":
                    if (tokens.length > 1){
                        createFile(tokens[0], tokens[1]);
                    }
                    break;
                
                case "writeFile":
                    if (tokens.length > 1){
                        writeFile(tokens[0], tokens[1]);
                    }
                    break;
                
                case "recursiveDelete":
                    if (tokens.length > 0){
                        File toDelete = new File(tokens[0]);
                        recursiveDelete(toDelete);
                    }
                    break;
                
                case "extractCommitMessages":
                    if (tokens.length > 0){
                        extractCommitMessages(tokens[0]);
                    }
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