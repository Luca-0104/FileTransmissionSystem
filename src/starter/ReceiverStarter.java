package starter;

import interfaces.INode;
import interfaces.IConnection;
import node.Receiver;
import commandManagement.ReceiverCommandManager;

import java.io.IOException;
import java.net.Socket;

public class ReceiverStarter implements IConnection {

    public static void main(String[] args) {

        // get a starter instance
        Starter starter = new Starter();

        // create a socket connection (client)
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT)){

            // create a receiver obj and run it
            starter.startNode(new Receiver(new ReceiverCommandManager(), socket));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
