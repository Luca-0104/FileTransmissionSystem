package starter;

import interfaces.INode;
import interfaces.IConnection;
import node.Sender;
import commandManagement.SenderCommandManager;

import java.io.IOException;
import java.net.ServerSocket;

public class SenderStarter implements IConnection {

    public static void main(String[] args) {

        // get a starter instance
        Starter starter = new Starter();

        // create a server socket
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){

            // create and run a sender obj
            starter.startNode(new Sender(new SenderCommandManager(), serverSocket));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
