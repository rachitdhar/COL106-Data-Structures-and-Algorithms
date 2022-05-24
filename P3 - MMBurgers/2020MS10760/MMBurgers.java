
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

class AVLNode {
	public int id = -2; // dummy default value
	public Customer node = null;
	public AVLNode leftChild = null;
	public AVLNode rightChild = null;
	public AVLNode parent = null;

	// constructors
	public AVLNode() {}
	public AVLNode(int i, Customer N, AVLNode p) { id = i; node = N; parent = p; }
}

// AVL Search Tree
class AVLTree {
	AVLNode root = new AVLNode();

	public void addNode(Customer N, int id) {
		AVLNode current = root;

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

	public Customer getNode(int id) throws IllegalNumberException {
		AVLNode current = root;
		
		while (true) {
			if (current.id == id) return current.node;
			else if (current.id < id && current.rightChild != null) current = current.rightChild;
			else if (current.id > id && current.leftChild != null) current = current.leftChild;
			else throw new IllegalNumberException("Illegal Number Exception");
		}
	}
}

class Customer {
    public int id;
    public int numb;
    public int queueNumber;
    public int arrivalTime;

    public int queueWaitTime = 0;
    public int foodWaitTime = 0;
    public boolean leftBuilding = false;

    public boolean waitingInQueue = true;
    public boolean waitingForFood = false;

    // constructor
    public Customer(int id, int numb, int q, int t) {
        this.id = id;
        this.numb = numb;
        queueNumber = q;
        arrivalTime = t;
    }
}

class Burger {
	public int id; // customer id who ordered the burger
	public int orderTime;
	public boolean cooked = false;
	public boolean inWaitingQueue = true;

	// constructor
	public Burger(int id, int orderTime) { this.id = id; this.orderTime = orderTime; }
}

class Event {
	
}

public class MMBurgers implements MMBurgersInterface {
    private int k;
    private int m;
    private int time = 0;

    private int totalWait = 0;
    private int totalCustomersCompleted = 0;

    private Queue<Customer> billingQueue[];
    private Queue<Queue<Event>> eventQueue = new Queue<Queue<Event>>();
    private AVLTree searchTree = new AVLTree();

	private Queue<Burger> waitingBurgers;
	private Queue<Burger> griddleBurgers;
    
    // constructor
    public MMBurgers() {};

    public boolean isEmpty(){
        if (eventQueue.size() == 0) return true;
        else return false;
    } 
    
    public void setK(int k) throws IllegalNumberException{
        if (k > 0) {
            this.k = k;

            // create k billing queues
            billingQueue = (Queue<Customer>[])(new Object[k]);
        } else {
            throw new IllegalNumberException("Illegal Number Exception");
        }
    }   
    
    public void setM(int m) throws IllegalNumberException{
        if (m > 0) {
            this.m = m;
        } else {
            throw new IllegalNumberException("Illegal Number Exception");
        }
    }

    public void advanceTime(int t) throws IllegalNumberException{
        try {
            while (time <= t) {
                Queue<Event> q = eventQueue.pop(); // queue of events that take place at time 'time'
                
				// execute event

				time++;
            }	
        } catch (Exception e) {
            throw new IllegalNumberException("Illegal Number Exception");
        }
    } 

    public void arriveCustomer(int id, int t, int numb) throws IllegalNumberException{
        if (t >= time && numb > 0) {
			advanceTime(t); // execute all events until time t

            int min = 0;
            for (int i = 0; i < k; i++) {
                if (billingQueue[i].size() < billingQueue[min].size()) min = i;
            }

            Customer c = new Customer(id, numb, min + 1, t);
            billingQueue[min].push(c);
            searchTree.addNode(c, id);
        } else {
            throw new IllegalNumberException("Illegal Number Exception");
        }	
    } 

    public int customerState(int id, int t) throws IllegalNumberException{
        if (t >= time) {
            Customer c = searchTree.getNode(id);

            if (c.arrivalTime > t) {
                return 0;
            } else if (c.waitingInQueue == true) {
                return c.queueNumber;
            } else if (c.waitingForFood == true) {
                return (c.queueNumber + 1);
            } else return (c.queueNumber + 2);
        } else {
            throw new IllegalNumberException("Illegal Number Exception");
        }
    } 

    public int griddleState(int t) throws IllegalNumberException{
        if (t >= time) {
			return griddleBurgers.size();
        } else {
            throw new IllegalNumberException("Illegal Number Exception");
        }
    } 

    public int griddleWait(int t) throws IllegalNumberException{
        if (t >= time) {
            return waitingBurgers.size();
        } else {
            throw new IllegalNumberException("Illegal Number Exception");
        }
    } 

    public int customerWaitTime(int id) throws IllegalNumberException{
        try {
            Customer c = searchTree.getNode(id);
            return (c.queueWaitTime + c.foodWaitTime);
        } catch (Exception e) {
            throw new IllegalNumberException("Illegal Number Exception");
        }
    } 

	public float avgWaitTime(){
        return ((float) totalWait / totalCustomersCompleted);
    }    
}