import java.util.Iterator;
import java.util.LinkedList;
public class Executor {
    private static LinkedList<String> commands = new LinkedList<>();
    public void AddToExecute(String command, boolean nextLine) {
        // Add command to the list of commands to be executed
        if (nextLine){
            command += "\n";
        }
        commands.push(command);
    }
    public void Execute() {
        if (commands.isEmpty()) {
            // No commands to execute
            return;
        }
        Iterator<String> iterator = commands.descendingIterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next());
        }
    }
}
