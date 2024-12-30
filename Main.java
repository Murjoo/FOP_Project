import java.util.HashMap;
import java.util.Map;

class Main {

    public Map<String, Object> variables = new HashMap<>(); // Variable storage

    static Handler handleObj = new Handler();

    public static void main(String[] args) throws Exception {

        Main interpreter = new Main();
        System.out.println(handleObj.HandleVar("125"));
        // Example program: For loop
        String program = " n := 10\n" +
                " n += 10 \n" +
                " sum := n\n" +
                " sum += n\n" +
                " boole := true\n" +
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
        for (String line : lines) {
            line = line.trim();
            System.out.println("START OF LINE -- " + line);
            if (line.isEmpty())
                continue;
            System.out.println(variables);
            // Interpreting different type of words

            if (line.contains("for")) {

            } else if (line.contains(":=")) {
                InterpretAssignment(line);
            } else if (line.contains("+=") || line.contains("-=") || line.contains("*=") || line.contains("/=")
                    || line.contains("%=") || line.contains("++") || line.contains("--")) {
                InterpretArithmetic(line);
            } else if (line.startsWith("Println") || line.startsWith("Print")) {
                InterpretPrint(line);
            }
        }
    }

    // Handle assignment statements like "sum := 10 + 20 or x := 15"
    private void InterpretAssignment(String line) throws Exception {
        String[] parts = line.split(":=");
        String varName = parts[0].trim();
        String expression = parts[1].trim();

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
    // Handle print statements like "PRINT(sum)"

    private void InterpretPrint(String line) {
        // Syntax check
    }
}
