package fr.axa.automation.webengine.helper;

import java.util.Stack;

public class OperatorAndOperandEvaluatorHelper {

    public static double evaluate(String expression) {
        return parseExpression(expression,new Stack<>(),new Stack<>());
    }

    private static double parseExpression(String expression,Stack<Double> values,Stack<String> ops) {
        expression = expression.replaceAll("\\s+", "");
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                int j = findClosingParenthesis(expression, i);
                values.push(parseExpression(expression.substring(i + 1, j), values, ops));
                i = j;
            } else if (Character.isDigit(expression.charAt(i)) || (expression.charAt(i) == '-' && i + 1 < expression.length() && Character.isDigit(expression.charAt(i + 1)))) {
                int j = i;
                if (expression.charAt(i) == '-') {
                    j++;
                }
                while (j < expression.length() && (Character.isDigit(expression.charAt(j)) || expression.charAt(j) == '.' || expression.charAt(j) == ',')) {
                    j++;
                }
                values.push(Double.parseDouble(expression.replace(",",".").substring(i, j)));
                i = j - 1;
            } else if (expression.charAt(i) == ';') {
                continue;
            } else {
                int j = i;
                while (j < expression.length() && Character.isLetter(expression.charAt(j))) {
                    j++;
                }
                String op = expression.substring(i, j);
                ops.push(op);
                i = j - 1;
            }
        }

        while (!ops.isEmpty()) {
            String op = ops.pop();
            double b = values.pop();
            double a = values.pop();
            values.push(applyOperation(op, a, b));
        }

        return values.pop();
    }

    private static int findClosingParenthesis(String expression, int openIndex) {
        int closeIndex = openIndex;
        int counter = 1;
        while (counter > 0) {
            closeIndex++;
            if (expression.charAt(closeIndex) == '(') {
                counter++;
            } else if (expression.charAt(closeIndex) == ')') {
                counter--;
            }
        }
        return closeIndex;
    }

    private static double applyOperation(String op, double a, double b) {
        switch (op) {
            case "sum":
                return (a) + (b);
            case "minus":
                return (a) - (b);
            case "multiply":
                return (a) * (b);
            case "division":
                return (a) / (b);
            case "percentage":
                return ((a) / 100) * (b);
            default:
                throw new IllegalArgumentException("Unknown operation: " + op);
        }
    }

    public static void main(String[] args) {
        String expression = "sum(10;minus(20;multiply(2;3)))";
        double result = evaluate(expression);
        System.out.println("Result: " + result);
        expression = "sum(10;10;20)";
        result = evaluate(expression);
        System.out.println("Result: " + result);
        expression = "sum(10;10;20;minus(30;20;10))";
        result = evaluate(expression);
        System.out.println("Result: " + result);
        expression = "sum(10;10;20;minus(30;20;10);percentage(10;20))";
        result = evaluate(expression);
        System.out.println("Result: " + result);
    }
}


