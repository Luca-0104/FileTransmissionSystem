package node;

import interfaces.IAnnounceSender;
import interfaces.IStatus;
import message.Message;
import message.MessageHandler;
import commandManagement.SenderCommandManager;
import task.SendTask;
import task.Task;
import transmission.Transmission;
import transmission.UploadTrans;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Sender extends Node implements IAnnounceSender {

    // connection
    private ServerSocket serverSocket;      // self
    private Socket socket;                  // connected client (for msg)

    // constructor
    public Sender(SenderCommandManager scm, ServerSocket serverSocket){
        super(scm);
        this.serverSocket = serverSocket;
    }

    @Override
    public void start() {
        System.out.println("--- sender (server) started ---");

        // listen to the connection from clients (receivers)
        try {

            this.socket = this.serverSocket.accept();

            System.out.println("--- a receiver connected ! ---");

            // init IO streams for msg
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());

            // a new tread for keeping listening to the msg
            new Thread(new MessageHandler(ois, this)).start();

            // listen to the user input
            commandManager.commandLineListening(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createSendTask(String filesStr) {
        // split the multiple complete file names
        String[] completeFileNames = filesStr.split(",");

        // create a transmission list for the new task
        ArrayList<Transmission> transList = new ArrayList<>();

        // go through to create each file (transmission) to fill the trans list
        for(String filePath : completeFileNames){
            filePath = filePath.trim();
            // create a file obj
            File file = new File(filePath);
            if (file.exists()){
                // create the transmission obj
                UploadTrans uploadTrans = new UploadTrans(file);
                // add it to list
                transList.add(uploadTrans);

            }else{
                System.out.println("--- File does not exist: " + filePath + " ---");
            }
        }

        // create the new task using trans list
        if (transList.size() > 0){
            SendTask task = new SendTask(transList);
            task.setId(getNextTaskId());
            // add this task to the task list of this users
            taskMap.put(task.getId(), task);
            // tell the receiver we will start the task
            announceStartTask(task);

            // show prompt
            System.out.println("--- task created! ---");

        }else{
            System.out.println("--- None of the file path available, task generation failed! ---");
        }

    }

    public void startSendTask(int taskId, boolean isResume){
        // get task from map
        SendTask task = (SendTask) taskMap.get(taskId);

        // check if we need to resume the task (status)
        if(isResume){
            task.resume();
            System.out.println("--- Task resumed! ---");
        }

        // start this task, which would create new threads for each file inside it.
        task.startSendTask(serverSocket, isResume);

    }

    /**
     * The suspendTask method should be override in sender class.
     * The sender would set the status of every trans in the task as "suspended".
     * Then the output stream in socket would come to the end.
     */
    @Override
    public void suspendTask(int taskId) {
        // check if the task exists
        if (taskMap.containsKey(taskId)){
            // get task by id
            Task task = taskMap.get(taskId);

            // check the status of task
            if (task.getStatus().equals(IStatus.STATUS_ONGOING)){
                // suspend the task
                task.suspend();
                System.out.println("--- Task suspended! ---");
            }else{
                System.out.println("--- Only the ongoing tasks can be suspended! ---");
            }

        }else{
            System.out.println("--- No such task with this ID! ---");
        }
    }

    /**
     * If a sender want to resume a task, it should tell the receiver, "I am going to resume a task".
     * Then the receiver will create new sockets for the trans in this task.
     * After getting the RESUME_ACK, the sender can start to accept sockets and send.
     */
    @Override
    public void resumeTask(int taskId) {
        // check if the task exists
        if (taskMap.containsKey(taskId)){
            // get task by id
            Task task = taskMap.get(taskId);

            // check the status of task
            if (task.getStatus().equals(IStatus.STATUS_SUSPENDED)){

                // tell the receiver to start the receiving
                announceResumeTask(taskId);

            }else{
                System.out.println("--- Only the suspended tasks can be resumed! ---");
            }

        }else{
            System.out.println("--- No such task with this ID! ---");
        }
    }


    // ------------------------------------------------------- announce -------------------------------------------------------

    @Override
    public void announceStartTask(SendTask sendTask){
        // tell the receiver we will start a task
        Message msg = new Message(Message.START_TASK, sendTask);
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void announceResumeTask(int taskId) {
        // tell the receiver we will start a task again
        Message msg = new Message(Message.RESUME_TASK, taskId);
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
