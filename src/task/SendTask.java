package task;

import transmission.Transmission;
import transmission.UploadTrans;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

/**
 * A SendTask refers to a group of uploading transmissions
 */
public class SendTask extends Task {

    // constructor
    public SendTask(List<Transmission> transmissionList) {
        super(transmissionList);
    }

    /**
     *  Firstly, the server socket would accept new socket connections several times,
     *  which is same as the number of transmissions (files) inside this task.
     *  Each time get a new socket connection, we will read in the first byte, which is the
     *  id of the trans (file) of this socket. So we then know which file should be sent.
     *  Then we will start the transmission of each file, which are running in different threads.
     * @param serverSocket The server socket of the sender
     * @param isResume  Whether this is a resumed task or the first-time beginning task.
     */
    public void startSendTask(ServerSocket serverSocket, boolean isResume){

        // get a loop with same number of the trans in this task
        // each time accept a socket
        for (int i = 0; i < this.getTransmissionList().size(); i++){

            // get socket from file trans receiver
            try {
                Socket socket = serverSocket.accept();

                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());

                // get an input stream of this socket to read in the trans index
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                int transIndex = bis.read();

                // get the corresponding tran obj by index
                Transmission trans = this.getTransmissionList().get(transIndex);

                // start a new thread to send this trans
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UploadTrans uploadTrans = (UploadTrans) trans;
                        uploadTrans.startUploadTransmission(bos, isResume);

                        // we must do this to let the receiver be able to reach "-1"
                        try {
                            socket.shutdownOutput();
                            socket.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
