import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        } else if (expression.contains("+") || expression.contains("-") || expression.contains("*")
                || expression.contains("/") || expression.contains("%")) {
            return "Arithmetic";   
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
            Pattern pattern = Pattern.compile("(\\w+)\\s*([+\\-*/%])\\s*(\\w+)");

            // Create matcher object
            Matcher matcher = pattern.matcher(expression);
            String operand1, operator, operand2;
            if (matcher.matches()) {
                // Extract the parts: operand1, operator, operand2
                 operand1 = matcher.group(1); // "i"
                 operator = matcher.group(2); // "*"
                 operand2 = matcher.group(3); // "n"

            } else {
                return null;
            }
            int value1;
            String Value1Type = new ExpressionTypeDetector().detectType(operand1);
           
            if (Value1Type.equals("Variable")) {
                value1 = (int) new Main().GetVariable(operand1);
            } else if (Value1Type.equals("Integer")) {
                value1 = Integer.parseInt(operand1);
            } else {
                return null;
            }
            int value2;
            String Value2Type = new ExpressionTypeDetector().detectType(operand2);
            if (Value2Type.equals("Variable")) {
                value2 = (int) new Main().GetVariable(operand2);
            } else if (Value2Type.equals("Integer")) {
                value2 = Integer.parseInt(operand2);
            } else {
                return null;
            }
            expression = value1 + " " + operator + " " + value2;
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
                    result = new Main().GetVariable(expression);
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
        if (num1 != null) {
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
            result = Math.round((float) result * 100) / 100;
        } else {
            if (!"".equals(operator)) {
                result = operator;
            }
        }
        return result;
    }

    public boolean HandleCondition(Object left, Object right, String operator) throws Exception {
        if (left instanceof Integer && right instanceof Integer) {
            int l = (int) left;
            int r = (int) right;
            switch (operator) {
                case "==":
                    return l == r;
                case "!=":
                    return l != r;
                case ">":
                    return l > r;
                case "<":
                    return l < r;
                case ">=":
                    return l >= r;
                case "<=":
                    return l <= r;
                default:
                    return false;
            }
        } else if (left instanceof String && right instanceof String) {
            String l = (String) left;
            String r = (String) right;
            switch (operator) {
                case "==":
                    return l.equals(r);
                case "!=":
                    return !l.equals(r);
                default:
                    return false;
            }
        } else {
            throw new Exception("Invalid condition");
        }
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