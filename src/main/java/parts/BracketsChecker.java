package parts;

import java.util.Stack;

public class BracketsChecker {
    public static boolean balanceBrackets(String expression) {
        Stack<Character> stack = new Stack<>();

        for (char ch : expression.toCharArray()) {
            if (ch == '(') {
                stack.push(ch);
            } else if (ch == ')') {
                if (stack.isEmpty() || stack.peek() != '(') {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    public static boolean isEquationBalanced(String equation) {
        String[] parts = equation.split("=");
        if (parts.length != 2) {
            return false; // Equation should have exactly two sides
        }

        return balanceBrackets(parts[0]) && balanceBrackets(parts[1]);
    }
}