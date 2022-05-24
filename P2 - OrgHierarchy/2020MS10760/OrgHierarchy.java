// import java.io.*; 
// import java.util.*; 
import java.lang.Math;

// Queue class
class Queue<T> {
	public int length = 2;
	private T q[];

	private int t = 0, r = 0;

	// constructor
	public Queue() {
		q = (T[])(new Object[length]);
	}

	public boolean isEmpty() {
		if (t == r) return true;
		else return false;
	}

	public int size() {
		return (length + r - t) % length;
	}

	public void push(T o) {
		if (size() == length - 1) { // full queue
			T temp[] = (T[])(new Object[length * 2]);

			for (int i = 0, j = t; i < length; i++) {
				temp[i] = q[j];
				j = (j + 1) % length;
			}

			q = temp;
			t = 0; r = length - 1;

			length *= 2;
		}

		q[r] = o;
		r = (r + 1) % length;
	}

	public T pop() {
		T topElement = q[t];
		q[t] = null;
		t = (t + 1) % length;
		return topElement;
	}

	public T top() {
		return q[t];
	}

	public T bottom() {
		return q[(length + r - 1) % length];
	}
}

// SLinked List Class
class SLList {
	public Node head = null;

	public void addChild(Node n) {
		if (head != null) {
			Node tempNode = head;
			head = n;
			n.next = tempNode;
		} else head = n;
	}

	public void removeChild(Node n) {
		Node temp = head;
		
		if (temp.getElement() != n.getElement()) {
			while (temp.next.getElement() != n.getElement()) {
				temp = temp.next;
			}

			temp.next = n.next;
			n.next = null;
		} else head = null;
	}
}

// Tree node
class Node {
	private int element;
	private int level = 1;
	public Node parent = null;
	public SLList children = new SLList();
	public Node next = null; // the next sibling

	// constructors
	public Node() {}
	public Node(int num) {
		element = num;
	}
	public Node(int num, Node boss) {
		element = num;
		parent = boss;
		level = parent.level + 1;
	}

	public void setElement(int e) { element = e; }
	
	public int getElement() {
		return element;
	}

	public int getLevel() {
		return level;
	}
}

class AVLNode {
	public int id = -2; // dummy default value
	public Node node = null;
	private int height = 0;
	public AVLNode leftChild = null;
	public AVLNode rightChild = null;
	public AVLNode parent = null;

	// constructors
	public AVLNode() {}
	public AVLNode(int i, Node N, AVLNode p) { id = i; node = N; parent = p; }

	public int balanceFactor() {
		int a = (leftChild != null ? leftChild.height : -1);
		int b = (rightChild != null ? rightChild.height : -1);
		return (a - b);
	}

	public int getHeight() {
		int a = (leftChild != null ? leftChild.getHeight() : -1);
		int b = (rightChild != null ? rightChild.getHeight() : -1);
		height = (Math.max(a, b) + 1);
		return height;
	}
}

// AVL Search Tree
class AVLTree {
	AVLNode root = new AVLNode();

	private void leftRotation(AVLNode N) {
		AVLNode NR = N.rightChild;
		AVLNode NRL = NR.leftChild;

		AVLNode p = N.parent;
		N.rightChild = NRL;
		NR.leftChild = N;
		NR.parent = p;
		NRL.parent = N;
		N.parent = NR;

		// updating root
		if (p == null) root = NR;

		// updating heights of N and NR, and storing their returned values in dummy variable
		int dummy_h = N.getHeight();
		dummy_h = NR.getHeight();
	}

	private void rightRotation(AVLNode N) {
		AVLNode NL = N.leftChild;
		AVLNode NLR = NL.rightChild;

		AVLNode p = N.parent;
		N.leftChild = NLR;
		NL.rightChild = N;
		NLR.parent = N;
		NL.parent = p;
		N.parent = NL;

		// updating root
		if (p == null) root = NL;

		// updating heights of N and NL, and storing their returned values in dummy variable
		int dummy_h = N.getHeight();
		dummy_h = NL.getHeight();
	}

	private void balanceTree(AVLNode N) {
		int b = N.getHeight(); // to update the heights. Value returned is irrelevant
		b = N.balanceFactor();

		if (b > 1) {
			if (N.rightChild.rightChild.getHeight() > N.rightChild.leftChild.getHeight()) leftRotation(N);
			else rightRotation(N);
		} else if (b < -1) {
			if (N.leftChild.rightChild.getHeight() > N.leftChild.leftChild.getHeight()) leftRotation(N);
			else rightRotation(N);
		}
	}

