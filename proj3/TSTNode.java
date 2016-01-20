
public class TSTNode{
    public TSTNode left;
    public TSTNode mid;
    public TSTNode right;
    public double val;
    public double max;
    public String letter;
    public String fullWord;
    public Boolean checkThisNode;
    public TSTNode(String letter, double val, double max){
        this.letter = letter;
        this.val = val;
        this.max = max;
        this.checkThisNode = true;
    }
    public TSTNode copy(){
        TSTNode newNode = new TSTNode(this.letter, this.val, this.max);
        newNode.left = this.left;
        newNode.mid = this.mid;
        newNode.right = this.right;
        newNode.fullWord = fullWord;
        return newNode;
    }
}