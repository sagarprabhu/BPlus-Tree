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
		
		// search for keys in leaf linked list
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
	 * Binary search used for searching the key
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
	
	/**
	 * Insert operation on B+ Tree
	 * @param key 
	 * @param value
	 */
	public void insert(int key, String value) {
		
		// if tree is not initialized
		if(this.root == null) {
			TreeNode node = new TreeNode();
			node.pairs.add(new Pair(key,value));
			node.parent = null;
			
			this.root = node;
		}
		// if there is only one node and not full
		else if(this.root.children.size()==0 && this.root.pairs.size() < (this.m-1)) 
			addToLeafNode(key,value,this.root);
		
		else {
			// traverse to the leaf node
			TreeNode currNode = this.root;
			while(currNode.children.size() != 0) 
				currNode = currNode.children.get(binarySearch(currNode.getPairs(), key));
			
			addToLeafNode(key,value,currNode);
			
			// split overfull leaf node
			if(currNode.pairs.size() == this.m)
				splitLeafNode(currNode); 
		}
	}
	
	/**
	 * Add dictionary pair to leaf node
	 * @param key 
	 * @param value
	 * @param node 
	 */
	public void addToLeafNode(int key ,String value, TreeNode node) {
		
		// find the index for insertion of the dictionary pair
		int index = binarySearch(node.pairs, key);
		
		// if key already exists
		if(index !=0 && node.pairs.get(index-1).key == key)
			node.pairs.get(index-1).values.add(value);
		else // create new key-value pair
			node.pairs.add(index, new Pair(key,value));
	}
	
	/**
	 * Splits overfull leaf node
	 * @param node The overfull node
	 */
	public void splitLeafNode(TreeNode node) {
		int mid = this.m /2;
		TreeNode middleNode = new TreeNode();
		TreeNode rightNode = new TreeNode();
		
		// rightNode has pairs >= mid
		int size = node.getPairs().size();
		rightNode.setPairs(node.getPairs().subList(mid,size));
		rightNode.parent = middleNode;
		
		// middleNode has key of middle element and rightNode as children
		middleNode.getPairs().add(new Pair(node.getPairs().get(mid).key));
		middleNode.children.add(rightNode);
		
		// remove Pairs >=m from current node
 		node.getPairs().subList(mid, size).clear();
		
		splitInternalNode(node.parent,node,middleNode, true);
	}
	
	/**
	 * Split overfull Internal Node
	 * @param currNode
	 * @param childNode
	 * @param newNode
	 * @param firstSplit
	 */
	public void splitInternalNode(TreeNode currNode, TreeNode childNode, TreeNode newNode, boolean firstSplit) {
		if(currNode == null) {
			// create new root
			this.root = newNode;
			// do binary search to find location of child node
			int index = binarySearch(newNode.getPairs(),childNode.getPairs().get(0).key);
			childNode.parent = newNode;
			newNode.children.add(index,childNode);
			
			// update leaf linked list once
			if(firstSplit) {
				if(index ==0) {
					newNode.children.get(0).next = newNode.children.get(1);
					newNode.children.get(1).prev = newNode.children.get(0);
				}
				else {
					newNode.children.get(index+1).prev = newNode.children.get(index);
					newNode.children.get(index-1).next = newNode.children.get(index);
				}
			}
		}else {
			mergeInternal(newNode,currNode);
			// if internal node becomes full, perform split again
			if(currNode.getPairs().size()== this.m) {
				int mid = (int)Math.ceil(this.m / 2) -1;
				
				TreeNode middleNode = new TreeNode();
				TreeNode rightNode = new TreeNode();
				
				// rightNode has pairs > mid
				int size = currNode.getPairs().size();
				rightNode.setPairs(currNode.getPairs().subList(mid +1 ,size));
				rightNode.parent = middleNode;
				
				// middleNode has key of middle element and rightNode as children
				middleNode.getPairs().add(new Pair(currNode.getPairs().get(mid).key));
				middleNode.children.add(rightNode);
				
				List<TreeNode> currChildren = currNode.getChildren();
				List<TreeNode> rightChildren = new ArrayList<>();
				
				int leftLastIndex = currChildren.size() -1;
				
				// move children of left to right part 
				for(int i=currChildren.size()-1; i>=0;i--) {
					List<Pair> currPairs = currChildren.get(i).getPairs();
					if(middleNode.getPairs().get(0).key <= currPairs.get(0).key) {
						currChildren.get(i).parent = rightNode;
						rightChildren.add(0,currChildren.get(i));
						leftLastIndex -=1;
					}
					else break;
				}
				rightNode.children = rightChildren;
				
				
				// update the curr overfull node: remove Pairs >=m from current node 
		 		currNode.getPairs().subList(mid, size).clear();
		 		currNode.children.subList(leftLastIndex+1, currChildren.size()).clear();
		 		
		 		// propagate split upwards
		 		splitInternalNode(currNode.parent, currNode,middleNode,false);
			}
		}
	}
	/**
	 * Merge internal nodes after split operation
	 * @param middleNode Node generated after split
	 * @param mergeInto Parent node to be merged into
	 */
	public void mergeInternal(TreeNode middleNode, TreeNode mergeInto) {
		Pair pairToBeInserted = middleNode.getPairs().get(0);
		TreeNode childToBeInserted = middleNode.getChildren().get(0);
		
		// find the insert index of key using binary search
		int index = binarySearch(mergeInto.getPairs(),pairToBeInserted.key);
		int childInsertPos = index;
		if(pairToBeInserted.key <= childToBeInserted.getPairs().get(0).key)
			childInsertPos +=1;
		
		childToBeInserted.parent= mergeInto;
		mergeInto.children.add(childInsertPos,childToBeInserted);
		mergeInto.getPairs().add(index,pairToBeInserted);
		
		// update linklist pointers of external nodes
		if(mergeInto.getChildren().size() != 0 && mergeInto.getChildren().get(0).getChildren().size() == 0) {
			
			// if merged at last element
			if(mergeInto.getChildren().size()-1 !=childInsertPos 
					&& mergeInto.getChildren().get(childInsertPos+1).prev == null) {
				mergeInto.getChildren().get(childInsertPos+1).prev = mergeInto.getChildren().get(childInsertPos);
				mergeInto.getChildren().get(childInsertPos).next = mergeInto.getChildren().get(childInsertPos+1);
			} 
			// if merged at first element
			else if(childInsertPos != 0 && mergeInto.getChildren().get(childInsertPos-1).next == null) {
				mergeInto.getChildren().get(childInsertPos).prev = mergeInto.getChildren().get(childInsertPos-1);
				mergeInto.getChildren().get(childInsertPos-1).next = mergeInto.getChildren().get(childInsertPos);
			}
			// for in-between merges
			else {
				mergeInto.getChildren().get(childInsertPos).next =mergeInto.getChildren().get(childInsertPos-1).next;
				mergeInto.getChildren().get(childInsertPos).next.prev=mergeInto.getChildren().get(childInsertPos);
				mergeInto.getChildren().get(childInsertPos - 1).next = mergeInto.getChildren().get(childInsertPos);
				mergeInto.getChildren().get(childInsertPos).prev =mergeInto.getChildren().get(childInsertPos - 1);
			}		
		}
	}
	
	
	
	
}
