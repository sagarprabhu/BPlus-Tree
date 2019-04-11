import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author sagar prabhu
 * Class for defining B+ Tree node structure
 */
public class TreeNode {
	/**
	 * List of dict pairs in the node
	 */
	List<Pair> pairs;
	
	/**
	 * List of children of current node
	 */
	List<TreeNode> children;
	
	/**
	 * Pointers to previous and next nodes. Set only for leaf nodes. 
	 */
	TreeNode prev, next;
	
	/**
	 * Pointer to parent node
	 */
	TreeNode parent;
	
	/**
	 * TreeNode Constructor
	 */
	public TreeNode() {
		this.pairs = new ArrayList<>();
		this.children = new ArrayList<>();
		this.prev = this.next = null;
	}
	
	/**
	 * Getter method for pairs
	 * @return a list of pairs
	 */
	public List<Pair> getPairs(){
		return pairs;
	}
	
	/**
	 * Setter method for pairs
	 * @param pairs List of key-value pairs
	 */
	public void setPairs(List<Pair> pairs) {
		Iterator<Pair> itr = pairs.iterator();
		while(itr.hasNext()) 
			this.pairs.add(itr.next());
	}
	
	/**
	 * Getter method for children
	 * @return list of children pointers of current node
	 */
	public List<TreeNode> getChildren() {
		return this.children;
	}
	
	public String toString() {
		return "Pairs= "+this.pairs.toString();
	}
	
}
