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
import java.util.Iterator;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

//for copying files and using the terminal
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption; 

//misc.
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;                 


public class Gitlet{
    private static final String GITLET_DIR = ".gitlet/";
    private static final String METADATA = ".gitlet/metadata/";
    private static final String STAGEDFILES = GITLET_DIR + "stagedFiles.ser";
    private static final String REMOVEDFILES = GITLET_DIR + "removedFiles.ser";
    private static final String BRANCHSET = GITLET_DIR + "branchSet.ser";
    private static final String CURRENTID = GITLET_DIR + "currentID.ser";
    private static final String CHECKID = GITLET_DIR + "checkID.ser";
    private static final String CURRENTBRANCH = GITLET_DIR + "currentBranch.ser";
    private static String head; 
    private static HashSet<String> stagedFiles;
    private static HashSet<String> removedFiles;
    private static HashSet<String> branchSet;
    private static HashSet<String> committedFiles;
    private static HashMap<String, HashSet<Integer>> checkID;
    private static int currentIDnumber;
    private static Commit currentCommit;
    private static HashMap<String, String> currentFileMap;
    private static DateFormat dF;
    private static String currentBranchName;

    //for merge
    private static Commit mCommit;
    private static Commit splitCommit;
    private static HashMap<String, String> splitCommitMap;
    private static Stack<String> mergeKeyStack;
    private static Stack<String> mergeValueStack;
    private static HashSet<String> mergeAddedSet;
    private static Iterator<String> mergeIterator;
    private static String mergeString;
    private static String[] mergeStringSplit;


    //rebase fields
    private static String rebaseBranch;
    private static Commit rebaseCommit;
    private static Stack<Commit> rebaseStack;
    private static Commit replayedCommit;
    private static Commit replayedCommitPrevious;
    private static String input;
    private static Commit headCommit;

    //for findSplitCommit()
    private static String[] currentBranchHistory;
    private static Commit historyCommit;
    private static int historyCommitID;
    private static Commit historyMergeCommit;



