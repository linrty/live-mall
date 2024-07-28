package top.linrty.live.common.utils;

import java.util.Collections;
import java.util.Stack;

public class CalculatorUtils {
    private Stack<String> postfixStack = new Stack();
    private Stack<Character> opStack = new Stack();
    private int[] operatPriority = new int[]{0, 3, 2, 1, -1, 1, 0, 2};

    public CalculatorUtils() {
    }

    public static double conversion(String expression) {
        expression = expression.replaceAll(" ", "");
        double result = 0.0;
        CalculatorUtils cal = new CalculatorUtils();

        try {
            expression = transform(expression);
            result = cal.calculate(expression);
            return result;
        } catch (Exception var5) {
            return Double.NaN;
        }
    }

    private static String transform(String expression) {
        char[] arr = expression.toCharArray();

        for(int i = 0; i < arr.length; ++i) {
            if (arr[i] == '-') {
                if (i == 0) {
                    arr[i] = '~';
                } else {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                        arr[i] = '~';
                    }
                }
            }
        }

        if (arr[0] != '~' && arr[1] != '(') {
            return new String(arr);
        } else {
            arr[0] = '-';
            return "0" + new String(arr);
        }
    }

    public double calculate(String expression) {
        Stack<String> resultStack = new Stack();
        this.prepare(expression);
        Collections.reverse(this.postfixStack);

        while(!this.postfixStack.isEmpty()) {
            String currentValue = (String)this.postfixStack.pop();
            if (!this.isOperator(currentValue.charAt(0))) {
                currentValue = currentValue.replace("~", "-");
                resultStack.push(currentValue);
            } else {
                String secondValue = (String)resultStack.pop();
                String firstValue = (String)resultStack.pop();
                firstValue = firstValue.replace("~", "-");
                secondValue = secondValue.replace("~", "-");
                String tempResult = this.calculate(firstValue, secondValue, currentValue.charAt(0));
                resultStack.push(tempResult);
            }
        }

        return Double.valueOf((String)resultStack.pop());
    }

    private void prepare(String expression) {
        this.opStack.push(',');
        char[] arr = expression.toCharArray();
        int currentIndex = 0;
        int count = 0;

        for(int i = 0; i < arr.length; ++i) {
            char currentOp = arr[i];
            if (this.isOperator(currentOp)) {
                if (count > 0) {
                    this.postfixStack.push(new String(arr, currentIndex, count));
                }

                char peekOp = (Character)this.opStack.peek();
                if (currentOp == ')') {
                    while((Character)this.opStack.peek() != '(') {
                        this.postfixStack.push(String.valueOf(this.opStack.pop()));
                    }

                    this.opStack.pop();
                } else {
                    while(currentOp != '(' && peekOp != ',' && this.compare(currentOp, peekOp)) {
                        this.postfixStack.push(String.valueOf(this.opStack.pop()));
                        peekOp = (Character)this.opStack.peek();
                    }

                    this.opStack.push(currentOp);
                }

                count = 0;
                currentIndex = i + 1;
            } else {
                ++count;
            }
        }

        if (count > 1 || count == 1 && !this.isOperator(arr[currentIndex])) {
            this.postfixStack.push(new String(arr, currentIndex, count));
        }

        while((Character)this.opStack.peek() != ',') {
            this.postfixStack.push(String.valueOf(this.opStack.pop()));
        }

    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')';
    }

    public boolean compare(char cur, char peek) {
        boolean result = false;
        if (this.operatPriority[peek - 40] >= this.operatPriority[cur - 40]) {
            result = true;
        }

        return result;
    }

    private String calculate(String firstValue, String secondValue, char currentOp) {
        String result = "";
        switch (currentOp) {
            case '*':
                result = String.valueOf(ArithHelper.mul(firstValue, secondValue));
                break;
            case '+':
                result = String.valueOf(ArithHelper.add(firstValue, secondValue));
            case ',':
            case '.':
            default:
                break;
            case '-':
                result = String.valueOf(ArithHelper.sub(firstValue, secondValue));
                break;
            case '/':
                result = String.valueOf(ArithHelper.div(firstValue, secondValue));
        }

        return result;
    }
}

