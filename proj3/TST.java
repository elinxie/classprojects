

public class TST{
    private TSTNode root;

    public TST(String beginningWord, double val){
        String beginningLetter = beginningWord.split("")[0];
        root = new TSTNode(beginningLetter, 0, val);
        this.insert(beginningWord, val);
    }

    public TSTNode find(String word){
    	String[] splitWord = word.split("");
    	// System.out.println(word + "herps");
        if (word.equals("") || word == null){
            // System.out.println(root.letter);
            return root;
        }
    	return recursiveFind(root, splitWord, 0 );
    }
    public TSTNode recursiveFind(TSTNode node, String[] letterArray, int index){
    	if (index >= letterArray.length){
    		return null;
    	}
    	if (node == null){
    		return null;
    	}
    	//check if the middle equals the letter the index points to in letterArray
    	String theLetter = letterArray[index];
    	// System.out.println("searched letter: " + theLetter);
    	// System.out.println("node letter: " + node.letter);
    	// System.out.println(index);
    	if (theLetter.equals(node.letter)){

            //if this is the last letter
            
    		if (index == letterArray.length - 1){
    			// System.out.println("true");
    			return node;
    		}
    		return recursiveFind(node.mid, letterArray, index + 1);
    		
    	}
    	// if (node.left != null){
    		// if (node.left.letter.equals(theLetter)){
    	    	// }

    	//if none of the nodes equals the letter
        // else{
        // 	return null;
        // }
    	// }
    	// if (node.right != null){
    		// if (node.right.letter.equals(theLetter)){
    	else{
    		TSTNode theLeft = recursiveFind(node.left, letterArray, index);
    		

    		TSTNode theRight = recursiveFind(node.right, letterArray, index);
    		


            if (theLeft != null){
            	return theLeft;
            }
            if (theRight != null){
            	return theRight;
            }
            else{
            	return null;
            }
        }


    	// return null;

    }

    public void insert(String word, double val){
    	String[] splitWord =  word.split("");
    	recursiveInsert(root, splitWord, val, 0, word);
    }

    private void recursiveInsert(TSTNode node, String[] letterArray, double val, int index, String fullWord){
    	if (index >= letterArray.length){
    		return;
    	}
    	// if (index == letterArray.length - 1){
    	// 	node.val = val;
    	// 	return;
    	// }
    	if (node.max < val){
    		node.max = val;
    	}
    	String theLetter = letterArray[index];
    	if (theLetter.equals(node.letter)){
    		if (index == letterArray.length - 1){
    		    node.val = val;
    		    node.fullWord = fullWord;
    		    // System.out.println(fullWord);
    		    return;
    	    }
    		if (node.mid == null){
    			node.mid = new TSTNode(letterArray[index + 1], 0, val);
    		}
    		recursiveInsert(node.mid, letterArray, val, index + 1, fullWord);
    	}
    	if (theLetter.compareTo(node.letter) < 0){
    		if (node.left == null){
    		    node.left = new TSTNode(letterArray[index], 0, val);
    		}
    		recursiveInsert(node.left, letterArray, val, index, fullWord);

    	}
    	if (theLetter.compareTo(node.letter) > 0){
    		if (node.right == null){
    		    node.right = new TSTNode(letterArray[index], 0, val);
    		} 
    		recursiveInsert(node.right, letterArray, val, index,fullWord);
    		
    	}
    }

}

