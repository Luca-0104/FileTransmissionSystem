package node;

import interfaces.IAnnounceReceiver;
import interfaces.IConnection;
import interfaces.IStatus;
import message.Message;
import message.MessageHandler;
import commandManagement.ReceiverCommandManager;
import task.ReceiveTask;
import task.SendTask;
import task.Task;
import transmission.DownloadTrans;
import transmission.Transmission;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Receiver extends Node implements IAnnounceReceiver, IConnection {

    // socket connection (for msg)
    private Socket socket;

    // directory for receiving files
    private static final String RECEIVE_DIR = "src/testFiles/testReceiveDir/";

    // constructor
    public Receiver(ReceiverCommandManager rcm, Socket socket){
        super(rcm);
        this.socket = socket;
    }

    @Override
    public void start() {
        System.out.println("--- receiver (client) started ---");

        try {

            // init IO streams for msg
            this.oos = new ObjectOutputStream(this.socket.getOutputStream());
            this.ois = new ObjectInputStream(this.socket.getInputStream());

            // a new tread for keeping listening to the msg
            new Thread(new MessageHandler(ois, this)).start();

            // keep listen the command from terminal
            commandManager.commandLineListening(this);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create the receiveTask according to the sendTask
     * @param sendTask The sendTask that need to be used to create the corresponding receiveTask
     */
    public void createReceiveTask(SendTask sendTask) {

        // create a transmission list for the new receiveTask
        ArrayList<Transmission> transList = new ArrayList<>();

        // go through the trans list of that sendTask
        for(Transmission uploadTrans : sendTask.getTransmissionList()){

            // get the file information
            String filename = uploadTrans.getFile().getName();
            long fileLength = uploadTrans.getFile().length();

            // create a file obj
            String filePath = RECEIVE_DIR + filename;
            File newFile = new File(filePath);

            // create the transmission obj
            DownloadTrans downloadTrans = new DownloadTrans(newFile, fileLength);
            // add it to list
            transList.add(downloadTrans);
        }

        // create the task using this trans list
        if (transList.size() > 0){
            ReceiveTask task = new ReceiveTask(transList);
            // NOTICE: here, we make sure the receiveTask id is same as the corresponding sendTask id
            task.setId(sendTask.getId());
            // add this task to the task list of this users
            taskMap.put(task.getId(), task);

            // start the receiving task
            startReceiveTask(task);

            // show prompt
            System.out.println("--- receiving task created! ---");

            // announce ack
            announceStartTaskACK(task.getId());
        }

    }

    private void startReceiveTask(ReceiveTask receiveTask){
        // start this task, which would create new threads for each file inside it.
        receiveTask.startReceiveTask();
    }

    /**
     * We need to always let the sender suspended the task.
     * Tell the Sender to call its suspendTask method.
     */
    @Override
    public void suspendTask(int taskId) {
        // check if the task exists
        if (taskMap.containsKey(taskId)){
            // get task by id
            Task task = taskMap.get(taskId);

            // check the status of task
            if (task.getStatus().equals(IStatus.STATUS_ONGOING)){

                // suspend the task (status)
                task.suspend();

                // tell the sender to suspend this task
                announceSuspendTask(taskId);

                System.out.println("--- Task suspended! ---");

            }else{
                System.out.println("--- Only the ongoing tasks can be suspended! ---");
            }

        }else{
            System.out.println("--- No such task with this ID! ---");
        }
    }

    /**
     * First, the receiver need to start to receive the task again,
     * which mains create new socket and start new thread for each trans (file).
     * Then we need to tell the sender to accept our sockets and start sending.
     */
    @Override
    public void resumeTask(int taskId) {
        // check if the task exists
        if (taskMap.containsKey(taskId)){
            // get task by id
            ReceiveTask task = (ReceiveTask) taskMap.get(taskId);

            // check the status of task
            if (task.getStatus().equals(IStatus.STATUS_SUSPENDED)){

                // start to receive the task again
                startReceiveTask(task);

                // resume the task (status)
                task.resume();

                // tell the sender to start to send this task again
                announceResumeTaskACK(taskId);

                System.out.println("--- Task resumed! ---");

            }else{
                System.out.println("--- Only the suspended tasks can be resumed! ---");
            }

        }else{
            System.out.println("--- No such task with this ID! ---");
        }
    }


    // ------------------------------------------------------- announce -------------------------------------------------------

    @Override
    public void announceStartTaskACK(int taskId){
        // tell the receiver we will start a task
        Message msg = new Message(Message.START_TASK_ACK, taskId);
        try {
            oos.writeObject(msg);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void announceSuspendTask(int taskId) {
        // tell the sender to suspend the task
        Message msg = new Message(Message.SUSPEND_TASK, taskId);
        try {
            oos.writeObject(msg);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void announceResumeTaskACK(int taskId) {
        // tell the sender to start sending the task again
        // the receiver has created new sockets
        Message msg = new Message(Message.RESUME_TASK_ACK, taskId);
        try {
            oos.writeObject(msg);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
