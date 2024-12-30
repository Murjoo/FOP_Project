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
            return "Variable";
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
    public Object Arithmetic(Object Num1, int Num2, String Action) { // Handling Arithmetic equations
        Object result;
        int n1 = (int) Num1;
        int n2 = Num2;
        switch (Action) { // Different type of equations
            case "+":
                result = n1 + n2;
                break;
            case "-":
                result = n1 - n2;
                break;
            case "*":
                result = n1 * n2;
                break;
            case "%":
                result = n1 % n2;
                break;
            case "/":
                if (n2 == 0) {
                    return null;
                }
                result = n1 / n2;
                break;
            default:
                result = null;
        }
        return result;
    }

    // Syntax check for variable declarations/assignments
    public Object HandleVar(String expression) {
        // Check if the expression contains an operator
        if (expression.contains("+") || expression.contains("-") || expression.contains("*") || expression.contains("/")
                || expression.contains("%")) {
            // Process arithmetic expression
            if (!isValidExpression(expression)) {
                return null;
            }
            // Calculation
            return calculate(expression);
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
                case "Variable":
                    result = "x12o4j2145opp1p2_22mdmdmmda2144";
                    break;
                default:
                    throw new AssertionError();
            }
            return result;
        }
    }

    private Object calculate(String expression) {
        Object result = null;
        String operator = "";
        Float num1 = null;
        Float num2 = null;
        boolean isAdding = false;
        boolean waitingForOperator = false;
        int index = 0;
        for (char c : expression.toCharArray()) {
            if (result != null) {
                return null;
            }
            if (c == ' ') {
                if (index == -1) {
                    operator += c;
                } else if ("".equals(operator)) {
                    waitingForOperator = true;
                }
                continue;
            }
            if (waitingForOperator) {
                if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                    if (index == -1) {
                        operator += c;
                        continue;
                    } else {
                        System.out.println(num1);
                        if (num1 != null) {
                            if (num2 != null) {
                                switch (c) {
                                    case '+':
                                        result = num1 + num2;
                                        break;
                                    case '-':
                                        result = num1 - num2;
                                        break;
                                    case '*':
                                        result = num1 * num2;
                                        break;
                                    case '/':
                                        result = num1 / num2;
                                        break;
                                    case '%':
                                        result = num1 % num2;
                                        break;
                                }
                                num2 = 0F;
                            }
                            operator = String.valueOf(c);
                            waitingForOperator = false;
                            isAdding = true;
                            index = 0;
                            continue;
                        } else {
                            if (!"".equals(operator) || !"+".equals(operator) || !"-".equals(operator)
                                    || !"*".equals(operator)
                                    || !"/".equals(operator) || !"%".equals(operator)) {
                                operator = String.valueOf(c);
                                waitingForOperator = false;
                                index = 0;
                                continue;
                            } else {
                                return null;
                            }
                        }
                    }
                } else {
                    return null;
                }
            }
            if (c == '"' || c == '\'') {
                // String
                if (index == 0) {
                    if ((isAdding && operator != "") || operator == "") {
                        index = -1;
                        continue;
                    } else {
                        return null;
                    }
                } else {
                    if (index == -1) {
                        if (operator.charAt(0) == c) {
                            operator += c;
                        } else {
                            index = 0;
                        }
                    } else {
                        return null;
                    }
                }
            } else {
                if (index == -1) {
                    operator += c;
                    continue;
                }
                if (Character.isDigit(c)) {
                    if (index == 0) {
                        if (num1 == null) {
                            num1 = Float.valueOf(String.valueOf(c));
                            index++;
                            continue;
                        } else {
                            if (isAdding) {
                                num2 = Float.valueOf(String.valueOf(c));
                                index++;
                                continue;
                            } else {

                                return null;
                            }
                        }
                    } else if (index > 0) {
                        if (num2 == null) {
                            num1 = num1 * 10 + Float.parseFloat(String.valueOf(c));
                            index++;
                            continue;
                        } else {
                            num2 = num2 * 10 + Float.parseFloat(String.valueOf(c));
                            index++;
                            continue;
                        }
                    } else {
                        return null;
                    }
                } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '%') {
                    if (!"".equals(operator)) {
                        if (operator.equals(String.valueOf(c)) && ("+".equals(operator) || "-".equals(operator))) {
                            if ("+".equals(operator)) {
                                num1++;
                            } else {
                                num1--;
                            }
                            result = num1;
                            continue;
                        } else {
                            if (c == '+') {
                                index = 0;
                                isAdding = true;
                                continue;
                            } else {
                                return null;
                            }
                        }
                    } else {
                        operator = String.valueOf(c);
                        waitingForOperator = false;
                        isAdding = true;
                        index = 0;
                        continue;
                    }
                } else {
                    return null;
                }
            }
        }
        if (result != null) {
            return result;
        }
        if (num1 != null){
            if (num2 != null) {
                if (!"+".equals(operator) && !"-".equals(operator) && !"*".equals(operator) && !"/".equals(operator)
                        && !"%".equals(operator)) {

                    return null;
                }
                switch (operator) {
                    case "+" -> result = num1 + num2;
                    case "-" -> result = num1 - num2;
                    case "*" -> result = num1 * num2;
                    case "/" -> result = num1 / num2;
                    case "%" -> result = num1 % num2;
                }
            } else {
                if (!"".equals(operator)) {
                    return null;
                }
                result = num1;
            }
        } else {
            if (!"".equals(operator)) {
                result = operator;
            }
        }
        return result;
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