	public void addNode(Node N) {
		AVLNode current = root;
		int id = N.getElement();

		if (current.node == null) { // inserting at root
			current.node = N;
			current.id = id;
		} else {
			while (true) {
				if (current.id < id && current.rightChild != null) current = current.rightChild;
				else if (current.id > id && current.leftChild != null) current = current.leftChild;
				else if (current.id < id && current.rightChild == null) {
					current.rightChild = new AVLNode(id, N, current);
					break;
				}
				else if (current.id > id && current.leftChild == null) {
					current.leftChild = new AVLNode(id, N, current);
					break;
				}
			}
			//balanceTree(current);
		}
	}

	private void removeLeafNode(AVLNode mainNode, int id) {
		if (mainNode.parent == null) { // cannot allow empty tree
			mainNode.node = null;
			mainNode.id = -2; // dummy value
		} else {
			mainNode.node = null;
			AVLNode p = mainNode.parent;

			if (p.id > id) p.leftChild = null;
			else p.rightChild = null;

			mainNode.parent = null;

			//balanceTree(p);
		}
	}

	private void removeSingleChildNode(AVLNode mainNode, int id) {
		AVLNode child = (mainNode.leftChild != null) ? mainNode.leftChild : mainNode.rightChild;

		if (mainNode.parent == null) { // has no parent
			root = child;
			child.parent = null;
		} else {
			AVLNode p = mainNode.parent;

			if (id > p.id) p.rightChild = child;
			else p.leftChild = child;

			child.parent = p;
		}

		mainNode.leftChild = null;
		mainNode.rightChild = null;
		mainNode.parent = null;
		mainNode.node = null;

		//balanceTree(child);
	}

	private void removeDoubleChildNode(AVLNode mainNode) {
		// find smallest id node in right subtree, and copy its data into mainNode
		AVLNode copyNode = mainNode.rightChild;

		while (copyNode.leftChild != null) {
			copyNode = copyNode.leftChild;
		}

		mainNode.node = copyNode.node;
		mainNode.id = copyNode.id;

		// removing copynode
		if (copyNode.rightChild == null) {
			removeLeafNode(copyNode, copyNode.id);
		} else removeSingleChildNode(copyNode, copyNode.id);
	}

	public void removeNode(int id) {
		// searching for node
		AVLNode mainNode = root;

		while (true) {
			if (mainNode.id == id) break;
			else if (mainNode.id < id && mainNode.rightChild != null) mainNode = mainNode.rightChild;
			else if (mainNode.id > id && mainNode.leftChild != null) mainNode = mainNode.leftChild;
		}

		// removal
		if (mainNode.leftChild == null && mainNode.rightChild == null) { // if leaf node
			removeLeafNode(mainNode, id);
		} else if (mainNode.leftChild == null || mainNode.rightChild == null) { // if single child node
			removeSingleChildNode(mainNode, id);
		} else { // double child node
			removeDoubleChildNode(mainNode);
		}
	}

	public Node getNode(int id) throws IllegalIDException {
		AVLNode current = root;
		
		while (true) {
			if (current.id == id) return current.node;
			else if (current.id < id && current.rightChild != null) current = current.rightChild;
			else if (current.id > id && current.leftChild != null) current = current.leftChild;
			else throw new IllegalIDException("Illegal ID Exception");
		}
	}
}

public class OrgHierarchy implements OrgHierarchyInterface{
	//root node
	Node root = new Node();
	private int size = 0;

	// search tree
	AVLTree searchTree = new AVLTree();

	// constructor
	public OrgHierarchy() {}

	public boolean isEmpty(){
		if (size == 0) return true;
		else return false;	
	} 

	public int size(){
		return size;
	}

	public int level(int id) throws IllegalIDException{
		Node employee = searchTree.getNode(id);
		return employee.getLevel();
	} 

	public void hireOwner(int id) throws NotEmptyException{
		if (isEmpty() == true) {
			root.setElement(id);
			searchTree.addNode(root);
			size++;
		} else {
			throw new NotEmptyException("Not Empty Exception");
		}
	}

