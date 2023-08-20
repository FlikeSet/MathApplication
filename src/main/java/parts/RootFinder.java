package parts;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class RootFinder {
    public static double findRoot(String equation, double x) {
        String[] sides = equation.split("=");
        if (sides.length == 2) {
            String leftSide = sides[0].trim();
            String rightSide = sides[1].trim();
            String full = leftSide + "-(" + rightSide + ")";
            String newEquation = full.replace("x", String.valueOf(x));
            return evaluateExpression(newEquation);

        } else {
            System.out.println("Invalid equation format.");
            return 0;
        }
    }

    public static double evaluateExpression(String expression) {
        ScriptEngine manager = new ScriptEngineManager().getEngineByName("graal.js");
        try {
            Object evalResult = manager.eval(expression);
            if (evalResult instanceof Number) {
                return ((Number) evalResult).doubleValue();
            } else {
                throw new IllegalArgumentException("Expression did not evaluate to a number.");
            }
        } catch (ScriptException e) {
            throw new IllegalArgumentException("Error evaluating the expression: " + e.getMessage());
        }
    }
}