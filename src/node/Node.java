package node;

import interfaces.ICommand;
import interfaces.IMsgHandler;
import interfaces.INode;
import interfaces.IOperateTask;
import task.Task;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class Node implements INode {

    // IO streams
    ObjectOutputStream oos;
    ObjectInputStream ois;

    // task list
    Map<Integer, Task> taskMap;

    // command dependency (manage commands in the terminal command line)
    ICommand commandManager;

    // constructor
    public Node(ICommand cm){
        this.commandManager = cm;
        this.taskMap = new HashMap<>();
    }

    int getNextTaskId(){
        return taskMap.size() + 1;
    }

    @Override
    public void printTaskList() {
        System.out.println("=============================== Task List ===============================");
        // taskId, status, file number, finished file number
        System.out.println("Task ID         Status          file number         finished file number");
        System.out.println("-------------------------------------------------------------------------");
        // loop through all the tasks of this user
        for (Integer taskId : taskMap.keySet()){
            // get the task obj
            Task task = taskMap.get(taskId);
            String status = task.getStatus();
            int fileNumber = task.getTransmissionList().size();
            int finishedFileNumber = task.getFinishedFileNumber();
            // print basic info of this task
            System.out.printf("%-15d %-15s %-19d %d%n", taskId, status, fileNumber, finishedFileNumber);
        }
        System.out.println("-------------------------------------------------------------------------");
    }

    @Override
    public void queryTask(int taskId) {
        // check the user input
        if (taskMap.containsKey(taskId)){

            System.out.println("=============================== Query Task ===============================");
            System.out.println("------------------------------- TaskId: " + taskId + " -------------------------------");
            // filename, status, progress, progress%
            System.out.println("Status          finished/total (bytes)         Progress (%)         filename");
            System.out.println("-------------------------------------------------------------------------");

            // get the task by id
            Task task = taskMap.get(taskId);
            // query this task (task.query())
            task.query();
            System.out.println("-------------------------------------------------------------------------");
        }else{
            System.out.println("--- No such task with this ID! ---");
        }
    }

//    @Override
//    public void suspendTask(int taskId) {
//    }
//
//    @Override
//    public void resumeTask(int taskId) {
//    }
}
