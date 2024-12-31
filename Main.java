import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Main {

    public static Map<String, Object> variables = new HashMap<>(); // Variable storage
    List<String> blocks;
    public static ArrayList<Integer> blockLines = new ArrayList<>();
    int blockCounter = 0;
    static Handler handleObj = new Handler();

    public static void main(String[] args) throws Exception {

        Main interpreter = new Main();
        Scanner scanner = new Scanner(System.in);

        // Inform the user to paste the code
        System.out.println("Paste your Go code. Press Ctrl+D (Linux/Mac) or Ctrl+Z (Windows) to finish:");

        StringBuilder program = new StringBuilder();

        // Read lines until the user presses Ctrl+D (Linux/Mac) or Ctrl+Z (Windows) to stop
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            // Append the line to the program and add a newline
            program.append(line).append("\n");
        }


        interpreter.eval(program.toString());
        new Executor().Execute();
    }

    // Method to evaluate the code
    public void eval(String code) throws Exception {
        Stack<String> varStack = new Stack<>();
        String[] lines = code.split("\n"); // Split by lines
        List<String> oldBlocks = blocks;
        ArrayList<Integer> oldblockLines = new ArrayList<>(blockLines);
        int oldBlockCounter = blockCounter;
        blockCounter = 0;
        blockLines.clear();
        blocks = extractBlocks(code); // Extract blocks
        String ifCondition = "None";
        int lineCounter = 0;
        for (String line : lines) {

            lineCounter++;
            line = line.trim();

            if (line.isEmpty() || IsInBlock(line, lineCounter))
                continue;

            if (!line.contains("if") && !line.contains("else")) {
                ifCondition = "None";
            }

            // Interpreting different type of words
            if (line.contains("if")) {
                ifCondition = String.valueOf(InterpretIf(line));
                blockCounter++;
            } else if (line.contains("else")) {
                if (ifCondition == "false") {
                    eval(blocks.get(blockCounter));
                } else if (ifCondition != "true") {
                    throw new Exception("Syntax error, \"else\" should be used after an \"if\" statement");
                }
                ifCondition = "None";
                blockCounter++;
            } else if (line.contains("for")) {
                InterpretFor(line);
                blockCounter++;
            } else if (line.contains(":=")) {
                String varName = InterpretAssignment(line, false);
                varStack.push(varName);
            } else if (isSimpleAssignment(line)) {
                InterpretAssignment(line, true);
            } else if (line.contains("+=") || line.contains("-=") || line.contains("*=") || line.contains("/=")
                    || line.contains("%=") || line.contains("++") || line.contains("--")) {
                InterpretArithmetic(line);
            } else if (line.startsWith("Println") || line.startsWith("Print")) {
                InterpretPrint(line);
            }
        }
        for (String varName : varStack) {
            variables.remove(varName);
        }
        blockLines.clear();
        blockLines = oldblockLines;
        blocks = oldBlocks;
        blockCounter = oldBlockCounter;
        varStack.clear();
    }

    private boolean isSimpleAssignment(String line) {
        // Regular expression for simple assignments
        // Matches: <variable> = <expression>, excludes: +=, -=, *=, /=, etc.
        String regex = "^\\s*\\w+\\s*=\\s*[^=+*/<>!]+\\s*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        return matcher.matches();
    }

    private boolean IsInBlock(String line, int curLine) {
        line = line.replace('{', ' ').replace('}', ' ').trim();
        int bc = 0;
        for (String block : blocks) {
            if (block.contains(line) && blockLines.get(bc * 2) < curLine && blockLines.get(bc * 2 + 1) > curLine) {
                return true;
            }
            bc++;
        }
        return false;
    }

    // Handle assignment statements like "sum := 10 + 20 or x := 15"
    private String InterpretAssignment(String line, boolean assign) throws Exception {
        String[] parts;
        String varName;
        String expression;
        if (assign) {
            if (isSimpleAssignment(line)) {
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
        return varName;
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
            if (expression != null) {
                throw new Exception("Syntax error, \"" + Operator + "\" operator should not have any value");
            } else {
                newValue = 1;
            }
        } else {
            String expType = new ExpressionTypeDetector().detectType(expression);
           
            newValue = (int) handleObj.HandleVar(expression);
            // if (expType == "Integer") {
            //     newValue = Integer.parseInt(expression);
            // } else if (expType == "Variable") {
            //     newValue = (int) variables.get(expression);
            // } else if (expType == "Arithmetic") {
            //     newValue = (int) handleObj.HandleVar(expression);
            // } else {
            //     throw new Exception("Syntax error, \"" + varName + "\" variable is not defined correctly");
            // }
        }
        Object varValue = variables.get(varName);
        if (varValue == null || !(varValue instanceof Integer)) {
            throw new Exception("Variable " + varName + " is not defined correctly");
        }
        varValue = handleObj.Arithmetic(varValue, newValue, Operator);
        variables.put(varName, varValue);
    }
    private void InterpretFor(String line) throws Exception {
        if (line.contains("for")) {
            // Handle `for` loop
            if (line.contains(";")) {

                // Split the string by semicolon (;)
                String[] parts = line.split(";");

                // Process each part after splitting by semicolon

                // Remove "for" and trim any extra spaces
                parts[0] = parts[0].replace("for", "").replace("{", "").trim();
            
                // Split the part by "=" but preserve the colon
                String[] splitByEqual1 = parts[0].split("=");// this is i:
            
                int S = Integer.parseInt(splitByEqual1[1].trim()); // parse i value to int

                String condition = parts[1].trim(); // Clean up the string
                Pattern pattern = Pattern.compile("(\\w+)\\s*(<=|>=|==|!=|<|>)\\s*(\\w+)");
                Matcher matcher = pattern.matcher(condition);
                String splitByEqual2,x,rightOperand;
                int y;
                if (matcher.find()) {
                    splitByEqual2 = matcher.group(1); // First part (variable)
                    x = matcher.group(2); // Operator
                    rightOperand = matcher.group(3); // Second part (value)
                } else {
                    throw new IllegalArgumentException("Invalid condition: " + condition);
                }
                String rightType =new ExpressionTypeDetector().detectType(rightOperand);
                if (rightType == "Variable") {
                    y = (int) variables.get(rightOperand);
                } else if (rightType == "Integer") { 
                    y = Integer.parseInt(rightOperand);
                } else {
                    throw new IllegalArgumentException("Invalid condition: " + condition);
                }
           
                if (splitByEqual1[0].contains(":")) {
                    InterpretAssignment(parts[0], false);
                } else {
                    InterpretAssignment(parts[0], true);
                }

                String varName = splitByEqual1[0].replace(":", "").trim();

                // Step 3: Loop logic based on the comparison operator
                if (x.equals("<")) {
                    for (int i = S; i < y; i++) { // Increment i in the loop
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                    }
                } else if (x.equals("<=")) {
                    for (int i = S; i <= y; i++) {
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                    }
                } else if (x.equals(">")) {
                    for (int i = S; i > y; i--) { // Decrement i in the loop
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                    }
                } else if (x.equals(">=")) {
                    for (int i = S; i >= y; i--) { // Decrement i in the loop
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                    }
                } else {
                    throw new Exception("Unsupported comparison operator.");
                }
            } else { // Handle `while` loop
                
                // Extract values from the parts array
                String condition = line.replace("for", "").replace("{", "").trim();
                Pattern pattern = Pattern.compile("(\\w+)\\s*(<=|>=|==|!=|<|>)\\s*(\\w+)");
                Matcher matcher = pattern.matcher(condition);
                String varName,operator;
                int limit;
                if (matcher.find()) {
                    varName = matcher.group(1); // First part (variable)
                    operator = matcher.group(2); // Operator
                    limit = Integer.parseInt(matcher.group(3)); // Second part (value)
                } else {
                    throw new IllegalArgumentException("Invalid condition: " + condition);
                }
                Object varValue = variables.get(varName);
                if (varValue == null) {
                    throw new IllegalArgumentException("Variable " + varName + " is not defined.");
                }
                int i = (int) varValue; // Get the value of the variable
                // Process the comparison and loop accordingly
                if (operator.equals("<")) {
                    while (i < limit) {  // Loop while i < 5
                        i = (int) variables.get(varName);
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                    }
                } else if (operator.equals("<=")) {
                    while (i <= limit) {  // Loop while i <= 5
                        i = (int) variables.get(varName);
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                        i = (int) variables.get(varName);
                    }
                } else if (operator.equals(">")) {
                    while (i > limit) {  // Loop while i > 5
                        i = (int) variables.get(varName);
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                        i = (int) variables.get(varName);
                    }
                } else if (operator.equals(">=")) {
                    while (i >= limit) {  // Loop while i >= 5
                        i = (int) variables.get(varName);
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                        i = (int) variables.get(varName);
                    }
                } else if (operator.equals("==")) {
                    while (i == limit) {  // Loop while i == 5
                        i = (int) variables.get(varName);
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                        i = (int) variables.get(varName);
                    }
                } else if (operator.equals("!=")) {
                    while (i != limit) {  // Loop while i != 5
                        i = (int) variables.get(varName);
                        InterpretAssignment(varName + "="+i, true);
                        eval(blocks.get(blockCounter));
                        i = (int) variables.get(varName);
                    }
                }
            }

        }
    }

    private boolean InterpretIf(String line) throws Exception {
        // Syntax check
        if (!line.contains("{")) {
            throw new Exception("Syntax error, \"{\" should be on a same line");
        }
       
        String condition = line.replace("if", "").replace("{", "").trim(); // Clean up the string
        String[] operands = condition.split("(?<=[<>=!]=|<|>|=])|(?=[<>=!]=|<|>|=])");


        String leftOperand = "";  // First part (variable)
        String operator = null; // Operator
        String rightOperand = ""; // Second part (value)
        for (String operand : operands) {
            operand = operand.trim(); // Trim any extra spaces
            // If the operand matches a comparison operator, set it as the operator
            if (operand.matches("<=|>=|==|!=|<|>")) {
                operator = operand;
            } else {
                // Otherwise, assign it as the left or right operand
                if (operator == null) {
                    leftOperand += operand;
                } else {
                    rightOperand = operand;
                }
            }
        }
        if (leftOperand != "" && operator != null && rightOperand != "") {
           
            Object leftValue;
            String leftType = new ExpressionTypeDetector().detectType(leftOperand);
            
            if (leftType == "Variable") {
                leftValue = variables.get(leftOperand);
            } else if (leftType == "Integer") {
                leftValue = Integer.parseInt(leftOperand);
            } else if (leftType == "Arithmetic") {
                leftValue = handleObj.HandleVar(leftOperand);
            } else {
                leftValue = leftOperand;
            }
            Object rightValue;
            String rightType = new ExpressionTypeDetector().detectType(rightOperand);
            if (rightType == "Variable") {
                rightValue = variables.get(rightOperand);
            } else if (rightType == "Integer") {
                rightValue = Integer.parseInt(rightOperand);
            } else if (rightType == "Arithmetic") {
                rightValue = handleObj.HandleVar(rightOperand);
            } else {
                rightValue = rightOperand;
            }
            boolean result = handleObj.HandleCondition(leftValue, rightValue, operator);
            if (result) {
                eval(blocks.get(blockCounter));

            }
            return result;
        } else {
            throw new IllegalArgumentException("Invalid condition: " + condition);
        }
    }

    private static List<String> extractBlocks(String code) {
        List<String> blocks = new ArrayList<>();
        StringBuilder currentBlock = new StringBuilder();
        int bracketCount = 0;
        int curBlockCounter = 0;

        String[] lines = code.split("\\n");
        int lineCounter = 0;
        for (String line : lines) {
            lineCounter++;
            line = line.trim();
            currentBlock.append("\n");
            for (char ch : line.toCharArray()) {
                if (ch == '{') {
                    if (bracketCount > 0) {
                        currentBlock.append(ch);
                    } else {
                        blockLines.add(2 * curBlockCounter, lineCounter);
                    }
                    bracketCount++;
                } else if (ch == '}') {
                    bracketCount--;
                    if (bracketCount > 0) {
                        currentBlock.append(ch);
                    } else if (bracketCount == 0) {
                        blocks.add(currentBlock.toString().trim());
                        currentBlock.setLength(0);
                        blockLines.add(2 * curBlockCounter + 1, lineCounter);
                        curBlockCounter++;
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

    public Object GetVariable(String varName) {
        return variables.get(varName);
    }
    // Handle print statements like "Println(10)" or "Println(x)"
    private void InterpretPrint(String line) throws Exception {
        // Regular expression to match valid syntax for Print or Println
        Pattern pattern = Pattern.compile("^(Print|Println)\\((.+)\\)$");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            // Extract content inside parentheses
            boolean isPrintln = line.startsWith("Println");
            Pattern contentPattern = Pattern.compile("\\((.*?)\\)");
            Matcher contentMatcher = contentPattern.matcher(line);

            if (contentMatcher.find()) {
                String content = contentMatcher.group(1);
                if (content.startsWith("\"") && content.endsWith("\"")) {
                    // Give executor the code to execute
                    new Executor().AddToExecute(content.substring(1, content.length() - 1), isPrintln);
                } else {
                    // Print the value of the variable
                    Object value = variables.get(content);
                    if (value != null) {
                        new Executor().AddToExecute(String.valueOf(value).replace("\"", "").trim(), isPrintln);
                    } else {
                        throw new Exception("Error: Variable " + content + " is not defined.");
                    }
                }
            }
        } else {
           throw new Exception("Error: Invalid syntax.");
        }
    }
}
