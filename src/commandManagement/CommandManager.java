package commandManagement;

import interfaces.INode;
import interfaces.ICommand;

public abstract class CommandManager implements ICommand {

    /**
     * Get a line from terminal command line, then separate the command string and parameter string.
     * @param line A whole String gotten from command line
     * @return An array contains [command, parameter]
     */
    String[] preprocessCommLine(String line) {
        // separate the command and parameter
        String command = line.substring(0, line.indexOf(" "));
        String parameter = line.substring(line.indexOf(" ") + 1);

        return new String[]{command, parameter};
    }

    /**
     * Assign the service according to the command.
     * @param node The node (Sender or Receiver)
     * @param command The command gotten from the command line
     * @param parameter The parameter gotten from the command line
     */
    void assignService(INode node, String command ,String parameter){
        // assign different services according to the command
        // for other commands, the parameter should be task id like "1", "2" ...
        // parse the string parameter to the int task id
        parameter = parameter.trim();

        try{
            // parse
            int taskId = Integer.parseInt(parameter);

            // assign services
            switch (command) {
                case COMM_QUERY:
                    // query this task
                    node.queryTask(taskId);
                    break;

                case COMM_SUSPEND:
                    // suspend this task
                    node.suspendTask(taskId);
                    break;

                case COMM_RESUME:
                    // resume this task
                    node.resumeTask(taskId);
                    break;
            }

        } catch (NumberFormatException e){
            System.out.println("--- You should use int number as task ID ---");
        }
    }
}
