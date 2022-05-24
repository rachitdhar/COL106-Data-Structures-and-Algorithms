public class Main {
    public static void main(String args[]) {
        Calculator c = new Calculator();
        try {
            System.out.println(c.evaluatePostFix("2 3 5 + *"));
            System.out.println(c.convertExpression("3 + (5 * 6 / (3 / 1)) * 2 * 2 - 9 / 3"));
        } catch (Exception e) {
            System.out.println("error");
        }
    }
}