	public void hireEmployee(int id, int bossid) throws IllegalIDException{
		try {
			Node boss = searchTree.getNode(bossid);
			Node employee = new Node(id, boss);
			
			boss.children.addChild(employee);
			searchTree.addNode(employee);
			size++;
		} catch (Exception e) {
			throw new IllegalIDException("Illegal ID Exception");
		}
	} 

	public void fireEmployee(int id) throws IllegalIDException{
		try {
			Node employee = searchTree.getNode(id);
			Node boss = employee.parent;

			boss.children.removeChild(employee);
			employee.parent = null;
			searchTree.removeNode(id);
			size--;
		} catch (Exception e) {
			throw new IllegalIDException("Illegal ID Exception");
		}
	}
	
	public void fireEmployee(int id, int manageid) throws IllegalIDException{
		try {
			Node employee = searchTree.getNode(id);
			Node manageEmployee = searchTree.getNode(manageid);
			Node boss = employee.parent;

			boss.children.removeChild(employee);
			employee.parent = null;
			searchTree.removeNode(id);

			// to add employee's children to manageEmployee node
			
			Node child = employee.children.head;

			while (child != null) {
				Node tempChild = child.next;
				child.next = null;
				manageEmployee.children.addChild(child);
				child.parent = manageEmployee;
				child = tempChild;
			}
			
			employee.children.head = null;
			size--;
		} catch (Exception e) {
			throw new IllegalIDException("Illegal ID Exception");
		}
	} 

	public int boss(int id) throws IllegalIDException{
		Node employee = searchTree.getNode(id);
		if (employee == root) return -1;
		else return employee.parent.getElement();
	}

	public int lowestCommonBoss(int id1, int id2) throws IllegalIDException{
		Node e1 = searchTree.getNode(id1);
		Node e2 = searchTree.getNode(id2);

		while (e1.getElement() != e2.getElement()) {
			if (e1.getLevel() > e2.getLevel()) e1 = e1.parent;
			else if (e1.getLevel() < e2.getLevel()) e2 = e2.parent;
			else { e1 = e1.parent; e2 = e2.parent; }
		}

		return e1.getElement();
	}

	private String sort(String s) {
		int nums[] = new int[s.length()];
		int count = 0;

		int firstIndex = 0; boolean scanning = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c != ' ' && scanning == false) {
				firstIndex = i;
				scanning = true;
			} else if (c == ' ' && scanning == true) {
				nums[count] = Integer.parseInt(s.substring(firstIndex, i));
				count++;
				scanning = false;
			}
		}
		nums[count] = Integer.parseInt(s.substring(firstIndex, s.length()));
		count++; scanning = false;

		// sorting array nums
		for (int i = 1; i < count; i++) {
			int j = i - 1;

			while (j >= 0) {
				if (nums[j] > nums[j + 1]) {
					nums[j] += nums[j + 1];
					nums[j + 1] = nums[j] - nums[j + 1];
					nums[j] -= nums[j + 1];

					j--;
				} else break;
			}
		}

		// converting back into string
		String sortedStr = "";

		for (int i = 0; i < count; i++) {
			if (sortedStr != "") {
				sortedStr = sortedStr + " " + nums[i];
			} else sortedStr += nums[i];
		}

		return sortedStr;
	}

	public String toString(int id) throws IllegalIDException{
		Node employee = searchTree.getNode(id);
		String s = "";

		Queue<Node> org = new Queue<Node>();
		org.push(employee);
		
		while (org.size() > 0) {
			Node top = org.top();
			
			if (top.children.head != null) {
				Node child = top.children.head;
				
				if (org.bottom() != null) {
					if (org.bottom().getLevel() != child.getLevel()) org.push(new Node(-2));
				} // for comma
				
				do {
					org.push(child);
					child = child.next;
				} while (child != null);
			}
			
			if (top.getElement() == -2) s += ",";
			else {
				if (s == "" || s.charAt(s.length() - 1) == ',') s += top.getElement();
				else s = s + " " + top.getElement();
			}
			
			top = org.pop();
		}
		
		int firstIndex = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == ',') {
				s = s.substring(0, firstIndex) + sort(s.substring(firstIndex, i)) + s.substring(i, s.length());
				firstIndex = i + 1;
			}
		}
		s = s.substring(0, firstIndex) + sort(s.substring(firstIndex, s.length()));
		return s;
	}
}
