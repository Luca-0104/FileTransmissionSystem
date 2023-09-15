package interfaces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * This is defined by Node package, and should be implemented by presentation classes
 * (prompts and operations in the command line)
 */
public interface ICommand {

    // possible commands in terminal
    public final static String COMM_SEND = "send";
    public final static String COMM_QUERY = "query";
    public final static String COMM_SUSPEND = "suspend";
    public final static String COMM_RESUME = "resume";
    public final static String[] COMM_ARR = new String[]{COMM_SEND, COMM_QUERY, COMM_SUSPEND, COMM_RESUME};
    public final static Set<String> COMM_SET = new HashSet<>(Arrays.asList(COMM_ARR));

    // methods related to the terminal operations
    public abstract void commandLineListening(INode node);
}
