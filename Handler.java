public class Handler {
    public String Arithmetic(String Num1, String Num2, String Action){ //Handling Arithmetic equations
        try { //Checking corectness of numbers
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
                result = String.valueOf(n1+n2);
                break;
            case "-":
                result = String.valueOf(n1-n2);
                break;
            case "*":
                result = String.valueOf(n1*n2);
                break;
            case "%":
                result = String.valueOf(n1%n2);
                break;
            case "/":
                result = String.valueOf(n1/n2);
                break;
            default:
                result = "Error";
        }
        return result;
    }
}
// + - * / % 
// ++ -- += %= *= /= -=
//