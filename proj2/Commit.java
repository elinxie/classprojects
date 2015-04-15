import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.Serializable;
import java.util.HashMap;
import java.lang.System;
import java.util.HashSet;


public class Commit implements Serializable{
    public Commit previous;
    public int id;
    public long commitTime;
    public Date commitDate;
    public DateFormat commitDateFormatted = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public String msg; 
    public HashMap<String, String> fileMap;
    public static final long serialVersionUID = 42L;
    public String commitBranch;
    public HashSet<String> committedFiles;


    public Commit(int id, String msg, Commit previous, HashMap<String, String> fileMap, String commitBranch, HashSet<String> committedFiles ){
        commitTime = System.currentTimeMillis();
        commitDate = new Date();
        this.id = id;
        this.msg = msg;
        this.previous = previous;
        this.fileMap = fileMap;
        this.commitBranch = commitBranch;
        this.committedFiles = committedFiles;
    }
    public Commit previous(){
        return this.previous;
    }
    public int id(){
        return id;
    }
    public long commitTime(){
        return commitTime;
    }
    public String msg(){
        return msg;
    }

    public boolean equals(Commit otherCommit){
        return this.id == otherCommit.id;
    }


}