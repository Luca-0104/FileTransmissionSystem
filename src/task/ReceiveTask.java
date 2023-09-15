package task;

import transmission.DownloadTrans;
import transmission.Transmission;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * A ReceiveTask refers to a group of downloading transmissions
 */
public class ReceiveTask extends Task {

    // constructor
    public ReceiveTask(List<Transmission> transmissionList) {
        super(transmissionList);
    }

    /**
     *  Loop through all the transmission files in this task, for each of them
     *  we will create a new socket connection to the sender, so that they can be run concurrently.
     *  To let the sender know which socket is sending which file, we will send a byte of
     *  transmission id firstly when the socket created.
     *  The transmission will be started in different threads.
     */
    public void startReceiveTask(){

        // the index (id) of the tran in the loop
        int transIndex = 0;

        // loop through the trans in this task to create new socket and thread for each of them
        for (Transmission trans : this.getTransmissionList()){
            int finalTransIndex = transIndex;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // create new socket as client
                        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                        BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());

                        // get an output stream to send the trans index of this trans in this task
                        BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                        bos.write(finalTransIndex);
                        bos.flush();

                        // start download for this trans
                        DownloadTrans downloadTrans = (DownloadTrans) trans;
                        downloadTrans.startDownloadTransmission(bis);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            // increase the trans index
            transIndex += 1;
        }
    }

}
