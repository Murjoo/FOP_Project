import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public Map<String, Object> variables = new HashMap<>(); // Variable storage

    int blockCounter = 0;
    static Handler handleObj = new Handler();

    public static void main(String[] args) throws Exception {

        Main interpreter = new Main();
        System.out.println(handleObj.HandleVar("125"));
        // Example program: For loop
        String program = " n := 10\n" +
                " n = 5 \n" +
                " sum := n\n" +
                " sum += n\n" +
                " boole := true\n" +
                " if n > 4 { \n" +
                "   n *= 2" +
                " } \n" +
                " theString := \"Hello!\"\n" +
                " for i := 1; i <= n; i++ {\n" +
                "     sum += i\n" +
                " }\n" +
                " Println(sum)";
        interpreter.eval(program);
    }

    // Method to evaluate the code
    public void eval(String code) throws Exception {
        String[] lines = code.split("\n"); // Split by lines
        List<String> blocks = extractBlocks(code); // Extract blocks
        for (String line : lines) {
            line = line.trim();
            System.out.println("START OF LINE -- " + line);
            if (line.isEmpty())
                continue;
            System.out.println(variables);
            // Interpreting different type of words
            if (line.contains("if")) {
                InterpretIf(line);
            } else if (line.contains("for")) {
                InterpretFor(line);
            } else if (line.contains(":=")) {
                InterpretAssignment(line, false);
            } else if (isSimpleAssignment(line)) {
                InterpretAssignment(line, true);
            } else if (line.contains("+=") || line.contains("-=") || line.contains("*=") || line.contains("/=")
                    || line.contains("%=") || line.contains("++") || line.contains("--")) {
                InterpretArithmetic(line);
            } else if (line.startsWith("Println") || line.startsWith("Print")) {
                InterpretPrint(line);
            }
        }
    }
    
    private boolean isSimpleAssignment(String line) {
        // Regular expression for simple assignments
        // Matches: <variable> = <expression>, excludes: +=, -=, *=, /=, etc.
        String regex = "^\\s*\\w+\\s*=\\s*[^=+*/<>!]+\\s*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }
    // Handle assignment statements like "sum := 10 + 20 or x := 15"
    private void InterpretAssignment(String line, boolean assign) throws Exception {
        String[] parts;
        String varName;
        String expression;
        if (assign) {
           if (isSimpleAssignment(line)){
                parts = line.split("=");
                varName = parts[0].trim();
                expression = parts[1].trim();
           } else {
                throw new Exception("Syntax error, assignment should be in the form of <variable> = <expression>");
           }
        } else {
            parts = line.split(":=");
            varName = parts[0].trim();
            expression = parts[1].trim();
        }
       

        if ((variables.containsKey(varName) && !assign) || (!variables.containsKey(varName) && assign)) {
            throw new Exception("Variable " + varName + " is already defined");
        }

        // Evaluate the expression, if there's one (addition, subtraction,
        // multiplication)
        Object value = handleObj.HandleVar(expression);
        if (value != null) {
            if (value == "x12o4j2145opp1p2_22mdmdmmda2144") {
                variables.put(varName, variables.get(expression));
            } else {
                variables.put(varName, value);
            }
        } else {
            throw new Exception("Declaration error, variable " + varName + " is not defined correctly");
        }
    }

    private void InterpretArithmetic(String line) throws Exception {
        // Syntax check
        String[] parts;
        String Operator;
        switch (line.contains("+=") ? 1
                : line.contains("-=") ? 2
                        : line.contains("*=") ? 3
                                : line.contains("/=") ? 4 : line.contains("%=") ? 5 : line.contains("++") ? 6 : 7) {
            case 1:
                parts = line.split("\\+=");
                Operator = "+";
                break;
            case 2:
                parts = line.split("\\-=");
                Operator = "-";
                break;
            case 3:
                parts = line.split("\\*=");
                Operator = "*";
                break;
            case 4:
                parts = line.split("\\/=");
                Operator = "/";
                break;
            case 5:
                parts = line.split("\\%=");
                Operator = "%";
                break;
            case 6:
                parts = line.split("\\+\\+");
                Operator = "++";
                break;
            case 7:
                parts = line.split("\\-\\-");
                Operator = "--";
                break;
            default:
                throw new AssertionError();
        }
        String varName = parts[0].trim();
        String expression = parts[1].trim();
        int newValue;
        if (Operator == "++" || Operator == "--") {
            if (expression != null){
                throw new Exception("Syntax error, \"" + Operator + "\" operator should not have any value");
            } else {
                newValue = 1;
            }
        } else {
            String expType = new ExpressionTypeDetector().detectType(expression);
            if (expType == "Integer"){
                newValue = Integer.parseInt(expression);
            } else if (expType == "Variable"){
                newValue = (int) variables.get(expression);
            } else {
                throw new Exception("Syntax error, \"" + varName + "\" variable is not defined correctly");
            }
        }
        Object varValue = variables.get(varName);
        System.out.println(varValue);
        if (varValue == null || !(varValue instanceof Integer)) {
            throw new Exception("Variable " + varName + " is not defined correctly");
        }
        varValue = handleObj.Arithmetic(varValue, newValue, Operator);
        System.out.println("NEW VALUE OF " + varName + " -- " + varValue);
        variables.put(varName, varValue);
    }
 
    private void InterpretFor(String line) throws Exception {
        
    }
    private void InterpretIf(String line) throws Exception {
        // Syntax check
        if (!line.contains("{")) {
            throw new Exception("Syntax error, \"{\" should be on a same line");
        }
        String condition = line.replace("if", "").replace("{", "").trim(); // Clean up the string
        Pattern pattern = Pattern.compile("(\\w+)\\s*(<=|>=|==|!=|<|>)\\s*(\\w+)");
        Matcher matcher = pattern.matcher(condition);

        if (matcher.find()) {
            String leftOperand = matcher.group(1);  // First part (variable)
            String operator = matcher.group(2);    // Operator
            String rightOperand = matcher.group(3); // Second part (value)
            Object leftValue;
            String leftType = new ExpressionTypeDetector().detectType(leftOperand);
            if (leftType == "Variable") {
                leftValue = variables.get(leftOperand);
            } else if (leftType == "Integer") {
                leftValue = Integer.parseInt(leftOperand);
            } else {
                leftValue = leftOperand;
            }
            Object rightValue;
            String rightType = new ExpressionTypeDetector().detectType(rightOperand);
            if (rightType == "Variable") {
                rightValue = variables.get(rightOperand);
            } else if (rightType == "Integer") {
                rightValue = Integer.parseInt(rightOperand);
            } else {
                rightValue = rightOperand;
            }
            boolean result = handleObj.HandleCondition(leftValue, rightValue, operator);
        } else {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
    }

    private static List<String> extractBlocks(String code) {
        List<String> blocks = new ArrayList<>();
        StringBuilder currentBlock = new StringBuilder();
        int bracketCount = 0;

        String[] lines = code.split("\\n");

        for (String line : lines) {
            line = line.trim();
            currentBlock.append("\n");
            for (char ch : line.toCharArray()) {
                if (ch == '{') {
                    if (bracketCount > 0) {
                        currentBlock.append(ch);
                    }
                    bracketCount++;
                } else if (ch == '}') {
                    bracketCount--;
                    if (bracketCount > 0) {
                        currentBlock.append(ch);
                    } else if (bracketCount == 0) {
                        blocks.add(currentBlock.toString().trim());
                        currentBlock.setLength(0);
                    }
                } else if (bracketCount > 0) {
                    currentBlock.append(ch);
                }
            }
        }

        if (bracketCount != 0) {
            throw new IllegalStateException("Mismatched brackets in the code.");
        }

        return blocks;
    }
    // Handle print statements like "Println(10)" or "Println(x)"
    private void InterpretPrint(String line) {
        // Syntax check
    }
}
