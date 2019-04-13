COMPILER = javac
FLAGS = -g

default: Pair.class TreeNode.class BPlusSearchTree.class bplustree.class

Pair.class: Pair.java
	$(COMPILER) $(FLAGS) Pair.java

TreeNode.class: TreeNode.java
	$(COMPILER) $(FLAGS) TreeNode.java

BPlusSearchTree.class: BPlusSearchTree.java
	$(COMPILER) $(FLAGS) BPlusSearchTree.java

bplustree.class: bplustree.java
	$(COMPILER) $(FLAGS) bplustree.java

clean: 
	$(RM) *.class
