public class MethodsWithArguments {
    static void calculate(int num1, int num2, String operator) {
        if (operator.equals("+")) {
            System.out.println(num1 + num2);
        } else if (operator.equals("-")) {
            System.out.println(num1 - num2);
        } else if (operator.equals("*")) {
            System.out.println(num1 * num2);
        } else if (operator.equals("/")) {
            System.out.println(num1 / num2);
        } else {
            System.out.println("Invalid operator");
        }
    }
    public static void main(String[] args) {
        calculate(4, 2, "+");
        calculate(4, 2, "-");
        calculate(4, 2, "*");
        calculate(4, 2, "/");
    }
}
