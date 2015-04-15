import java.io.File;
import java.nio.file.attribute.FileTime;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.HashMap;

public class SerialTest{
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
    public static void main(String[] args){
    	if (args.length > 0){
        	Object testingobject = tryLoadingObject(args[0]);
        	if (testingobject instanceof Commit){
        		Commit testingCommit = (Commit) testingobject;
        		System.out.println(testingCommit.msg());
        	}
        	if (testingobject instanceof String){
        		System.out.println(testingobject);
        	}
        }
    }
}