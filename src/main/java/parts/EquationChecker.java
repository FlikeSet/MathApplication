package parts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquationChecker {
    public static boolean isEquationValid(String equation) {
        equation = equation.replaceAll("\\s", "");
        boolean equalitySignFound = false;
        boolean X_Found = false;

        for (int i = 0; i < equation.length(); i++) {
            char c = equation.charAt(i);


            switch (c) {
                case '+':
                case '*':
                case '/':
                    if (i == 0 || i == equation.length() - 1 ||
                            !isSymbol(equation.charAt(i + 1)) && equation.charAt(i + 1) !='(') {
                        System.out.println("'+' || '*' || '/' wrong position on index "+i);
                        return false;
                    }
                    continue;
                case '-':
                    if (i == equation.length() - 1 ||
                            !isSymbol(equation.charAt(i + 1)) && equation.charAt(i + 1) !='(') {
                        System.out.println("- wrong position on index "+i);
                        return false;
                    }
                    continue;
                case '=':
                    if (i == 0 || i == equation.length() - 1 || equalitySignFound) {
                        System.out.println("= wrong position or not 1 in equation on index "+i);
                        return false;
                    }
                    equalitySignFound = true;
                    continue;
                case '.':
                    if (i == 0 || i == equation.length()-1 ||
                            !isDigit(equation.charAt(i + 1)) || !isDigit(equation.charAt(i - 1))) {
                        System.out.println(". wrong position on index "+i);
                        return false;
                    }
                    continue;
                case '(':
                    if ((i == (equation.length() - 1)) ||
                            (((i > 0) && isSymbol(equation.charAt(i - 1)) || ((i > 0) && equation.charAt(i - 1) ==')') ||
                            ((!isSymbol(equation.charAt(i + 1)) && equation.charAt(i + 1) !='(' && equation.charAt(i + 1) !='-'))||
                            equation.charAt(i + 1) ==')'))){
                        System.out.println("( wrong position on index "+i);
                        return false;
                    }
                    continue;
                case ')':
                    if (i==0 ||(i < (equation.length() - 1) && isSymbol(equation.charAt(i + 1))) ||
                            (i < (equation.length() - 1) && equation.charAt(i - 1) !=')'&& !isSymbol(equation.charAt(i - 1)))) {
                        System.out.println(") wrong position on index "+i);
                        return false;
                    }
                    continue;
                case 'x':
                    if (!X_Found){
                        X_Found = true;
                    }
                    if ((i < (equation.length() - 1) && isSymbol(equation.charAt(i + 1)))||(i > 0 && isDigit(equation.charAt(i - 1)))) {
                        System.out.println("x wrong position on index "+i);
                        return false;
                    }
                    continue;
                default:
                    if (!isSymbol(c)) {
                        System.out.println("Wrong symbol input on index "+i);
                        return false;
                    }
                    break;
            }
        }

        if (!X_Found){
            System.out.println("x missing");
            return false;
        }

        if (!equalitySignFound){
            System.out.println("= missing or more than 1");
            return false;
        }

        if (!BracketsChecker.isEquationBalanced(equation)){
            System.out.println("Unbalanced brackets");
            return false;
        }

        return true;

    }

    private static boolean isSymbol(char c) {
        return Character.isDigit(c) || c =='x';
    }

    private static boolean isDigit(char c) {
        return Character.isDigit(c);
    }

    public static int countNumbersInEquation(String equation) {
        int count = 0;
        Pattern pattern = Pattern.compile("-?\\d*\\.?\\d+");
        Matcher matcher = pattern.matcher(equation);
        while (matcher.find()) {
            count++;
        }
        return count;
    }
}
