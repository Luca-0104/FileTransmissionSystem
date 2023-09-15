package commandManagement;

import interfaces.INode;
import node.Sender;

import java.util.Scanner;

public class SenderCommandManager extends CommandManager {

    /**
     * Defined how the receiver listening to the command
     */
    @Override
    public void commandLineListening(INode node) {

        Scanner sc = new Scanner(System.in);
        boolean isQuit = false;
        while(!isQuit){

            System.out.print(">>> ");

            if (sc.hasNextLine()){
                // read in user input
                String line = sc.nextLine();

                // check quite
                if (line.equals("quit")){
                    isQuit = true;
                }else if(line.equals("tasks")){
                    node.printTaskList();
                }else{
                    // check input format
                    int separatorIndex = line.indexOf(" ");
                    if (separatorIndex != -1){
                        // separate the command and parameter
                        String[] elements = preprocessCommLine(line);
                        String command = elements[0];
                        String parameter = elements[1];

                        // check command exists
                        command = command.trim().toLowerCase();
                        if(COMM_SET.contains(command)){

                            // assign different services according to the command
                            if (command.equals(COMM_SEND)){
                                // parameter should be like "dir/file1.txt, dir/file2.txt"
                                // create a task using the parameter
                                ((Sender) node).createSendTask(parameter);

                            }else{
                                // assign the services according to command
                                assignService(node, command, parameter);

                            }

                        }else{
                            // if the command does not exist
                            System.out.println("--- no such command ---");
                        }

                    }else{
                        System.out.println("--- illegal input ---");
                    }
                }
            }

        }
    }

}