    ///////////////////////////////////////////
    ///////Methods to do with Serializables////
    ///////////////////////////////////////////

    
    //serialize objects
    public static void toSerialize(String path, Object input){
    	try{

            //making new directories in case you try to serialize something like .gitlet/frank/bob.ser or
            //.gitlet/frank/bob/joe. Assumes .gitlet/ is always in front.
            String[] splitPath = path.split("/");
                        if (splitPath.length > 2){
                String toFolder = splitPath[1];
                createFolder(toFolder, true);
                if (splitPath.length > 3){
                    for (int i = 2; i < splitPath.length - 1; i += 1){
                        toFolder = toFolder + "/" + splitPath[i];
                        createFolder(toFolder, true);
                    }
                }
            }

            //actual serializing
            
            File newFile = new File(path);
            FileOutputStream fileOut = new FileOutputStream(newFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(input);
            objectOut.close();
        }
        catch (IOException e) {
            

            String msg = "IOException while saving Serial.";
            System.out.println(msg);
        }
    }

    //loading objects from .ser
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

    public static Commit tryLoadingCommit(String serialName){
        return (Commit) tryLoadingObject(serialName);
    }
    public static String tryLoadingString(String serialName){
        return (String) tryLoadingObject(serialName);
    }

    public static HashSet<String> tryLoadingHashSet(String serialName){
        return (HashSet<String>) tryLoadingObject(serialName);
    }

    public static Integer tryLoadingInteger(String serialName){
        return (Integer) tryLoadingObject(serialName);
    }

    public static HashMap<String, HashSet<Integer>> tryLoadingIDMap(String serialName){
        return (HashMap<String, HashSet<Integer>>) tryLoadingObject(serialName);
    }
    public static HashMap<String, String> tryLoadingMap(String serialName){
        return (HashMap<String, String>) tryLoadingObject(serialName);
    }


    //////////////////////////////////////////////////////
    ///////Methods to do with Terminal copy and remove////
    //////////////////////////////////////////////////////

    public static void executeTerminalCopy(String file, String folder) throws IOException{
    	// build my command as a list of strings
    	String[] splitFile = file.split("/");
        String jTFile = splitFile[splitFile.length - 1];
        String destinationPath = folder + "/" + jTFile;
        File destination =  new File(destinationPath);
        Boolean created = destination.createNewFile();
        if (created){
            executeCopy(file, destinationPath);
            }
        }
    public static void executeCopy(String file, String target) throws IOException{
        File targetFile = new File(target);
        System.out.println(target);
        System.out.println(targetFile.exists());
        //makes new target
        if (!targetFile.exists()){
            //makes folders target needs
            String[] splitPath = target.split("/");
            if (splitPath[0].equals(GITLET_DIR)){
                if (splitPath.length > 2){
                    String toFolder = splitPath[1];
                    System.out.println(toFolder);
                    createFolder(toFolder, true);
                    if (splitPath.length > 3){
                        for (int i = 2; i < splitPath.length - 1; i += 1){
                            toFolder = toFolder + "/" + splitPath[i];
                            createFolder(toFolder, true);
                        }
                    }
                }
            }
            if (splitPath.length > 1 && splitPath[0].equals(GITLET_DIR) == false){
                    String toFolder = splitPath[0];
                    System.out.println(toFolder);
                    createFolder(toFolder, false);
                    if (splitPath.length > 2){
                        for (int i = 2; i < splitPath.length - 1; i += 1){
                            toFolder = toFolder + "/" + splitPath[i];
                            createFolder(toFolder, false);
                        }
                    }
                }

            System.out.println("archie");
            targetFile.createNewFile();
            System.out.println(targetFile.exists());

        }
        Path FROM = Paths.get(file);
        Path TO = Paths.get(target);
        //overwrite existing file, if exists
        CopyOption[] options = new CopyOption[]{
          StandardCopyOption.REPLACE_EXISTING,
          StandardCopyOption.COPY_ATTRIBUTES
        }; 
        Files.copy(FROM, TO, options);
            
        }
    //brings files in fileMap of commit to 
    public static void mapToSurface(Commit usedCommit){
        HashMap<String, String> usedMap = usedCommit.fileMap;
        Iterator<String> usedIterator = usedMap.keySet().iterator();
        Iterator<String> usedIterator2 = usedMap.values().iterator();
        System.out.println("hello");
        while (usedIterator.hasNext()){
            String usedFile = usedIterator.next();
            String usedSource = usedIterator2.next();
            try{
                System.out.println("cramps");
                executeCopy(usedSource, usedFile);
            }
            catch (IOException e) {
                System.out.println("marge");
                String msg = "IOException while saving Serial.";
                System.out.println(msg);
            }
        }
    }

    public static String createFolder(String folderName, Boolean inGitletDIR){
        //makes folder inside of the gitlet director. You do not need the /
        File newFolder = new File(GITLET_DIR + folderName + "/");
        if (!inGitletDIR){
            newFolder = new File(folderName + "/");
        }
        if (newFolder.exists()){
            int a = 0;           //placeholder function, does nothing
        }
        else{
            newFolder.mkdirs();
        }
        return GITLET_DIR + folderName + "/";
    }

    //from GitletPublicTest, to delete branch head pointers and the folders they make 
    private static void recursiveDelete(File d) {
        if (d.isDirectory()) {
            for (File f : d.listFiles()) {
                recursiveDelete(f);
            }
        }
        d.delete();
    }


    //////////////////////////////////////////////////////
    ///////Methods to do with Staging and removing////////
    //////////////////////////////////////////////////////


    public static void stageTheFile(String fileName){
    	stagedFiles.add(fileName);
    	if (removedFiles.contains(fileName)){
            removedFiles.remove(fileName);
            toSerialize(REMOVEDFILES, removedFiles);
    	}
        toSerialize(STAGEDFILES, stagedFiles);
    }

    public static void removeTheFile(String fileName){
    	removedFiles.add(fileName);
    	if (stagedFiles.contains(fileName)){
            stagedFiles.remove(fileName);
            toSerialize(STAGEDFILES, stagedFiles);
    	}
        toSerialize(REMOVEDFILES, removedFiles);
    }

    public static Commit findSplitCommit(Commit otherBranchCommit){
            currentIDnumber = tryLoadingInteger(CURRENTID);
            currentBranchHistory = new String[currentIDnumber];
            historyCommit = currentCommit;
            while(historyCommit != null){
                historyCommitID = historyCommit.id;
                currentBranchHistory[historyCommitID] = "placeholder";
                historyCommit = historyCommit.previous;
            }
            historyMergeCommit = otherBranchCommit;
            while(currentBranchHistory[historyMergeCommit.id] == null){
                historyMergeCommit = historyMergeCommit.previous;
            }
            return historyMergeCommit;

    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    ///////Merge method (to use in merge and rebase and i-rebase/////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    public static void merge(String token, Boolean splitCommitInitiallyFound){
        String mergeBranch = token;
        if (mergeBranch.equals(currentBranchName)){
            System.out.println("Cannot merge a branch with itself.");
            return;
        }

        Commit mergeCommit = tryLoadingCommit(GITLET_DIR + mergeBranch + ".ser");
        if (mergeCommit == null){
            System.out.println("A branch with that name does not exist.");
            return;
        }

        //in case someone messes with deleting files manually
        // mapToSurface(currentCommit);

        // HashMap<String, String> mergeCommitMap = mergeCommit.fileMap;

        splitCommit = tryLoadingCommit(GITLET_DIR + ":" + mergeBranch + ":" + currentBranchName + ":" + "SplitPoint.ser");
        if (splitCommit == null && splitCommitInitiallyFound == false){
            splitCommit = findSplitCommit(mergeCommit);

            // System.out.println("no commit where branches split off");
            // return;
        }
        splitCommitMap = splitCommit.fileMap;
        mCommit = mergeCommit;
        mergeKeyStack = new Stack<String>();
        mergeValueStack = new Stack<String>();
        
        while (mCommit.commitBranch.equals(mergeBranch) && mCommit.equals(splitCommit) == false ){
            mergeAddedSet = mCommit.committedFiles;
            mergeIterator = mergeAddedSet.iterator();
            while(mergeIterator.hasNext()){
                mergeString = mergeIterator.next();
                String[] mergeStringSplit = mergeString.split("/");
                mergeKeyStack.push(mergeString);
                mergeValueStack.push(GITLET_DIR + mCommit.id + "/" + mergeStringSplit[mergeStringSplit.length - 1]);
            }
            mCommit = mCommit.previous;

        }



        // Iterator<String> mergeCommitKeys = mergeCommitMap.keySet().iterator();
        // Iterator<String> mergeCommitValues = mergeCommitMap.values().iterator();
        while (mergeKeyStack.empty() == false && mergeValueStack.empty() == false){
            String iteratedKey = mergeKeyStack.pop();
            String iteratedValue = mergeValueStack.pop();

            if (splitCommit != null){
                if (splitCommitMap.containsKey(iteratedKey) && currentFileMap.containsKey(iteratedKey)){
                    if(splitCommitMap.get(iteratedKey) != currentFileMap.get(iteratedKey)){
                        // System.out.println("gramps");
                        try{
                            System.out.println(iteratedKey.split("\\.")[0]);
                            executeCopy(iteratedValue, iteratedKey.split("\\.")[0] + ".conflicted");
                        }
                        catch(IOException e){
                            System.out.print(e.getMessage());
                        }
                    }
                    else {
                        currentFileMap.put(iteratedKey, iteratedValue);
                        // try{
                        //     executeCopy(iteratedValue, iteratedKey);
                        // }
                        // catch(IOException e){
                        // System.out.print(e.getMessage());
                        // }

                    }
                }

                //deals with user if he has removed the file
                if (splitCommitMap.containsKey(iteratedKey) && currentFileMap.containsKey(iteratedKey) == false){
                    try{
                        executeCopy(iteratedValue, iteratedKey + ".conflicted");
                    }
                    catch(IOException e){
                        System.out.print(e.getMessage());
                    }
                }

                //deals if user put in same file after split point
                if (splitCommitMap.containsKey(iteratedKey) == false && currentFileMap.containsKey(iteratedKey)){
                    try{
                        executeCopy(iteratedValue, iteratedKey + ".conflicted");
                    }
                    catch(IOException e){
                        System.out.print(e.getMessage());
                    }
                }


                else {
                    currentFileMap.put(iteratedKey, iteratedValue);
                    // try{
                    //     executeCopy(iteratedValue, iteratedKey);
                    // }
                    // catch(IOException e){
                    // System.out.print(e.getMessage());
                    // }

                }
            }
        }
            // else {currentFileMap.put(iteratedKey, iteratedValue);
            //     try{
            //         executeCopy(iteratedValue, iteratedKey);
            //     }
            //     catch(IOException e){
            //     System.out.print(e.getMessage());
            //     }
            // // System.out.println("gumpert");  
            //     }
            
        currentCommit.fileMap = currentFileMap;
        toSerialize(head, currentCommit);
        System.out.println(head);
        // mapToSurface(currentCommit);
        return;
    }

    public static Boolean warningMessage(){
        System.out.println("Warning: The command you entered may alter the files in your working directory. Uncommitted changes may be lost. Are you sure you want to continue? (yes/no)");
        input = System.console().readLine();

        while (input.equals("yes") == false && input.equals("no") == false ){
            System.out.println("please type in (yes) or (no) ");
            input = System.console().readLine();
        }
        if (input.equals("yes")){
            return true;
        }
        return false;
    }
    public static void refreshStagedandRemoved(){
        toSerialize(STAGEDFILES, new HashSet<String>());             //empties stagedFiles and removedFiles
        toSerialize(REMOVEDFILES, new HashSet<String>());
    }

    public static void addToCheckID(String msg){
        if (!checkID.containsKey(msg)){
            checkID.put(msg, new HashSet<Integer>());
        }
        checkID.get(msg).add(currentIDnumber);
        toSerialize(CHECKID, checkID);
    }



	public static void main(String[] args){
		String command = args[0];
		String[] tokens = new String[args.length - 1];
		System.arraycopy(args, 1, tokens, 0, args.length - 1);

		//only if gitlet folder exists
		File gitletFolder = new File(GITLET_DIR);
        if (gitletFolder.exists()){
        	File currentBranch = new File(GITLET_DIR + "currentBranch.ser");
        	if (currentBranch.exists()){
        	    head = tryLoadingString(GITLET_DIR + "currentBranch.ser");
        	}
        	currentCommit = tryLoadingCommit(head);             //maybe make this capital....do something diff than getting head
            currentFileMap = currentCommit.fileMap;
        	stagedFiles = tryLoadingHashSet(STAGEDFILES);
        	if (stagedFiles == null){
        		stagedFiles = new HashSet<String>();
        	}
        	removedFiles = tryLoadingHashSet(REMOVEDFILES);
        	if (removedFiles == null){
        		removedFiles = new HashSet<String>();
        	}
            branchSet = tryLoadingHashSet(BRANCHSET);
            if (branchSet == null){
                branchSet = new HashSet<String>();
            }
        	checkID = tryLoadingIDMap(CHECKID);
        	if (checkID == null){
        		System.out.println("fun");
        		checkID = new HashMap<String, HashSet<Integer>>();
        	}
            //get name from head
            String[] splitHead = head.split("/");

            //to deal with branches such as hello/frank
            String addedBranchText = " ";
            if (splitHead.length > 2){
                addedBranchText = splitHead[1];
                    if (splitHead.length > 3){
                    for (int i = 2; i < splitHead.length - 1; i += 1){
                        addedBranchText = addedBranchText + "/" + splitHead[i];
                    }
                }
            }
            String splitPart1 = splitHead[splitHead.length - 1];
            String[] splitHead2 = splitPart1.split("\\."); // "\\." is just "." but for java String.split
            currentBranchName = splitHead2[0];

            //to also deal with branches such as hello/frank
            if(!addedBranchText.equals(" ")){
                currentBranchName = addedBranchText + "/" + currentBranchName;
            }

        }
        else{
        	checkID = new HashMap<String, HashSet<Integer>>();
            branchSet = new HashSet<String>();
        }


   
    try{
    switch (command) {
    	case "init":
            if (gitletFolder.exists()) {
                System.out.println("A gitlet version control system already exists in the current directory");
            }
            else{
            	gitletFolder.mkdirs();
            	HashMap<String, String> fileMap = new HashMap<String, String>();
            	Commit initialCommit = new Commit(000000, "initial commit", null, fileMap, "master", new HashSet<String>());
            	toSerialize(GITLET_DIR + "master.ser", initialCommit);
            	toSerialize(GITLET_DIR + Integer.toString(initialCommit.id) +".ser", initialCommit);
            	toSerialize(GITLET_DIR + "currentBranch.ser", GITLET_DIR + "master.ser");

                //commit folder
            	createFolder(Integer.toString(initialCommit.id), true);

                //puts master branch into the branchSet
                branchSet.add("master");    // master is what is printed out
                toSerialize(BRANCHSET, branchSet);

                //makes new IDgenerator
            	toSerialize(GITLET_DIR + "currentID.ser", 1);
            	if (!checkID.containsKey("initial commit")){
                	checkID.put("initial commit", new HashSet<Integer>());
                }
                checkID.get("initial commit").add(000000);
                toSerialize(CHECKID, checkID);
            }
            break;
        case "add":
            if (tokens.length > 0){
                String fileName = tokens[0];
                File f = new File(fileName);
                if (!f.exists()){
                	System.out.println("File does not exist");
                }
                long lastModifiedTime = f.lastModified();
                if (currentFileMap.containsKey(fileName)){
                    File commitFile = new File(currentFileMap.get(fileName));
                    long commitFileTime = commitFile.lastModified();
                    if (commitFileTime >= lastModifiedTime){
                    	System.out.println("File has not been modified");
                    }
                    else{
                    	stageTheFile(fileName);
                    }
                }


                else{
                    stageTheFile(fileName);
                }
            }
            break;

        case "rm":
            if (tokens.length > 0){
                String fileName = tokens[0];
                File f = new File(fileName);
                if (!f.exists()){
                	System.out.println("File does not exist");
                    break;
                }
                if(!currentFileMap.containsKey(fileName) && !stagedFiles.contains(fileName)){
                    System.out.println("No reason to remove the file");
                    break;
                }
                else{
                    removeTheFile(fileName); 
                }              
            }
            break;


        case "commit":
            if (tokens.length < 1){
            	System.out.println("Please enter a commit message.");
            }
            if (stagedFiles.size() < 1 && removedFiles.size() < 1){
            	System.out.println("No changes added to the commit.");
            }
            else{
            	String msg = tokens[0];
            	currentIDnumber = tryLoadingInteger(CURRENTID);
            	toSerialize(CURRENTID, currentIDnumber + 1);    //puts in next id number for next commit
                String newCommitPath = GITLET_DIR + currentIDnumber;
                createFolder(Integer.toString(currentIDnumber), true);
                committedFiles = stagedFiles;
                Iterator<String> stagedIterator = stagedFiles.iterator();
                while (stagedIterator.hasNext()){    
                    String addedFile = stagedIterator.next();
                    String[] splitFile = addedFile.split("/");
                    String justTheFile = splitFile[splitFile.length - 1];                          //this adds values in map and also copies files into folder
                    currentFileMap.put(addedFile, newCommitPath + "/" + justTheFile);     //adding file to map
                    executeTerminalCopy(addedFile, newCommitPath);
                }
                if (removedFiles.size() > 0){
                    Iterator<String> removedIterator = removedFiles.iterator();
                    while (removedIterator.hasNext()){    
                        String removedFile = removedIterator.next();          
                        currentFileMap.remove(removedFile);     //removing file from map
                    }
                }

                Commit newCommit = new Commit(currentIDnumber, msg, currentCommit, currentFileMap, currentBranchName, committedFiles );
                toSerialize(newCommitPath + ".ser" , newCommit );
                toSerialize(head, newCommit);                                 //moves head
                if (!checkID.containsKey(msg)){
                	checkID.put(msg, new HashSet<Integer>());
                }
                checkID.get(msg).add(currentIDnumber);
                toSerialize(CHECKID, checkID);
                toSerialize(STAGEDFILES, new HashSet<String>());             //empties stagedFiles and removedFiles
                toSerialize(REMOVEDFILES, new HashSet<String>());
                
            }
            break;


            case "log":
            	Commit recursedCommit = currentCommit;
                dF = currentCommit.commitDateFormatted;
            	while (recursedCommit != null){
            		System.out.println("====");
            		System.out.println("Commit " + recursedCommit.id);
            		System.out.println(dF.format(recursedCommit.commitDate));
            		System.out.println(recursedCommit.msg);
            		System.out.println(" ");
            		recursedCommit = recursedCommit.previous;
            	}
            	break;

            case "find":
                if (tokens.length < 1){
                    System.out.println("Please enter a commit message.");
                    break;
                }
                String msg = tokens[0];
                if (!checkID.containsKey(msg)){
                	System.out.println("Found no Commit with that message");
                }
                else{
                	HashSet<Integer> matchingIDs = checkID.get(msg);
                	Iterator<Integer> matchingIDsIterator = matchingIDs.iterator();
                	while (matchingIDsIterator.hasNext()){
                		System.out.println(Integer.toString(matchingIDsIterator.next()));
                	}
                }
                break;

            case "checkout":
                // if (!warningMessage()){
                //     break;
                // }
                if (tokens.length < 1){
                    System.out.println("Please enter an id, file name or a branch .");
                    break;
                }
                String item1 = tokens[0];
                String checkoutFile = item1;
                String item1BranchString = GITLET_DIR + item1 + ".ser";
                String errorMessage = "File does not exist in the most recent commit, or no such branch exists.";
                File item1Branch = new File(item1BranchString);
                Commit grabbedCommit;
                if (item1Branch.exists()){
                    if (item1BranchString.equals(head)){
                        System.out.println("No need to checkout the current Branch");
                        break;
                    }
                	toSerialize(GITLET_DIR + "currentBranch.ser", GITLET_DIR + item1 + ".ser");
                    grabbedCommit = tryLoadingCommit(GITLET_DIR + item1 + ".ser");
                    if (grabbedCommit != null){
                        mapToSurface(grabbedCommit);
                    }
                    break;
                }
                if (tokens.length > 1){
                	String item2 = tokens[1];
                	checkoutFile = item2;
                	grabbedCommit = tryLoadingCommit(GITLET_DIR + item1 + ".ser");
                    if (grabbedCommit == null){
                        System.out.println("No commit with that id exists");
                        break;
                    }
                    errorMessage = "File does not exist in that commit.";
                }
                else{
                	grabbedCommit = currentCommit;
                }
                
                HashMap<String, String> grabbedCommitMap = grabbedCommit.fileMap;
                if (grabbedCommitMap.containsKey(checkoutFile)){
                	String grabbedCommitFile = grabbedCommitMap.get(checkoutFile);
                    executeCopy(grabbedCommitFile, checkoutFile);
                }
                else {
                    System.out.println(errorMessage);
                }
                break;

                case "branch":
                if (tokens.length < 1){
                    System.out.println("Please enter a branch .");
                    break;
                }
                String branchName = tokens[0];
                if (branchSet.contains(branchName)){
                    System.out.println("A branch with that name already exists");
                    break;
                }
                branchSet.add(branchName);
                toSerialize(BRANCHSET, branchSet);
                toSerialize(GITLET_DIR + branchName + ".ser", currentCommit);
                toSerialize(GITLET_DIR + ":" + branchName + ":" + currentBranchName + ":" + "SplitPoint.ser", currentCommit);
                toSerialize(GITLET_DIR + ":" + currentBranchName + ":" + branchName + ":" + "SplitPoint.ser", currentCommit);
                refreshStagedandRemoved();
                break;     

            case "status":

                System.out.println("=== Branches ===");
                Iterator<String> branchNameIterator = branchSet.iterator();
                while (branchNameIterator.hasNext()){
                    String iteratedBranchName = branchNameIterator.next();
                    if (iteratedBranchName.equals(currentBranchName)){
                        iteratedBranchName = "*" + currentBranchName;
                    }
                    System.out.println(iteratedBranchName);
                }
                System.out.println(" ");
                System.out.println("=== Staged Files ===");
                Iterator<String> stagedFilesIterator = stagedFiles.iterator();
                while (stagedFilesIterator.hasNext()){
                    String iteratedStagedFile = stagedFilesIterator.next();
                    System.out.println(iteratedStagedFile);
                }
                System.out.println(" ");
                System.out.println("=== Files Marked for Removal ===");
                Iterator<String> removedFilesIterator = removedFiles.iterator(); 
                while (removedFilesIterator.hasNext()){
                    String iteratedRemovedFile = removedFilesIterator.next();
                    System.out.println(iteratedRemovedFile);
                }
                System.out.println(" ");
                break;

            case "global-log": 
                dF = currentCommit.commitDateFormatted;
                int globalNumber = tryLoadingInteger(CURRENTID) - 1;
                Commit globalCommit;
                while (globalNumber >= 0){
                    globalCommit = tryLoadingCommit(GITLET_DIR + globalNumber + ".ser" );
                    System.out.println("====");
                    System.out.println(globalNumber);
                    System.out.println(dF.format(globalCommit.commitDate));
                    System.out.println(globalCommit.msg);
                    System.out.println(" ");
                    globalNumber -= 1;
                }
                break;

            case "rm-branch":
                if (tokens.length < 1){
                    System.out.println("Please enter  a branch .");
                    break;
                }
                String removedBranch = tokens[0];
                if(!branchSet.contains(removedBranch)){
                    System.out.println( "A branch with that name does not exist.");
                    break;
                }
                String removedBranchString = GITLET_DIR + removedBranch + ".ser";
                if(removedBranchString.equals(head)){
                    System.out.println("Cannot remove the current branch.");
                    break;
                }
                
                recursiveDelete(new File(removedBranchString));               
                branchSet.remove(removedBranch);
                toSerialize(BRANCHSET, branchSet);
                break;

            case "reset":
                // if (!warningMessage()){
                //     break;
                // }
                if (tokens.length < 1){
                    System.out.println("Please enter a commit id.");
                    break;
                }
                String commitID = tokens[0];
                Commit resetedCommit = tryLoadingCommit(GITLET_DIR + commitID + ".ser");
                if (resetedCommit != null){
                    toSerialize(head, resetedCommit);
                    mapToSurface(resetedCommit);
                }
                else{
                    System.out.println("No commit with that id exists");
                }
                refreshStagedandRemoved();
                break;

            case "merge": 

                // if (!warningMessage()){
                //     break;
                // }
                if (tokens.length < 1){
                    System.out.println("Please enter a branch.");
                    break;
                }
                merge(tokens[0], false);
                refreshStagedandRemoved();
                break;

            case "rebase":
                // if (!warningMessage()){
                //     break;
                // }
                if (tokens.length < 1){
                    System.out.println("Please enter a branch.");
                    break;
                }
                rebaseBranch = tokens[0];
                if (rebaseBranch. equals(currentBranchName)){
                    System.out.println("Cannot rebase a branch with itself.");
                    break;
                }
                rebaseCommit = tryLoadingCommit(GITLET_DIR + rebaseBranch + ".ser");
                if (rebaseCommit == null){
                    System.out.println("A branch with that name does not exist.");
                    break;
                }

                rebaseStack = new Stack<Commit>();
                splitCommit = tryLoadingCommit(GITLET_DIR + ":" + rebaseBranch + ":" + currentBranchName + ":" + "SplitPoint.ser");
                if (splitCommit == null){
                    splitCommit = findSplitCommit(rebaseCommit);
                }
                merge(rebaseBranch, true);  
                headCommit = tryLoadingCommit(head);
                mapToSurface(headCommit);
                while (headCommit.commitBranch.equals(currentBranchName) &&  headCommit.equals(splitCommit) == false){
                    rebaseStack.push(headCommit);
                    headCommit = headCommit.previous;                    
                }
                if (rebaseStack.empty()){
                    System.out.println("Already up-to-date.");
                    break;
                }
                
                replayedCommitPrevious = rebaseCommit;
                toSerialize(GITLET_DIR + ":" + rebaseBranch + ":" + currentBranchName + ":" + "SplitPoint.ser", rebaseCommit);
                toSerialize(GITLET_DIR + ":" + currentBranchName + ":" + rebaseBranch + ":" + "SplitPoint.ser", rebaseCommit);
                while (!rebaseStack.empty()){
                    currentIDnumber = tryLoadingInteger(CURRENTID);
                    toSerialize(CURRENTID, currentIDnumber + 1);
                    replayedCommit = rebaseStack.pop();
                    replayedCommit.id = currentIDnumber;
                    replayedCommit.previous = replayedCommitPrevious;
                    replayedCommitPrevious = replayedCommit;
                    toSerialize(GITLET_DIR + currentIDnumber + ".ser", replayedCommit);
                    addToCheckID(replayedCommit.msg);
                }
                toSerialize(head, replayedCommit);
                refreshStagedandRemoved();
                break;

            case "i-rebase":
                // if (!warningMessage()){
                //     break;
                // }
                if (tokens.length < 1){
                    System.out.println("Please enter a branch.");
                    break;
                }
                rebaseBranch = tokens[0];
                if (rebaseBranch.equals(currentBranchName)){
                    System.out.println("Cannot rebase a branch with itself.");
                    break;
                }
                rebaseCommit = tryLoadingCommit(GITLET_DIR + rebaseBranch + ".ser");
                if (rebaseCommit == null){
                    System.out.println("A branch with that name does not exist.");
                    break;
                }
                rebaseStack = new Stack<Commit>();
                splitCommit = tryLoadingCommit(GITLET_DIR + ":" + rebaseBranch + ":" + currentBranchName + ":" + "SplitPoint.ser");
                if (splitCommit == null){
                    splitCommit = findSplitCommit(rebaseCommit);
                }
                merge(rebaseBranch, true);

                headCommit = tryLoadingCommit(head);
                mapToSurface(headCommit);
                while (headCommit.commitBranch.equals(currentBranchName) &&  headCommit.equals(splitCommit) == false){
                    rebaseStack.push(headCommit);
                    rebaseCommit = headCommit.previous;                    
                }
                if (rebaseStack.empty()){
                    System.out.println("Already up-to-date.");
                    break;
                }
                
                replayedCommitPrevious = rebaseCommit;
                toSerialize(GITLET_DIR + ":" + rebaseBranch + ":" + currentBranchName + ":" + "SplitPoint.ser", rebaseCommit);
                toSerialize(GITLET_DIR + ":" + currentBranchName + ":" + rebaseBranch + ":" + "SplitPoint.ser", rebaseCommit);
                while (!rebaseStack.empty()){
                    replayedCommit = rebaseStack.pop();
                    
                    //getting input from terminal
                    System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
                    input = System.console().readLine();

                    while (input.equals("c") == false && input.equals("s") == false && input.equals("m") == false){
                        System.out.println("please type in (c) to continue, (s) to skip commit, or (m) to change commit message");
                        input = System.console().readLine();
                    }

                    if (input.equals("c") || input.equals("m")){
                        currentIDnumber = tryLoadingInteger(CURRENTID);
                        toSerialize(CURRENTID, currentIDnumber + 1);
                        replayedCommit.id = currentIDnumber;
                        replayedCommit.previous = replayedCommitPrevious;
                        replayedCommitPrevious = replayedCommit;
                        if (input.equals("m")){
                            System.out.print("Please enter a new message for this commit.");
                            String msgInput = System.console().readLine();
                            replayedCommit.msg = msgInput;
                            addToCheckID(msgInput);
                        }
                        else{
                            addToCheckID(replayedCommit.msg);
                        }
                        toSerialize(GITLET_DIR + currentIDnumber + ".ser", replayedCommit);
                    }
                    if (input.equals("s")){
                        //does nothing
                        int doesNothing = 5;                 
                    }
                }
                toSerialize(head, replayedCommit);
                refreshStagedandRemoved();
                break;


        //extra tests
        // case "checkStaged":                                               
        //     Iterator<String> stgIterator = stagedFiles.iterator();
        //     while (stgIterator.hasNext()){
        //         System.out.println(stgIterator.next());
        //     }
        //     break;

        // case "checkCommitMsg":
        //     String currentBranch = (String) tryLoadingObject(GITLET_DIR + "currentBranch.ser");
        //     Commit checkedCommit = (Commit) tryLoadingObject(currentBranch);

        //     if (checkedCommit != null) System.out.println(checkedCommit.msg);
        //     break;

        // case "Copy":
        //     System.out.println("hi");
        //     if (tokens.length > 1){
        //     	createFolder("herp");
        //     	executeTerminalCopy(tokens[0], tokens[1]);
        //         System.out.println("plzprint");
        //         File recentCopy = new File(tokens[1] + "/" + tokens[0]);
        //         if (recentCopy.exists()){
        //         	System.out.println("true");
        //         }
        //         else{
        //         	System.out.println("false");
        //         }
        //     }
        //     else{
        //     	createFolder("derp");
        //     	System.out.println("what is happening");
        //     }
        //     break;
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