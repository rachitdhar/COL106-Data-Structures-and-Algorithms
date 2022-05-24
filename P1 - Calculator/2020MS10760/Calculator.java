import java.util.HashMap;

public class Calculator {
    public Calculator() {}; // constructor

    private boolean isOperator(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/') return true;
        else return false;
    }

    private int operate(char op, Integer f, Integer s) {
        if (op == '+') return (int) (f + s);
        else if (op == '-') return (int) (f - s);
        else if (op == '*') return (int) (f * s);
        else return (int) (f / s);
    }

    public int evaluatePostFix(String s) throws Exception {
        MyStack<Integer> A = new MyStack<Integer>();

        try {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) != ' ') {
                    if (isOperator(s.charAt(i)) == true) {
                        Integer second = A.pop();
                        Integer first = A.pop();
                        
                        A.push(operate(s.charAt(i), first, second));
                    } else {
                        Integer num = 0;
                        while (s.charAt(i) != ' ' && isOperator(s.charAt(i)) == false) {
                            num = (num * 10) + (Integer) (s.charAt(i) - '0');
                            i++;
                        }
                        A.push(num);
                        i--;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("InvalidPostfixException");
        }

        return (int) A.top();
    }

    public String convertExpression(String s) throws Exception {
        String postfix = "";
        MyStack<Character> op = new MyStack<Character>();
        HashMap<Character, Integer> precedence = new HashMap<Character, Integer>();

        precedence.put('(', 0);
        precedence.put(')', 0);
        precedence.put('+', 1);
        precedence.put('-', 1);
        precedence.put('*', 2);
        precedence.put('/', 3);

        try {
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);

                if (c != ' ') {
                    if (isOperator(c) == false && c != '(' && c != ')') {
                        int init_index = i;
                        while ((i + 1) < s.length() && s.charAt(i + 1) != ' ' && isOperator(s.charAt(i + 1)) == false && s.charAt(i + 1) != ')') {
                            i++;
                        }
			
                        if (postfix != "") postfix += " ";
                        postfix += s.substring(init_index, i + 1);
                    } else if (c == '(') {
                        op.push('(');
                    } else if (isOperator(c) == true || c == ')') {
			if (op.isEmpty() == true) op.push((Character) c);
			else {
				int p = precedence.get((Character) c);

                        	if (p > precedence.get(op.top())) op.push((Character) c);
				else if (p == precedence.get(op.top()) && p > 1) op.push((Character) c);
                        	else {
                            		while (op.isEmpty() == false) {
                                		Character opChar = op.pop();
                                	if (opChar != '(') {
                                    		postfix = postfix + " " + opChar;
                                	} else break;
                            	}

                            	if (c != ')') op.push((Character) c);
                        	}
			}
                    }
                }
            }

	    while (op.isEmpty() == false) {
		postfix = postfix + " " + op.pop();
	    }
        } catch (Exception e) {
            throw new Exception("InvalidExprException");
        }

        return postfix;
    }
}
