package message;

import interfaces.IMsgHandler;
import interfaces.INode;
import node.Receiver;
import node.Sender;
import task.SendTask;

import java.io.IOException;
import java.io.ObjectInputStream;

public class MessageHandler implements Runnable, IMsgHandler {

    // input stream for listening msg
    private ObjectInputStream ois;

    // the node that this handler handle msg for
    private INode node;

    // whether should this go on the endless loop
    private boolean keepListening = true;

    // constructor
    public MessageHandler(ObjectInputStream ois, INode node){
        this.ois = ois;
        this.node = node;
    }

    @Override
    public void listenMessage() {
        try {
            // read the msg obj from stream
            Message msg = (Message) ois.readObject();
            String type = msg.getType();
            Object content = msg.getContent();

            // check the type of msg to provided different response
            responseMessage(type, content);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            this.keepListening = false;
        }
    }

    /**
     * Make different responses to the msg received,
     * according to the msg type.
     */
    private void responseMessage(String msgType, Object content) {
        switch (msgType) {
            case Message.START_TASK:
                // this kind of msg can only be listened by receiver node
                SendTask sendTask = (SendTask) content;
                // create download task at receiver node
                ((Receiver) node).createReceiveTask(sendTask);
                break;

            case Message.SUSPEND_TASK:
                // this kind of msg can only be listened by sender node
                // the receiver tells the sender to suspend
                // let the sender suspend the task, the content should be the taskId
                ((Sender) node).suspendTask((int) content);
                break;

            case Message.RESUME_TASK:
                // this kind of msg can only be listened by receiver node
                // the content should be the taskId
                // the receiver should start receiving (create new sockets...)
                ((Receiver) node).resumeTask((int) content);
                break;

            case Message.START_TASK_ACK:
                // this kind of msg can only be listened by sender node
                // in this msg type, the content should be taskId
                // let the sender start to send
                ((Sender) node).startSendTask((int) content, false);
                break;

            case Message.RESUME_TASK_ACK:
                // this kind of msg can only be listened by sender node
                // content here should be task id
                // start that task again and skip the bytes from beginning of the file that have been read last time
                ((Sender) node).startSendTask((int) content, true);
                break;
        }
    }

    @Override
    public void run() {
        while (this.keepListening){
            listenMessage();
        }
    }

}
