class ExpressionTypeDetector {
    public String detectType(String expression) {
        if (isInteger(expression)) {
            return "Integer";
        } else if (isFloat(expression)) {
            return "Float";
        } else if (isBoolean(expression)) {
            return "Boolean";
        } else if (isString(expression)) {
            return "String";
        } else {
            return "Exception";
        }
    }

    private boolean isInteger(String expression) {
        try {
            Integer.parseInt(expression);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isFloat(String expression) {
        try {
            Float.parseFloat(expression);
            // Check if it’s not an integer
            return expression.contains(".");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isBoolean(String expression) {
        return expression.equalsIgnoreCase("true") || expression.equalsIgnoreCase("false");
    }

    private boolean isString(String expression) {
        // Check if it starts and ends with double quotes
        return expression.startsWith("\"") && expression.endsWith("\"");
    }
}

public class Handler {
    public String Arithmetic(String Num1, String Num2, String Action) { // Handling Arithmetic equations
        try { // Checking corectness of numbers
            Long.parseLong(Num1);
            Long.parseLong(Num2);
        } catch (Exception e) {
            return "Exception";
        }
        String result = " ";
        long n1 = Long.parseLong(Num1);
        long n2 = Long.parseLong(Num2);
        switch (Action) { // Different type of equations
            case "+":
                result = String.valueOf(n1 + n2);
                break;
            case "-":
                result = String.valueOf(n1 - n2);
                break;
            case "*":
                result = String.valueOf(n1 * n2);
                break;
            case "%":
                result = String.valueOf(n1 % n2);
                break;
            case "/":
                if (n2 == 0) {
                    return "Exception";
                }
                result = String.valueOf(n1 / n2);
                break;
            default:
                result = "Error";
        }
        return result;
    }

    // Syntax check for variable declarations/assignments
    public Object HandleVar(String expression) {
        // Check if the expression contains an operator
        if (expression.contains("+") || expression.contains("-") || expression.contains("*") || expression.contains("/")
                || expression.contains("%")) {
            // Process arithmetic expression
            System.out.println("Expression detected, proceeding to syntax check...");

            if (!isValidExpression(expression)) {
                return "Syntax Error";
            }
            // Calculation
        } else {
            String Type = new ExpressionTypeDetector().detectType(expression);
            Object result = null;
            switch (Type) {
                case "Integer":
                    result = Integer.parseInt(expression);
                    break;
                case "Float":
                    result = Float.parseFloat(expression);
                    break;
                case "Boolean":
                    result = Boolean.parseBoolean(expression);
                    break;
                case "String":
                    result = expression;
                    break;
                default:
                    throw new AssertionError();
            }
            return result;
        }
        return null;
    }

    private Object calculate(String expression) {
        Object result = null;
        String operator = "";
        Long num1 = null;
        Long num2 = null;
        boolean isAdding = false;
        int index = 0;
        for (char c : expression.toCharArray()) {
            if (c == ' ') {
                if (index == -1){
                    operator += c;
                }
                continue;
            }
            if (c == '"' || c == '\'') {
                // String
                if (index == 0) {
                    if ((isAdding && operator != "") || operator == "") {
                        index = -1; 
                        continue;
                    } else {

                    }                    
                } else {
                    if (index == -1) {
                        if (operator.charAt(0) == c){
                            operator+=c;
                        } else{
                            index = 0;
                        }
                    } else {

                    }

                }
            } else if (Character.isDigit(c)) {
                // Number
                if (index == 0) {
                    if (num1 == null) {
                        num1 = Long.parseLong(String.valueOf(c));
                    } else {
                        
                    }
                } else {
    
                }
            } 
            switch (c) {
            // Add
                case '+':
                    break;
            // Subtract
                case '-':
                    break;
            // Multiply
                case '*':
                    break;
            // Divide
                case '/':
                    break;
            // Modulus
                case '%':
                    break;
                default:
                    break;
            }
        }
        return  result;
    }

    // Check if the arithmetic expression is valid
    private boolean isValidExpression(String expression) {
        // Regular expression to match valid arithmetic expressions
        return expression.matches("\\d+\\s*[\\+\\-\\*/%]\\s*\\d+");
    }
}
// + - * / %
// ++ -- += %= *= /= -=
//