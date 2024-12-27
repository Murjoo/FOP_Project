import java.util.HashMap;
import java.util.Map;

class Main {

    private final Map<String, Object> variables = new HashMap<>(); // Variable storage

    static Handler handleObj = new Handler();
    public static void main(String[] args) {
        
        Main interpreter = new Main();
     
        // Example program: For loop
        String program = " n := 10\n" + 
                        " sum := 0\n" + 
                        " for i := 1; i <= n; i++ {\n" + 
                        "     sum += i\n" + 
                        " }\n" + 
                        " Println(sum)";
        interpreter.eval(program);
    }

    // Method to evaluate the code
    public void eval(String code) {
        String[] lines = code.split("\n"); // Split by lines
        for (String line : lines) {
            line = line.trim();
            System.out.println("START OF LINE -- " + line);
            if (line.isEmpty()) continue;

            // Interpreting different type of words
            if (line.contains("for")){

            }
            else if (line.contains("while")) {
                
            }
            else if (line.contains(":=")) {
                InterpretAssignment(line);
            }
            else if (line.startsWith("Println") || line.startsWith("Print")) {
                InterpretPrint(line);
            }
        }
    }

    // Handle assignment statements like "sum := 10 + 20 or x := 15"
    private void InterpretAssignment(String line) {
        String[] parts = line.split(":=");
        String varName = parts[0].trim();
        String expression = parts[1].trim();

        // Evaluate the expression (addition, subtraction, multiplication)
        Object value = evaluateExpression(expression);
        if (expression != null) {
           variables.put(varName, value);   
        }
    }

    // Method to evaluate expressions (addition, subtraction, multiplication)
    private Object evaluateExpression(String expression) {
        if (!(expression.contains("+")) || !(expression.contains("-")) || !(expression.contains("*")) || !(expression.contains("/")) || !(expression.contains("%")) ){
            System.out.println("Read variable");
            // Here will be syntax check
            return expression;
        } else {
            
        }
        return null;
    }

    // Handle print statements like "PRINT(sum)"
    private void InterpretPrint(String line) {
        //Syntax check
    }
}
