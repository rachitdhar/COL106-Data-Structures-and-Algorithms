import java.util.EmptyStackException;

interface StackInterface<T> {
    public void push(T o);
    public T pop() throws EmptyStackException;
    public T top() throws EmptyStackException;
    public boolean isEmpty();
    public String toString();
}

public class MyStack<T> implements StackInterface<T> {
    public int length = 1;  // tells the size of the entire array object
    private int t = -1;     // tells the index of top of the stack. If empty then set to -1.
    private T s[];          // the stack array

    // constructor
    public MyStack() {
        s = (T[]) (new Object[length]);
    }

    public int size() {
        return (t + 1);
    }

    public boolean isEmpty() {
        if (t == -1) return true;
        else return false;
    }

    public T top() throws EmptyStackException {
        if (isEmpty() == true) {
            throw new EmptyStackException();
        } else {
            return s[t];
        }
    }

    public T pop() throws EmptyStackException {
        if (isEmpty() == true) {
            throw new EmptyStackException();
        } else {
            T element = s[t];
            s[t] = null;
            t--;
            return element;
        }
    }

    public void push(T o) {
        if (length == t + 1) {
            T temp[] = (T[]) (new Object[length * 2]);

            for (int i = 0; i <= t; i++) {
                temp[i] = s[i];
            }
            s = temp;
            length *= 2;
        }

        t++;
        s[t] = o;
    }

    public String toString() {
        if (isEmpty() == true) {
            return "[]";
        } else {
            String str = "[";

            for (int i = t; i >= 0; i--) {
                str = str + "" + s[i];
                if (i > 0) str += ",";
            }
            str += "]";
            return str;
        }
    }
}