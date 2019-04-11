import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author sagar prabhu
 * 
 */
public class BPlusTree {

	int m;
	
	TreeNode root;
	
	/**
	 * Constructor for initializing the tree
	 * @param m the order of tree
	 */
	public BPlusTree(int m) {
		this.m = m;
		root = null;
		// System.out.println("B+ Tree Initialized. Degree is " + m);
	}
	
	/**
	 * Search operation on B+ Tree
	 * @param key The search key
	 * @return List of values associated with the key
	 */
	public List<String> search(int key){
		// traverse to the leaf node
		TreeNode currNode = this.root;
		while(currNode.children.size() !=0)
			currNode = currNode.children.get(binarySearch(currNode.getPairs(), key));
				
		// linearly search for the key in the node
		for(Pair x: currNode.getPairs()) {
			if(key == x.key) 
				return x.values;
			if(key < x.key)
				break;
		}
		return null;
	}
	
	/**
	 * Range search operation on B+ Tree
	 * @param key1
	 * @param key2
	 * @return List of values such that in the range key1 <= key <= key2
	 */
	public List<Pair> search(int key1, int key2){
		// traverse to the leaf node
		TreeNode currNode = this.root;
		while(currNode.children.size() != 0) 
			currNode = currNode.children.get(binarySearch(currNode.getPairs(), key1));
		
		List<Pair> result = new ArrayList<>();
		boolean endSearch = false;
		while(currNode != null && !endSearch) {
			
			for(Pair x: currNode.getPairs()) {
				if(x.key >= key1 && x.key <= key2)
					result.add(x);
				if(x.key > key2) {
					endSearch = true;
					break;
				}
			}
			currNode = currNode.next;
		}

		return result;
	}
	
	/**
	 * Binary search used for searching a key
	 * @param pairs List of pairs
	 * @param target Key to be searched
	 * @return The first index at which the target key is less than list of keys
	 */
	public int binarySearch(List<Pair> pairs, int target) {
		int start= 0;
		int end = pairs.size() -1;
		
		// if target is less than first key
		if(target < pairs.get(0).key)
			return 0;
		
		// if target is greater than last key
		if(target >= pairs.get(end).key)
			return pairs.size();
		
		while(start <= end) {
			
			int mid =(start+end)/2;
			
			if(target >= pairs.get(mid-1).key && target < pairs.get(mid).key)
				return mid; 
			
			if(target >= pairs.get(mid).key)
				start = mid +1;
			else
				end = mid -1;
		}
		return -1;
	}
	
	
}
